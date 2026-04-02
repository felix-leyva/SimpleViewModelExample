package de.felixlf.simpleviewmodelexample.feature.musicdiscovery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.felixlf.simpleviewmodelexample.domain.Album
import de.felixlf.simpleviewmodelexample.domain.Artist
import de.felixlf.simpleviewmodelexample.domain.Genre
import de.felixlf.simpleviewmodelexample.domain.Track
import kotlinx.collections.immutable.ImmutableList
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MusicDiscoveryScreen(
    viewModel: MusicDiscoveryViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    MusicDiscoveryContent(
        state = state,
        sendCommand = viewModel::sendCommand,
    )
}

@Composable
fun MusicDiscoveryContent(
    state: MusicDiscoveryUIState,
    sendCommand: (MusicDiscoveryCommand) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text("Music Discovery", style = MaterialTheme.typography.headlineMedium)
        }

        // Genre selection
        item {
            GenreSelector(
                genres = state.genres,
                selectedGenre = state.selectedGenre,
                onSelect = { sendCommand(MusicDiscoveryCommand.SelectGenre(it)) },
            )
        }

        // Artist selection
        if (state.artists.isNotEmpty()) {
            item {
                ArtistSelector(
                    artists = state.artists,
                    selectedArtist = state.selectedArtist,
                    onSelect = { sendCommand(MusicDiscoveryCommand.SelectArtist(it)) },
                )
            }
        }

        // Album selection
        if (state.albums.isNotEmpty()) {
            item {
                AlbumSelector(
                    albums = state.albums,
                    selectedAlbum = state.selectedAlbum,
                    onSelect = { sendCommand(MusicDiscoveryCommand.SelectAlbum(it)) },
                )
            }
        }

        // Track listing
        if (state.tracks.isNotEmpty()) {
            item {
                Text("Tracks", style = MaterialTheme.typography.titleMedium)
            }
            items(state.tracks) { track ->
                TrackItem(track)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GenreSelector(
    genres: ImmutableList<Genre>,
    selectedGenre: Genre?,
    onSelect: (Genre) -> Unit,
) {
    Column {
        Text("Genre", style = MaterialTheme.typography.titleMedium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            genres.forEach { genre ->
                FilterChip(
                    selected = genre == selectedGenre,
                    onClick = { onSelect(genre) },
                    label = { Text(genre.name) },
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ArtistSelector(
    artists: ImmutableList<Artist>,
    selectedArtist: Artist?,
    onSelect: (Artist) -> Unit,
) {
    Column {
        Text("Artists", style = MaterialTheme.typography.titleMedium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            artists.forEach { artist ->
                FilterChip(
                    selected = artist == selectedArtist,
                    onClick = { onSelect(artist) },
                    label = { Text(artist.name) },
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AlbumSelector(
    albums: ImmutableList<Album>,
    selectedAlbum: Album?,
    onSelect: (Album) -> Unit,
) {
    Column {
        Text("Albums", style = MaterialTheme.typography.titleMedium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            albums.forEach { album ->
                FilterChip(
                    selected = album == selectedAlbum,
                    onClick = { onSelect(album) },
                    label = { Text("${album.title} (${album.year})") },
                )
            }
        }
    }
}

@Composable
private fun TrackItem(track: Track) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        ListItem(
            headlineContent = { Text(track.title) },
            trailingContent = { Text(formatDuration(track.durationSeconds)) },
        )
    }
}

private fun formatDuration(seconds: Int): String {
    val min = seconds / 60
    val sec = seconds % 60
    return "$min:${sec.toString().padStart(2, '0')}"
}
