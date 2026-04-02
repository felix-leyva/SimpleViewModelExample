package de.felixlf.simpleviewmodelexample.domain

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MusicRepository {

    private val genres = listOf(
        Genre("rock", "Rock"),
        Genre("jazz", "Jazz"),
        Genre("electronic", "Electronic"),
    )

    private val artists = listOf(
        Artist("led-zeppelin", "Led Zeppelin", "rock"),
        Artist("pink-floyd", "Pink Floyd", "rock"),
        Artist("miles-davis", "Miles Davis", "jazz"),
        Artist("john-coltrane", "John Coltrane", "jazz"),
        Artist("daft-punk", "Daft Punk", "electronic"),
        Artist("kraftwerk", "Kraftwerk", "electronic"),
    )

    private val albums = listOf(
        Album("lz-iv", "Led Zeppelin IV", "led-zeppelin", 1971),
        Album("lz-houses", "Houses of the Holy", "led-zeppelin", 1973),
        Album("pf-darkside", "The Dark Side of the Moon", "pink-floyd", 1973),
        Album("pf-wall", "The Wall", "pink-floyd", 1979),
        Album("md-blue", "Kind of Blue", "miles-davis", 1959),
        Album("md-brew", "Bitches Brew", "miles-davis", 1970),
        Album("jc-love", "A Love Supreme", "john-coltrane", 1965),
        Album("dp-ram", "Random Access Memories", "daft-punk", 2013),
        Album("dp-discovery", "Discovery", "daft-punk", 2001),
        Album("kw-autobahn", "Autobahn", "kraftwerk", 1974),
    )

    private val tracks = mapOf(
        "lz-iv" to listOf(
            Track("1", "Black Dog", 296),
            Track("2", "Rock and Roll", 220),
            Track("3", "Stairway to Heaven", 482),
        ),
        "lz-houses" to listOf(
            Track("1", "The Song Remains the Same", 330),
            Track("2", "The Rain Song", 459),
            Track("3", "Over the Hills and Far Away", 288),
        ),
        "pf-darkside" to listOf(
            Track("1", "Speak to Me / Breathe", 239),
            Track("2", "Time", 410),
            Track("3", "Money", 382),
            Track("4", "Brain Damage / Eclipse", 289),
        ),
        "pf-wall" to listOf(
            Track("1", "Another Brick in the Wall, Pt. 2", 239),
            Track("2", "Comfortably Numb", 382),
            Track("3", "Hey You", 280),
        ),
        "md-blue" to listOf(
            Track("1", "So What", 562),
            Track("2", "Freddie Freeloader", 589),
            Track("3", "Blue in Green", 327),
        ),
        "md-brew" to listOf(
            Track("1", "Pharaoh's Dance", 1220),
            Track("2", "Bitches Brew", 1628),
        ),
        "jc-love" to listOf(
            Track("1", "Acknowledgement", 468),
            Track("2", "Resolution", 459),
            Track("3", "Pursuance", 289),
            Track("4", "Psalm", 413),
        ),
        "dp-ram" to listOf(
            Track("1", "Give Life Back to Music", 275),
            Track("2", "Get Lucky", 369),
            Track("3", "Instant Crush", 337),
        ),
        "dp-discovery" to listOf(
            Track("1", "One More Time", 320),
            Track("2", "Aerodynamic", 212),
            Track("3", "Digital Love", 301),
        ),
        "kw-autobahn" to listOf(
            Track("1", "Autobahn", 1349),
            Track("2", "Kometenmelodie 1", 375),
            Track("3", "Kometenmelodie 2", 336),
        ),
    )

    fun getGenres(): List<Genre> = genres

    fun getArtistsForGenre(genreId: String): Flow<List<Artist>> = flow {
        delay(300)
        emit(artists.filter { it.genreId == genreId })
    }

    fun getAlbumsForArtist(artistId: String): Flow<List<Album>> = flow {
        delay(300)
        emit(albums.filter { it.artistId == artistId })
    }

    fun getTracksForAlbum(albumId: String): Flow<List<Track>> = flow {
        delay(200)
        emit(tracks[albumId].orEmpty())
    }
}
