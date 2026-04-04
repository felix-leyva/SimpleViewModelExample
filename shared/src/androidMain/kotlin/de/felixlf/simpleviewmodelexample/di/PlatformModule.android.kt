package de.felixlf.simpleviewmodelexample.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

actual val platformModule = module {
    factory { CoroutineScope(Dispatchers.Main.immediate + SupervisorJob()) }
}
