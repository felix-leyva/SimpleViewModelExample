package de.felixlf.simpleviewmodelexample.domain.usecases

import de.felixlf.simpleviewmodelexample.domain.Artist
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

fun interface GetArtistsForGenreUseCase {
    operator fun invoke(genreId: String): Flow<ImmutableList<Artist>>
}
