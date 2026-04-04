package kobwebApp

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.core.init.InitKobweb
import com.varabyte.kobweb.core.init.InitKobwebContext
import com.varabyte.kobweb.silk.SilkApp
import de.felixlf.simpleviewmodelexample.di.platformModule
import de.felixlf.simpleviewmodelexample.di.sharedModule
import org.koin.core.context.startKoin

@InitKobweb
fun initKoin(ctx: InitKobwebContext) {
    startKoin {
        modules(sharedModule, platformModule)
    }
}

@App
@Composable
fun AppEntry(content: @Composable () -> Unit) {
    SilkApp {
        content()
    }
}
