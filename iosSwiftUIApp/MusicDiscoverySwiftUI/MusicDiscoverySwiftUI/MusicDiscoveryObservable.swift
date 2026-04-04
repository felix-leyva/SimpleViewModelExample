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
        state = uiModel.uiState.value as! MusicDiscoveryUIState

        // SKIE converts StateFlow to AsyncSequence — collect updates
        observationTask = Task { @MainActor [weak self] in
            guard let uiModel = self?.uiModel else { return }
            for await newState in uiModel.uiState {
                self?.state = newState
            }
        }
    }

    func send(_ command: MusicDiscoveryCommand) {
        uiModel.sendCommand(command: command)
    }

    deinit {
        observationTask?.cancel()
    }
}
