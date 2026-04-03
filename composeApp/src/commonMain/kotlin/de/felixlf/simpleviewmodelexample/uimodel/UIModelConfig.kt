package de.felixlf.simpleviewmodelexample.uimodel

import kotlinx.coroutines.flow.SharingStarted
import kotlin.coroutines.CoroutineContext

data class UIModelScope(
    val coroutineContext: CoroutineContext,
    val sharingStarted: SharingStarted,
)
