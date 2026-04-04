# Your ViewModel Should Be 3 Lines Long

Companion project for the article series **"Your ViewModel Should Be 3 Lines Long"**

This project demonstrates a UIModel delegation pattern for Kotlin Multiplatform where all UI logic
lives in a pure Kotlin class (`UIModel`) with zero AndroidX dependencies. The ViewModel becomes an
optional 3-line lifecycle wrapper using Kotlin `by` delegation.

## The pattern

```kotlin
// The UIModel — pure Kotlin, no AndroidX, lives in commonMain
class MusicDiscoveryUIModel(
    override val scope: CoroutineScope,
    // use cases injected here...
) : UIModel<MusicDiscoveryUIState, MusicDiscoveryCommand> { ... }

// The ViewModel — 3 lines, optional, only needed on Android
class MusicDiscoveryViewModel(
    musicDiscoveryUIModel: MusicDiscoveryUIModel,
) : ViewModel(musicDiscoveryUIModel.scope),
    UIModel<MusicDiscoveryUIState, MusicDiscoveryCommand> by musicDiscoveryUIModel
```

## Project structure

| Module           | Description                                                                                                                             |
|------------------|-----------------------------------------------------------------------------------------------------------------------------------------|
| `:shared`        | Pure Kotlin module with `UIModel`, state, commands, use cases, and DI. No AndroidX, no Compose. Targets: Android, iOS, JVM, JS, WasmJS. |
| `:composeApp`    | Compose Multiplatform app (Android, iOS, Desktop, Web). Adds the 3-line ViewModel wrapper and consumes the UIModel.                     |
| `:kobwebApp`     | Kobweb (Compose HTML / Kotlin JS) app. Consumes the UIModel directly — no ViewModel needed.                                             |
| `iosSwiftUIApp/` | Native SwiftUI app. Consumes the UIModel via SKIE (StateFlow → Swift AsyncSequence). No ViewModel.                                      |

## Platforms

The same `MusicDiscoveryUIModel` class runs on all platforms:

- **Android** — via Compose Multiplatform + 3-line ViewModel wrapper
- **iOS (Compose)** — via Compose Multiplatform
- **iOS (SwiftUI)** — via SKIE, native `@Observable` wrapper
- **Desktop (JVM)** — via Compose Multiplatform
- **Web (Wasm)** — via Compose Multiplatform
- **Web (Kobweb)** — via Compose HTML, direct StateFlow collection

## Build and run

### Android / Desktop / Web (Compose Multiplatform)

```shell
# Android
./gradlew :composeApp:assembleDebug

# Desktop
./gradlew :composeApp:run

# Web (Wasm)
./gradlew :composeApp:wasmJsBrowserDevelopmentRun

# Web (JS)
./gradlew :composeApp:jsBrowserDevelopmentRun
```

### Kobweb

```shell
cd kobwebApp
kobweb run
```

### iOS (Compose Multiplatform)

Open the project in Android Studio / Fleet and use the iOS run configuration, or open `iosApp/` in
Xcode.

### iOS (SwiftUI)

1. Build the shared framework: `./gradlew :shared:linkDebugFrameworkIosSimulatorArm64`
2. Open `iosSwiftUIApp/MusicDiscoverySwiftUI/MusicDiscoverySwiftUI.xcodeproj` in Xcode
3. Run on simulator

## Key dependencies

- Kotlin Multiplatform
- Compose Multiplatform
- Kobweb
- SKIE (Touchlab) — Swift interop for StateFlow
- Koin — Dependency injection
- kotlinx.collections.immutable
