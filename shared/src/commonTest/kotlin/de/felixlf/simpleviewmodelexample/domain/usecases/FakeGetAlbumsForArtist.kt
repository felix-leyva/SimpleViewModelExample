package de.felixlf.simpleviewmodelexample.domain.usecases

import de.felixlf.simpleviewmodelexample.domain.Album
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeGetAlbumsForArtist : GetAlbumsForArtistUseCase {
    val lzIV = Album("lz-iv", "Led Zeppelin IV", "led-zeppelin", 1971)
    val lzHouses = Album("lz-houses", "Houses of the Holy", "led-zeppelin", 1973)
    val ledZeppelinAlbums = persistentListOf(lzIV, lzHouses)

    val kindOfBlue = Album("md-blue", "Kind of Blue", "miles-davis", 1959)
    val milesDavisAlbums = persistentListOf(kindOfBlue)

    var lastArtistId: String? = null
    var callCount = 0

    override fun invoke(artistId: String): Flow<ImmutableList<Album>> {
        lastArtistId = artistId
        callCount++
        return when (artistId) {
            "led-zeppelin" -> flowOf(ledZeppelinAlbums)
            "miles-davis" -> flowOf(milesDavisAlbums)
            else -> flowOf(persistentListOf())
        }
    }
}
