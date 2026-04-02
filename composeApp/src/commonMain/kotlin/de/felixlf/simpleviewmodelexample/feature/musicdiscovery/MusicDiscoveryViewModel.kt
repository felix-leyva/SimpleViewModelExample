package de.felixlf.simpleviewmodelexample.feature.musicdiscovery

import androidx.lifecycle.ViewModel
import de.felixlf.simpleviewmodelexample.uimodel.UIModel

class MusicDiscoveryViewModel(
    musicDiscoveryUIModel: MusicDiscoveryUIModel,
) : ViewModel(musicDiscoveryUIModel.scope),
    UIModel<MusicDiscoveryUIState, MusicDiscoveryCommand> by musicDiscoveryUIModel
