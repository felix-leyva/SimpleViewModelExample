package de.felixlf.simpleviewmodelexample.domain.usecases

import de.felixlf.simpleviewmodelexample.domain.Artist
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeGetArtistsForGenre : GetArtistsForGenreUseCase {
    val ledZeppelin = Artist("led-zeppelin", "Led Zeppelin", "rock")
    val pinkFloyd = Artist("pink-floyd", "Pink Floyd", "rock")
    val rockArtists = persistentListOf(ledZeppelin, pinkFloyd)

    val milesDavis = Artist("miles-davis", "Miles Davis", "jazz")
    val jazzArtists = persistentListOf(milesDavis)

    var lastGenreId: String? = null
    var callCount = 0

    override fun invoke(genreId: String): Flow<ImmutableList<Artist>> {
        lastGenreId = genreId
        callCount++
        return when (genreId) {
            "rock" -> flowOf(rockArtists)
            "jazz" -> flowOf(jazzArtists)
            else -> flowOf(persistentListOf())
        }
    }
}
