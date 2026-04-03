package de.felixlf.simpleviewmodelexample.domain.usecases

import de.felixlf.simpleviewmodelexample.domain.Genre
import kotlinx.collections.immutable.ImmutableList

fun interface GetGenresUseCase {
    operator fun invoke(): ImmutableList<Genre>
}
