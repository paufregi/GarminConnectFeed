package paufregi.connectfeed.presentation.app.activities.list

import paufregi.connectfeed.core.models.Activity

data class ActivitiesState(
    val loading: Boolean = true,
    val activities: List<Activity> = emptyList()
)

