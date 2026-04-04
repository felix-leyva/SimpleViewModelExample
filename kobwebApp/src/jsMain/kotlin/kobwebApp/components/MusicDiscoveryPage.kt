package kobwebApp.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.padding
import de.felixlf.simpleviewmodelexample.feature.musicdiscovery.MusicDiscoveryCommand
import de.felixlf.simpleviewmodelexample.feature.musicdiscovery.MusicDiscoveryUIState
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Hr
import org.jetbrains.compose.web.dom.Text

@Composable
fun MusicDiscoveryPage(
    state: MusicDiscoveryUIState,
    sendCommand: (MusicDiscoveryCommand) -> Unit,
) {
    Column(Modifier.fillMaxWidth().padding(16.px)) {
        H1 { Text("Music Discovery") }
        Div { Text("No ViewModel. Just UIModel. Pure Kotlin.") }

        Hr()

        // Genres
        if (state.genres.isNotEmpty()) {
            H3 { Text("Genres") }
            ChipRow(
                items = state.genres,
                selectedItem = state.selectedGenre,
                label = { it.name },
                onSelect = { sendCommand(MusicDiscoveryCommand.SelectGenre(it)) },
            )
        }

        // Artists
        if (state.artists.isNotEmpty()) {
            H3 { Text("Artists") }
            ChipRow(
                items = state.artists,
                selectedItem = state.selectedArtist,
                label = { it.name },
                onSelect = { sendCommand(MusicDiscoveryCommand.SelectArtist(it)) },
            )
        }

        // Albums
        if (state.albums.isNotEmpty()) {
            H3 { Text("Albums") }
            ChipRow(
                items = state.albums,
                selectedItem = state.selectedAlbum,
                label = { "${it.title} (${it.year})" },
                onSelect = { sendCommand(MusicDiscoveryCommand.SelectAlbum(it)) },
            )
        }

        // Tracks
        if (state.tracks.isNotEmpty()) {
            H3 { Text("Tracks") }
            state.tracks.forEach { track ->
                Div(attrs = {
                    style {
                        property("padding", "8px 0")
                    }
                }) {
                    Text("${track.title} — ${track.durationSeconds / 60}:${(track.durationSeconds % 60).toString().padStart(2, '0')}")
                }
            }
        }
    }
}

@Composable
private fun <T> ChipRow(
    items: List<T>,
    selectedItem: T?,
    label: (T) -> String,
    onSelect: (T) -> Unit,
) {
    Row(Modifier.gap(8.px)) {
        items.forEach { item ->
            val isSelected = item == selectedItem
            Button(attrs = {
                onClick { onSelect(item) }
                style {
                    property("padding", "8px 16px")
                    property("border-radius", "20px")
                    property("border", if (isSelected) "2px solid #1976D2" else "1px solid #ccc")
                    property("background-color", if (isSelected) "#1976D2" else "#f5f5f5")
                    property("color", if (isSelected) "white" else "#333")
                    property("cursor", "pointer")
                    property("font-size", "14px")
                }
            }) {
                Text(label(item))
            }
        }
    }
}
