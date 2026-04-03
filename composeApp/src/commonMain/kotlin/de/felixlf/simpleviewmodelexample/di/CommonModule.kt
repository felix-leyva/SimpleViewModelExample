package de.felixlf.simpleviewmodelexample.di

import de.felixlf.simpleviewmodelexample.domain.MusicRepository
import de.felixlf.simpleviewmodelexample.domain.usecases.GetAlbumsForArtistUseCase
import de.felixlf.simpleviewmodelexample.domain.usecases.GetArtistsForGenreUseCase
import de.felixlf.simpleviewmodelexample.domain.usecases.GetGenresUseCase
import de.felixlf.simpleviewmodelexample.domain.usecases.GetTracksForAlbumUseCase
import de.felixlf.simpleviewmodelexample.feature.musicdiscovery.MusicDiscoveryUIModel
import de.felixlf.simpleviewmodelexample.feature.musicdiscovery.MusicDiscoveryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

val commonModule = module {
    singleOf(::MusicRepository)

    // Use cases — fun interfaces backed by repository
    single<GetGenresUseCase> { GetGenresUseCase { get<MusicRepository>().getGenres() } }
    single<GetArtistsForGenreUseCase> { GetArtistsForGenreUseCase { get<MusicRepository>().getArtistsForGenre(it) } }
    single<GetAlbumsForArtistUseCase> { GetAlbumsForArtistUseCase { get<MusicRepository>().getAlbumsForArtist(it) } }
    single<GetTracksForAlbumUseCase> { GetTracksForAlbumUseCase { get<MusicRepository>().getTracksForAlbum(it) } }

    factory {
        MusicDiscoveryUIModel(
            scope = CoroutineScope(get<CoroutineContext>() + SupervisorJob()),
            sharingStarted = get<SharingStarted>(),
            getGenres = get(),
            getArtistsForGenre = get(),
            getAlbumsForArtist = get(),
            getTracksForAlbum = get(),
        )
    }
    viewModelOf(::MusicDiscoveryViewModel)
}
