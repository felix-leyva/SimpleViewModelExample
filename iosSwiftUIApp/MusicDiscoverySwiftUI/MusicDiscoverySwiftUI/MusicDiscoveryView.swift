import SwiftUI
import Shared

struct MusicDiscoveryView: View {
    @State private var observable = MusicDiscoveryObservable()

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                Text("Music Discovery")
                    .font(.largeTitle)
                    .bold()

                Text("No ViewModel. Just UIModel. Pure Kotlin.")
                    .font(.subheadline)
                    .foregroundColor(.secondary)

                Divider()

                // Genres
                if !observable.state.genres.isEmpty {
                    SectionHeader(title: "Genres")
                    ChipRow(
                        items: Array(observable.state.genres),
                        selectedItem: observable.state.selectedGenre,
                        label: { $0.name },
                        isEqual: { $0.id == $1.id },
                        onSelect: { genre in
                            observable.send(MusicDiscoveryCommandSelectGenre(genre: genre))
                        }
                    )
                }

                // Artists
                if !observable.state.artists.isEmpty {
                    SectionHeader(title: "Artists")
                    ChipRow(
                        items: Array(observable.state.artists),
                        selectedItem: observable.state.selectedArtist,
                        label: { $0.name },
                        isEqual: { $0.id == $1.id },
                        onSelect: { artist in
                            observable.send(MusicDiscoveryCommandSelectArtist(artist: artist))
                        }
                    )
                }

                // Albums
                if !observable.state.albums.isEmpty {
                    SectionHeader(title: "Albums")
                    ChipRow(
                        items: Array(observable.state.albums),
                        selectedItem: observable.state.selectedAlbum,
                        label: { "\($0.title) (\($0.year))" },
                        isEqual: { $0.id == $1.id },
                        onSelect: { album in
                            observable.send(MusicDiscoveryCommandSelectAlbum(album: album))
                        }
                    )
                }

                // Tracks
                if !observable.state.tracks.isEmpty {
                    SectionHeader(title: "Tracks")
                    ForEach(Array(observable.state.tracks), id: \.id) { track in
                        HStack {
                            Text(track.title)
                            Spacer()
                            let minutes = track.durationSeconds / 60
                            let seconds = track.durationSeconds % 60
                            Text("\(minutes):\(String(format: "%02d", seconds))")
                                .foregroundColor(.secondary)
                        }
                        .padding(.vertical, 4)
                    }
                }
            }
            .padding()
        }
    }
}

struct SectionHeader: View {
    let title: String

    var body: some View {
        Text(title)
            .font(.headline)
            .padding(.top, 8)
    }
}

struct ChipRow<T>: View {
    let items: [T]
    let selectedItem: T?
    let label: (T) -> String
    let isEqual: (T, T) -> Bool
    let onSelect: (T) -> Void

    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 8) {
                ForEach(items.indices, id: \.self) { index in
                    let item = items[index]
                    let isSelected = selectedItem.map { isEqual(item, $0) } ?? false
                    Button(action: { onSelect(item) }) {
                        Text(label(item))
                            .padding(.horizontal, 16)
                            .padding(.vertical, 8)
                            .background(isSelected ? Color.blue : Color(.systemGray6))
                            .foregroundColor(isSelected ? .white : .primary)
                            .cornerRadius(20)
                            .overlay(
                                RoundedRectangle(cornerRadius: 20)
                                    .stroke(isSelected ? Color.blue : Color.gray.opacity(0.3), lineWidth: 1)
                            )
                    }
                }
            }
        }
    }
}
