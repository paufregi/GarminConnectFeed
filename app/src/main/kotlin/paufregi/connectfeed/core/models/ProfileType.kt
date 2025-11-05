package paufregi.connectfeed.core.models

sealed class ProfileType(val name: String, val order: Int, val allowCourse: Boolean) {
    data object Any : ProfileType("Any", 1,false)
    data object Running : ProfileType("Running", 2, true)
    data object Cycling : ProfileType("Cycling", 3, true)
    data object Swimming : ProfileType("Swimming", 4, false)
    data object Strength : ProfileType("Strength", 5, false)
    data object Fitness : ProfileType("Fitness", 6, false)
    data object Other : ProfileType("Other", 7, false)

    fun compatible(type: ActivityType): Boolean =
        this == Any || this == type.profileType
}