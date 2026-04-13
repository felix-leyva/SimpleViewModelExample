package de.felixlf.simpleviewmodelexample.feature.musicdiscovery

import de.felixlf.simpleviewmodelexample.domain.Album
import de.felixlf.simpleviewmodelexample.domain.Artist
import de.felixlf.simpleviewmodelexample.domain.Genre
import de.felixlf.simpleviewmodelexample.domain.Track
import de.felixlf.simpleviewmodelexample.domain.usecases.GetAlbumsForArtistUseCase
import de.felixlf.simpleviewmodelexample.domain.usecases.GetArtistsForGenreUseCase
import de.felixlf.simpleviewmodelexample.domain.usecases.GetGenresUseCase
import de.felixlf.simpleviewmodelexample.domain.usecases.GetTracksForAlbumUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

// --- Test data ---

private val rock = Genre("rock", "Rock")
private val jazz = Genre("jazz", "Jazz")

private val testGenres = persistentListOf(rock, jazz)

private val ledZeppelin = Artist("led-zeppelin", "Led Zeppelin", "rock")
private val pinkFloyd = Artist("pink-floyd", "Pink Floyd", "rock")
private val rockArtists = persistentListOf(ledZeppelin, pinkFloyd)

private val milesDavis = Artist("miles-davis", "Miles Davis", "jazz")
private val jazzArtists = persistentListOf(milesDavis)

private val lzIV = Album("lz-iv", "Led Zeppelin IV", "led-zeppelin", 1971)
private val lzHouses = Album("lz-houses", "Houses of the Holy", "led-zeppelin", 1973)
private val ledZeppelinAlbums = persistentListOf(lzIV, lzHouses)

private val kindOfBlue = Album("md-blue", "Kind of Blue", "miles-davis", 1959)
private val milesDavisAlbums = persistentListOf(kindOfBlue)

private val lzIVTracks = persistentListOf(
    Track("1", "Black Dog", 296),
    Track("2", "Stairway to Heaven", 482),
)

// --- Reusable fakes ---

private class FakeGetGenres : GetGenresUseCase {
    val genres = MutableStateFlow<ImmutableList<Genre>>(testGenres)
    override fun invoke(): Flow<ImmutableList<Genre>> = genres
}

private class FakeGetArtistsForGenre : GetArtistsForGenreUseCase {
    val artists = MutableStateFlow<ImmutableList<Artist>>(persistentListOf())
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

private class FakeGetAlbumsForArtist : GetAlbumsForArtistUseCase {
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

private class FakeGetTracksForAlbum : GetTracksForAlbumUseCase {
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

// --- Extension to create SUT with collector into mutableList ---

private val fakeGenres = FakeGetGenres()
private val fakeArtists = FakeGetArtistsForGenre()
private val fakeAlbums = FakeGetAlbumsForArtist()
private val fakeTracks = FakeGetTracksForAlbum()

@OptIn(ExperimentalCoroutinesApi::class)
private fun TestScope.createSut(
    getGenres: GetGenresUseCase = fakeGenres,
    getArtistsForGenre: GetArtistsForGenreUseCase = fakeArtists,
    getAlbumsForArtist: GetAlbumsForArtistUseCase = fakeAlbums,
    getTracksForAlbum: GetTracksForAlbumUseCase = fakeTracks,
): Pair<MusicDiscoveryUIModel, MutableList<MusicDiscoveryUIState>> {
    val sut = MusicDiscoveryUIModel(
        scope = backgroundScope,
        getGenres = getGenres,
        getArtistsForGenre = getArtistsForGenre,
        getAlbumsForArtist = getAlbumsForArtist,
        getTracksForAlbum = getTracksForAlbum,
    )
    val states = mutableListOf<MusicDiscoveryUIState>()
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
        sut.uiState.collect { states.add(it) }
    }
    return sut to states
}

@OptIn(ExperimentalCoroutinesApi::class)
class MusicDiscoveryUIModelTest {

    @Test
    fun givenDefaultState_whenInitialized_thenGenresAreLoadedAndSelectionsAreNull() = runTest(UnconfinedTestDispatcher()) {
        // Given & When
        val (_, states) = createSut()

        // Then
        val current = states.last()
        assertEquals(testGenres, current.genres)
        assertNull(current.selectedGenre)
        assertNull(current.selectedArtist)
        assertNull(current.selectedAlbum)
        assertTrue(current.artists.isEmpty())
        assertTrue(current.albums.isEmpty())
        assertTrue(current.tracks.isEmpty())
    }

    @Test
    fun givenGenresLoaded_whenSelectGenre_thenArtistsAreLoaded() = runTest(UnconfinedTestDispatcher()) {
        // Given
        val (sut, states) = createSut()

        // When
        sut.sendCommand(MusicDiscoveryCommand.SelectGenre(rock))

        // Then
        val current = states.last()
        assertEquals(rock, current.selectedGenre)
        assertEquals(rockArtists, current.artists)
    }

    @Test
    fun givenGenreSelected_whenSelectArtist_thenAlbumsAreLoaded() = runTest(UnconfinedTestDispatcher()) {
        // Given
        val (sut, states) = createSut()
        sut.sendCommand(MusicDiscoveryCommand.SelectGenre(rock))

        // When
        sut.sendCommand(MusicDiscoveryCommand.SelectArtist(ledZeppelin))

        // Then
        val current = states.last()
        assertEquals(ledZeppelin, current.selectedArtist)
        assertEquals(ledZeppelinAlbums, current.albums)
    }

    @Test
    fun givenArtistSelected_whenSelectAlbum_thenTracksAreLoaded() = runTest(UnconfinedTestDispatcher()) {
        // Given
        val (sut, states) = createSut()
        sut.sendCommand(MusicDiscoveryCommand.SelectGenre(rock))
        sut.sendCommand(MusicDiscoveryCommand.SelectArtist(ledZeppelin))

        // When
        sut.sendCommand(MusicDiscoveryCommand.SelectAlbum(lzIV))

        // Then
        val current = states.last()
        assertEquals(lzIV, current.selectedAlbum)
        assertEquals(lzIVTracks, current.tracks)
    }

    @Test
    fun givenFullChainSelected_whenSelectDifferentGenre_thenDownstreamResets() = runTest(UnconfinedTestDispatcher()) {
        // Given — select rock → Led Zeppelin → LZ IV (full chain)
        val (sut, states) = createSut()
        sut.sendCommand(MusicDiscoveryCommand.SelectGenre(rock))
        sut.sendCommand(MusicDiscoveryCommand.SelectArtist(ledZeppelin))
        sut.sendCommand(MusicDiscoveryCommand.SelectAlbum(lzIV))

        // When — switch to jazz
        sut.sendCommand(MusicDiscoveryCommand.SelectGenre(jazz))

        // Then — artist/album reset, jazz artists loaded
        val current = states.last()
        assertEquals(jazz, current.selectedGenre)
        assertEquals(jazzArtists, current.artists)
        assertNull(current.selectedArtist)
        assertNull(current.selectedAlbum)
        assertTrue(current.albums.isEmpty())
        assertTrue(current.tracks.isEmpty())
    }

    @Test
    fun givenAlbumSelected_whenSelectDifferentArtist_thenAlbumResetsAndTracksCleared() = runTest(UnconfinedTestDispatcher()) {
        // Given — select rock → Led Zeppelin → LZ IV
        val (sut, states) = createSut()
        sut.sendCommand(MusicDiscoveryCommand.SelectGenre(rock))
        sut.sendCommand(MusicDiscoveryCommand.SelectArtist(ledZeppelin))
        sut.sendCommand(MusicDiscoveryCommand.SelectAlbum(lzIV))

        // When — switch to Pink Floyd (same genre, different artist)
        sut.sendCommand(MusicDiscoveryCommand.SelectArtist(pinkFloyd))

        // Then — album reset, tracks cleared
        val current = states.last()
        assertEquals(pinkFloyd, current.selectedArtist)
        assertNull(current.selectedAlbum)
        assertTrue(current.tracks.isEmpty())
    }
}
