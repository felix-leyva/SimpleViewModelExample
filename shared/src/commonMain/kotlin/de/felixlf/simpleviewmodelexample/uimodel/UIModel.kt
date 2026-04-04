package de.felixlf.simpleviewmodelexample.uimodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface UIModel<UIState, UICommand> {
    val scope: CoroutineScope
    val uiState: StateFlow<UIState>
    fun sendCommand(command: UICommand)
}
