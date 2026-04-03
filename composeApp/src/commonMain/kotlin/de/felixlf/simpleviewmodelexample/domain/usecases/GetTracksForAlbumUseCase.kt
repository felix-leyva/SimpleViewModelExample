package de.felixlf.simpleviewmodelexample.domain.usecases

import de.felixlf.simpleviewmodelexample.domain.Track
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

fun interface GetTracksForAlbumUseCase {
    operator fun invoke(albumId: String): Flow<ImmutableList<Track>>
}
