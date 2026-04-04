package de.felixlf.simpleviewmodelexample.feature.musicdiscovery

import de.felixlf.simpleviewmodelexample.domain.Album
import de.felixlf.simpleviewmodelexample.domain.Artist
import de.felixlf.simpleviewmodelexample.domain.Genre
import de.felixlf.simpleviewmodelexample.domain.Track
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class MusicDiscoveryUIState(
    val genres: ImmutableList<Genre>,
    val selectedGenre: Genre?,
    val artists: ImmutableList<Artist>,
    val selectedArtist: Artist?,
    val albums: ImmutableList<Album>,
    val selectedAlbum: Album?,
    val tracks: ImmutableList<Track>,
) {
    companion object {
        val Default = MusicDiscoveryUIState(
            genres = persistentListOf(),
            selectedGenre = null,
            artists = persistentListOf(),
            selectedArtist = null,
            albums = persistentListOf(),
            selectedAlbum = null,
            tracks = persistentListOf(),
        )
    }
}

sealed interface MusicDiscoveryCommand {
    data class SelectGenre(val genre: Genre) : MusicDiscoveryCommand
    data class SelectArtist(val artist: Artist) : MusicDiscoveryCommand
    data class SelectAlbum(val album: Album) : MusicDiscoveryCommand
}
