package paufregi.connectfeed.presentation.syncstrava

import paufregi.connectfeed.core.models.Activity

sealed interface SyncStravaAction {
    data class SetActivity(val activity: Activity) : SyncStravaAction
    data class SetStravaActivity(val activity: Activity) : SyncStravaAction
    data class SetDescription(val description: String?) : SyncStravaAction
    data class SetTrainingEffect(val trainingEffect: Boolean) : SyncStravaAction
    data object Save : SyncStravaAction
    data object Restart : SyncStravaAction
}
