package de.felixlf.simpleviewmodelexample.di

import de.felixlf.simpleviewmodelexample.domain.MusicRepository
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
    factory {
        MusicDiscoveryUIModel(
            scope = CoroutineScope(get<CoroutineContext>() + SupervisorJob()),
            sharingStarted = get<SharingStarted>(),
            repository = get(),
        )
    }
    viewModelOf(::MusicDiscoveryViewModel)
}
