package de.felixlf.simpleviewmodelexample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.felixlf.simpleviewmodelexample.di.commonModule
import de.felixlf.simpleviewmodelexample.di.platformModule
import de.felixlf.simpleviewmodelexample.feature.musicdiscovery.MusicDiscoveryScreen
import org.koin.compose.KoinApplication

@Composable
fun App() {
    KoinApplication(application = { modules(commonModule, platformModule) }) {
        MaterialTheme {
            Surface(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .safeContentPadding()
                    .fillMaxSize(),
            ) {
                MusicDiscoveryScreen()
            }
        }
    }
}
