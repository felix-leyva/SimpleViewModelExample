package de.felixlf.simpleviewmodelexample.domain.usecases

import de.felixlf.simpleviewmodelexample.domain.Album
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

fun interface GetAlbumsForArtistUseCase {
    operator fun invoke(artistId: String): Flow<ImmutableList<Album>>
}
