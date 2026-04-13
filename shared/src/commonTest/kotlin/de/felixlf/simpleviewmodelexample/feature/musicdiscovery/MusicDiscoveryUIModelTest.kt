package de.felixlf.simpleviewmodelexample.feature.musicdiscovery

import de.felixlf.simpleviewmodelexample.domain.usecases.FakeGetAlbumsForArtist
import de.felixlf.simpleviewmodelexample.domain.usecases.FakeGetArtistsForGenre
import de.felixlf.simpleviewmodelexample.domain.usecases.FakeGetGenres
import de.felixlf.simpleviewmodelexample.domain.usecases.FakeGetTracksForAlbum
import de.felixlf.simpleviewmodelexample.domain.usecases.GetAlbumsForArtistUseCase
import de.felixlf.simpleviewmodelexample.domain.usecases.GetArtistsForGenreUseCase
import de.felixlf.simpleviewmodelexample.domain.usecases.GetGenresUseCase
import de.felixlf.simpleviewmodelexample.domain.usecases.GetTracksForAlbumUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MusicDiscoveryUIModelTest {

    private lateinit var fakeGenres: FakeGetGenres
    private lateinit var fakeArtists: FakeGetArtistsForGenre
    private lateinit var fakeAlbums: FakeGetAlbumsForArtist
    private lateinit var fakeTracks: FakeGetTracksForAlbum

    @BeforeTest
    fun setup() {
        fakeGenres = FakeGetGenres()
        fakeArtists = FakeGetArtistsForGenre()
        fakeAlbums = FakeGetAlbumsForArtist()
        fakeTracks = FakeGetTracksForAlbum()
    }

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

    @Test
    fun givenDefaultState_whenInitialized_thenGenresAreLoadedAndSelectionsAreNull() = runTest(UnconfinedTestDispatcher()) {
        val (_, states) = createSut()

        val current = states.last()
        assertEquals(fakeGenres.testGenres, current.genres)
        assertNull(current.selectedGenre)
        assertNull(current.selectedArtist)
        assertNull(current.selectedAlbum)
        assertTrue(current.artists.isEmpty())
        assertTrue(current.albums.isEmpty())
        assertTrue(current.tracks.isEmpty())
    }

    @Test
    fun givenGenresLoaded_whenSelectGenre_thenArtistsAreLoaded() = runTest(UnconfinedTestDispatcher()) {
        val (sut, states) = createSut()

        sut.sendCommand(MusicDiscoveryCommand.SelectGenre(fakeGenres.rock))

        val current = states.last()
        assertEquals(fakeGenres.rock, current.selectedGenre)
        assertEquals(fakeArtists.rockArtists, current.artists)
    }

    @Test
    fun givenGenreSelected_whenSelectArtist_thenAlbumsAreLoaded() = runTest(UnconfinedTestDispatcher()) {
        val (sut, states) = createSut()
        sut.sendCommand(MusicDiscoveryCommand.SelectGenre(fakeGenres.rock))

        sut.sendCommand(MusicDiscoveryCommand.SelectArtist(fakeArtists.ledZeppelin))

        val current = states.last()
        assertEquals(fakeArtists.ledZeppelin, current.selectedArtist)
        assertEquals(fakeAlbums.ledZeppelinAlbums, current.albums)
    }

    @Test
    fun givenArtistSelected_whenSelectAlbum_thenTracksAreLoaded() = runTest(UnconfinedTestDispatcher()) {
        val (sut, states) = createSut()
        sut.sendCommand(MusicDiscoveryCommand.SelectGenre(fakeGenres.rock))
        sut.sendCommand(MusicDiscoveryCommand.SelectArtist(fakeArtists.ledZeppelin))

        sut.sendCommand(MusicDiscoveryCommand.SelectAlbum(fakeAlbums.lzIV))

        val current = states.last()
        assertEquals(fakeAlbums.lzIV, current.selectedAlbum)
        assertEquals(fakeTracks.lzIVTracks, current.tracks)
    }

    @Test
    fun givenFullChainSelected_whenSelectDifferentGenre_thenDownstreamResets() = runTest(UnconfinedTestDispatcher()) {
        val (sut, states) = createSut()
        sut.sendCommand(MusicDiscoveryCommand.SelectGenre(fakeGenres.rock))
        sut.sendCommand(MusicDiscoveryCommand.SelectArtist(fakeArtists.ledZeppelin))
        sut.sendCommand(MusicDiscoveryCommand.SelectAlbum(fakeAlbums.lzIV))

        sut.sendCommand(MusicDiscoveryCommand.SelectGenre(fakeGenres.jazz))

        val current = states.last()
        assertEquals(fakeGenres.jazz, current.selectedGenre)
        assertEquals(fakeArtists.jazzArtists, current.artists)
        assertNull(current.selectedArtist)
        assertNull(current.selectedAlbum)
        assertTrue(current.albums.isEmpty())
        assertTrue(current.tracks.isEmpty())
    }

    @Test
    fun givenAlbumSelected_whenSelectDifferentArtist_thenAlbumResetsAndTracksCleared() = runTest(UnconfinedTestDispatcher()) {
        val (sut, states) = createSut()
        sut.sendCommand(MusicDiscoveryCommand.SelectGenre(fakeGenres.rock))
        sut.sendCommand(MusicDiscoveryCommand.SelectArtist(fakeArtists.ledZeppelin))
        sut.sendCommand(MusicDiscoveryCommand.SelectAlbum(fakeAlbums.lzIV))

        sut.sendCommand(MusicDiscoveryCommand.SelectArtist(fakeArtists.pinkFloyd))

        val current = states.last()
        assertEquals(fakeArtists.pinkFloyd, current.selectedArtist)
        assertNull(current.selectedAlbum)
        assertTrue(current.tracks.isEmpty())
    }
}
