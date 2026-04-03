package de.felixlf.simpleviewmodelexample.feature.musicdiscovery

import de.felixlf.simpleviewmodelexample.domain.Album
import de.felixlf.simpleviewmodelexample.domain.Artist
import de.felixlf.simpleviewmodelexample.domain.Genre
import de.felixlf.simpleviewmodelexample.domain.MusicRepository
import de.felixlf.simpleviewmodelexample.uimodel.UIModel
import de.felixlf.simpleviewmodelexample.uimodel.combine
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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
    private val artists = selectedGenre.flatMapLatest { genre ->
        when {
            genre != null -> repository.getArtistsForGenre(genre.id)
            else -> flowOf(persistentListOf())
        }
    }

    // flatMapLatest #2: artist → albums
    private val albums = selectedArtist.flatMapLatest { artist ->
        when {
            artist != null -> repository.getAlbumsForArtist(artist.id)
            else -> flowOf(persistentListOf())
        }
    }

    // flatMapLatest #3: album → tracks
    private val tracks = selectedAlbum.flatMapLatest { album ->
        when {
            album != null -> repository.getTracksForAlbum(album.id)
            else -> flowOf(persistentListOf())
        }
    }

    // --- Public state: combine wires everything together ---
    override val uiState: StateFlow<MusicDiscoveryUIState> = combine(
        selectedGenre,
        artists,
        selectedArtist,
        albums,
        selectedAlbum,
        tracks,
    ) { selectedGenre, artists, selectedArtist, albums, selectedAlbum, tracks ->
        MusicDiscoveryUIState(
            genres = genres,
            selectedGenre = selectedGenre,
            artists = artists,
            selectedArtist = selectedArtist,
            albums = albums,
            selectedAlbum = selectedAlbum,
            tracks = tracks,
        )
    }.stateIn(scope, sharingStarted, MusicDiscoveryUIState.Default)

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
