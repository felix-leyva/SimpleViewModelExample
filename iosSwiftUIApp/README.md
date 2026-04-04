# SwiftUI App — Music Discovery (No ViewModel)

This SwiftUI app consumes the shared `MusicDiscoveryUIModel` directly — no AndroidX ViewModel.

## Setup

### 1. Build the Shared framework

```bash
cd /Users/felix.leyva/Samples/SimpleViewModelExample
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64
```

### 2. Create the Xcode project

1. Open Xcode → File → New → Project → iOS → App
2. Name: `MusicDiscoverySwiftUI`, Interface: SwiftUI, Language: Swift
3. Save it inside `iosSwiftUIApp/`
4. Delete the generated `ContentView.swift` and `App.swift`
5. Add the 3 Swift files from this directory to the project:
   - `MusicDiscoveryApp.swift`
   - `MusicDiscoveryObservable.swift`
   - `MusicDiscoveryView.swift`
6. Link the Shared framework:
   - Go to Target → General → Frameworks, Libraries, and Embedded Content
   - Click "+" → Add Other → Add Files
   - Navigate to `shared/build/bin/iosSimulatorArm64/debugFramework/Shared.framework`
   - Set embed to "Embed & Sign"
7. Add framework search path:
   - Target → Build Settings → Framework Search Paths
   - Add: `$(SRCROOT)/../shared/build/bin/iosSimulatorArm64/debugFramework`
8. Build and run on simulator

## How it works

- `MusicDiscoveryApp.swift` — App entry, initializes Koin
- `MusicDiscoveryObservable.swift` — `@Observable` wrapper that collects `StateFlow` via SKIE's `AsyncSequence` bridging
- `MusicDiscoveryView.swift` — Pure SwiftUI view consuming the state and sending commands

**The UIModel class is identical to what Android, Desktop, Web, and Kobweb use.** Only the scope provider and the UI layer differ.
