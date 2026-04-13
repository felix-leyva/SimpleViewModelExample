package de.felixlf.simpleviewmodelexample.domain.usecases

import de.felixlf.simpleviewmodelexample.domain.Genre
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeGetGenres : GetGenresUseCase {
    val rock = Genre("rock", "Rock")
    val jazz = Genre("jazz", "Jazz")
    val testGenres = persistentListOf(rock, jazz)

    val genres = MutableStateFlow<ImmutableList<Genre>>(testGenres)
    var callCount = 0

    override fun invoke(): Flow<ImmutableList<Genre>> {
        callCount++
        return genres
    }
}
