package de.felixlf.simpleviewmodelexample.di

import de.felixlf.simpleviewmodelexample.feature.musicdiscovery.MusicDiscoveryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val commonModule = module {
    includes(sharedModule)
    viewModelOf(::MusicDiscoveryViewModel)
}
