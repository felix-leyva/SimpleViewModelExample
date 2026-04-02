package de.felixlf.simpleviewmodelexample.feature.musicdiscovery

import de.felixlf.simpleviewmodelexample.domain.Album
import de.felixlf.simpleviewmodelexample.domain.Artist
import de.felixlf.simpleviewmodelexample.domain.Genre
import de.felixlf.simpleviewmodelexample.domain.MusicRepository
import de.felixlf.simpleviewmodelexample.domain.Track
import de.felixlf.simpleviewmodelexample.uimodel.UIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class MusicDiscoveryUIModel(
    override val scope: CoroutineScope,
    sharingStarted: SharingStarted,
    private val repository: MusicRepository,
) : UIModel<MusicDiscoveryUIState, MusicDiscoveryCommand> {

    private val genres: ImmutableList<Genre> = repository.getGenres().toImmutableList()

    // --- Internal mutable state (user selections) ---
    private val selectedGenre = MutableStateFlow<Genre?>(null)
    private val selectedArtist = MutableStateFlow<Artist?>(null)
    private val selectedAlbum = MutableStateFlow<Album?>(null)

    // --- Derived flows: each depends on the previous selection ---

    // flatMapLatest #1: genre → artists
    private val artists: StateFlow<ImmutableList<Artist>> = selectedGenre
        .flatMapLatest { genre ->
            flow {
                emit(persistentListOf())
                if (genre != null) {
                    emit(repository.getArtistsForGenre(genre.id).toImmutableList())
                }
            }
        }
        .stateIn(scope, sharingStarted, persistentListOf())

    // flatMapLatest #2: artist → albums
    private val albums: StateFlow<ImmutableList<Album>> = selectedArtist
        .flatMapLatest { artist ->
            flow {
                emit(persistentListOf())
                if (artist != null) {
                    emit(repository.getAlbumsForArtist(artist.id).toImmutableList())
                }
            }
        }
        .stateIn(scope, sharingStarted, persistentListOf())

    // flatMapLatest #3: album → tracks
    private val tracks: StateFlow<ImmutableList<Track>> = selectedAlbum
        .flatMapLatest { album ->
            flow {
                emit(persistentListOf())
                if (album != null) {
                    emit(repository.getTracksForAlbum(album.id).toImmutableList())
                }
            }
        }
        .stateIn(scope, sharingStarted, persistentListOf())

    // --- Public state: combine wires everything together ---
    override val uiState: StateFlow<MusicDiscoveryUIState> = combine(
        selectedGenre,
        artists,
        selectedArtist,
        albums,
        selectedAlbum,
        tracks,
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        MusicDiscoveryUIState(
            genres = genres,
            selectedGenre = values[0] as Genre?,
            artists = values[1] as ImmutableList<Artist>,
            selectedArtist = values[2] as Artist?,
            albums = values[3] as ImmutableList<Album>,
            selectedAlbum = values[4] as Album?,
            tracks = values[5] as ImmutableList<Track>,
        )
    }.stateIn(scope, sharingStarted, MusicDiscoveryUIState.Default.copy(genres = genres))

    // --- Commands: update selections, reset downstream when parent changes ---
    override fun sendCommand(command: MusicDiscoveryCommand) {
        when (command) {
            is MusicDiscoveryCommand.SelectGenre -> {
                selectedGenre.value = command.genre
                selectedArtist.value = null
                selectedAlbum.value = null
            }

            is MusicDiscoveryCommand.SelectArtist -> {
                selectedArtist.value = command.artist
                selectedAlbum.value = null
            }

            is MusicDiscoveryCommand.SelectAlbum -> {
                selectedAlbum.value = command.album
            }
        }
    }
}
