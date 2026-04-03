package de.felixlf.simpleviewmodelexample.di

import de.felixlf.simpleviewmodelexample.uimodel.UIModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import org.koin.dsl.module

actual val platformModule = module {
    single {
        UIModelScope(
            coroutineContext = Dispatchers.Main.immediate,
            sharingStarted = SharingStarted.WhileSubscribed(5_000),
        )
    }
}
