import SwiftUI
import Shared

@main
struct MusicDiscoveryApp: App {
    init() {
        KoinHelperKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            MusicDiscoveryView()
        }
    }
}
