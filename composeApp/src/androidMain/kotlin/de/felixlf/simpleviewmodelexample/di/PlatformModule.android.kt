package de.felixlf.simpleviewmodelexample.di

import de.felixlf.simpleviewmodelexample.uimodel.UIDispatcher
import de.felixlf.simpleviewmodelexample.uimodel.UIStateSharing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import org.koin.dsl.module

actual val platformModule = module {
    single { UIDispatcher(Dispatchers.Main.immediate) }
    single { UIStateSharing(SharingStarted.WhileSubscribed(5_000)) }
}
