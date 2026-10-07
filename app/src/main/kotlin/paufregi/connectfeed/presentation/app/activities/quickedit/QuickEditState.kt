package paufregi.connectfeed.presentation.app.activities.quickedit

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.Profile

sealed interface Status {
    data object Loading : Status
    data object Success: Status
    data class Failure(val reason: String) : Status
}
data class QuickEditState(
    val status: Status? = null,

    val activity: Activity,
    val profiles: List<Profile> = emptyList(),
    val gears: List<Gear> = emptyList(),

    val profile: Profile? = null,
    val gear: Gear? = null,
    val description: String? = null,
    val water: Int? = null,
    val effort: Float? = null,
    val feel: Float? = null,
) {
    val availableProfiles: List<Profile>
        get() = profiles.filter { it.type.compatible(activity.type) }

    val availableGears: List<Gear>
        get() = gears.filter { it.type.compatible(activity.type) }
}

