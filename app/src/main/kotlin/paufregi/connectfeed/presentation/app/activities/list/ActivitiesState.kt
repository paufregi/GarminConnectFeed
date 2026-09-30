package paufregi.connectfeed.presentation.app.activities.list

import paufregi.connectfeed.core.models.Activity

data class ActivitiesState(
    val loading: Boolean = false,
    val activities: List<Activity> = emptyList()
)

