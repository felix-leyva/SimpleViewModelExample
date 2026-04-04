import Foundation
import Shared

@Observable
class MusicDiscoveryObservable {
    private let uiModel: MusicDiscoveryUIModel
    var state: MusicDiscoveryUIState

    private var observationTask: Task<Void, Never>?

    init() {
        let helper = KoinHelper()
        uiModel = helper.getMusicDiscoveryUIModel()
        state = uiModel.uiState.value

        // SKIE converts StateFlow to AsyncSequence — collect updates
        observationTask = Task { @MainActor [weak self] in
            for await newState in self?.uiModel.uiState ?? AsyncStream { $0.finish() } {
                self?.state = newState
            }
        }
    }

    func send(_ command: MusicDiscoveryCommand) {
        uiModel.sendCommand(command: command)
    }

    deinit {
        observationTask?.cancel()
        uiModel.scope.cancel(cause: nil)
    }
}
