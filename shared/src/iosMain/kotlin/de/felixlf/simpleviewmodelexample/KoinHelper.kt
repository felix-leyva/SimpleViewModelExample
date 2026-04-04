package de.felixlf.simpleviewmodelexample

import de.felixlf.simpleviewmodelexample.di.platformModule
import de.felixlf.simpleviewmodelexample.di.sharedModule
import de.felixlf.simpleviewmodelexample.feature.musicdiscovery.MusicDiscoveryUIModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(sharedModule, platformModule)
    }
}

class KoinHelper : KoinComponent {
    fun getMusicDiscoveryUIModel(): MusicDiscoveryUIModel = get()
}
