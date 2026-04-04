package kobwebApp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.silk.SilkApp
import de.felixlf.simpleviewmodelexample.di.platformModule
import de.felixlf.simpleviewmodelexample.di.sharedModule
import org.koin.core.context.startKoin

private var koinStarted = false

@App
@Composable
fun AppEntry(content: @Composable () -> Unit) {
    LaunchedEffect(Unit) {
        if (!koinStarted) {
            startKoin {
                modules(sharedModule, platformModule)
            }
            koinStarted = true
        }
    }
    SilkApp {
        content()
    }
}
