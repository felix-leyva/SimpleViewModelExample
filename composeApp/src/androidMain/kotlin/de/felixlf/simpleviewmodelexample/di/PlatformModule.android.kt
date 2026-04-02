package de.felixlf.simpleviewmodelexample.di

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

actual val platformModule = module {
    single<CoroutineContext> { Dispatchers.Main.immediate }
    single<SharingStarted> { SharingStarted.WhileSubscribed(5_000) }
}
