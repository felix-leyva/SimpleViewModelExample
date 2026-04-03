package de.felixlf.simpleviewmodelexample.feature.musicdiscovery

import de.felixlf.simpleviewmodelexample.domain.Album
import de.felixlf.simpleviewmodelexample.domain.Artist
import de.felixlf.simpleviewmodelexample.domain.Genre
import de.felixlf.simpleviewmodelexample.domain.usecases.GetAlbumsForArtistUseCase
import de.felixlf.simpleviewmodelexample.domain.usecases.GetArtistsForGenreUseCase
import de.felixlf.simpleviewmodelexample.domain.usecases.GetGenresUseCase
import de.felixlf.simpleviewmodelexample.domain.usecases.GetTracksForAlbumUseCase
import de.felixlf.simpleviewmodelexample.uimodel.UIModel
import de.felixlf.simpleviewmodelexample.uimodel.combine
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

private val defaultSharing = SharingStarted.WhileSubscribed(5_000)

@OptIn(ExperimentalCoroutinesApi::class)
class MusicDiscoveryUIModel(
    override val scope: CoroutineScope,
    getGenres: GetGenresUseCase,
    getArtistsForGenre: GetArtistsForGenreUseCase,
    getAlbumsForArtist: GetAlbumsForArtistUseCase,
    getTracksForAlbum: GetTracksForAlbumUseCase,
) : UIModel<MusicDiscoveryUIState, MusicDiscoveryCommand> {

    private val genres = getGenres()

    // --- Internal mutable state (user selections) ---
    private val selectedGenre = MutableStateFlow<Genre?>(null)
    private val selectedArtist = MutableStateFlow<Artist?>(null)
    private val selectedAlbum = MutableStateFlow<Album?>(null)

    // --- Derived flows: each depends on the previous selection ---

    // flatMapLatest #1: genre → artists
    private val artists = selectedGenre.flatMapLatest { genre ->
        when {
            genre != null -> getArtistsForGenre(genre.id)
            else -> flowOf(persistentListOf())
        }
    }

    // flatMapLatest #2: artist → albums
    private val albums = selectedArtist.flatMapLatest { artist ->
        when {
            artist != null -> getAlbumsForArtist(artist.id)
            else -> flowOf(persistentListOf())
        }
    }

    // flatMapLatest #3: album → tracks
    private val tracks = selectedAlbum.flatMapLatest { album ->
        when {
            album != null -> getTracksForAlbum(album.id)
            else -> flowOf(persistentListOf())
        }
    }

    // --- Public state: combine wires everything together ---
    override val uiState: StateFlow<MusicDiscoveryUIState> = combine(
        genres,
        selectedGenre,
        artists,
        selectedArtist,
        albums,
        selectedAlbum,
        tracks,
        ::MusicDiscoveryUIState
    ).stateIn(scope, defaultSharing, MusicDiscoveryUIState.Default)

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
