package paufregi.connectfeed.presentation.app.activities.quickedit

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.presentation.ui.models.Status

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
    val notFound: Boolean
        get() = activity == Activity.EMPTY

    val availableProfiles: List<Profile>
        get() = profiles.filter { it.type.compatible(activity.type) }

    val availableGears: List<Gear>
        get() = gears.filter { it.type.compatible(activity.type) }

    val canSave: Boolean
        get() = profile != null
}

