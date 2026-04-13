package de.felixlf.simpleviewmodelexample.domain.usecases

import de.felixlf.simpleviewmodelexample.domain.Track
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeGetTracksForAlbum : GetTracksForAlbumUseCase {
    val lzIVTracks = persistentListOf(
        Track("1", "Black Dog", 296),
        Track("2", "Stairway to Heaven", 482),
    )

    var lastAlbumId: String? = null
    var callCount = 0

    override fun invoke(albumId: String): Flow<ImmutableList<Track>> {
        lastAlbumId = albumId
        callCount++
        return when (albumId) {
            "lz-iv" -> flowOf(lzIVTracks)
            else -> flowOf(persistentListOf())
        }
    }
}
