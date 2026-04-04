package kobwebApp.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.varabyte.kobweb.core.Page
import de.felixlf.simpleviewmodelexample.feature.musicdiscovery.MusicDiscoveryUIModel
import kobwebApp.components.MusicDiscoveryPage
import org.koin.core.context.GlobalContext

@Page
@Composable
fun HomePage() {
    val uiModel = remember { GlobalContext.get().get<MusicDiscoveryUIModel>() }
    val state by uiModel.uiState.collectAsState()

    MusicDiscoveryPage(
        state = state,
        sendCommand = uiModel::sendCommand,
    )
}
