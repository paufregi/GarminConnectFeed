package paufregi.connectfeed.presentation.syncstrava

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.presentation.ui.models.ProcessState

data class SyncStravaState(
    val process: ProcessState = ProcessState.Processing,
    val activities: List<Activity> = emptyList(),
    val stravaActivities: List<Activity> = emptyList(),
    val activity: Activity? = null,
    val stravaActivity: Activity? = null,
    val description: String? = null,
    val trainingEffect: Boolean = false
)

