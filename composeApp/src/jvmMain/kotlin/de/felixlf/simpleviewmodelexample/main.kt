package de.felixlf.simpleviewmodelexample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "SimpleViewModelExample",
    ) {
        App()
    }
}