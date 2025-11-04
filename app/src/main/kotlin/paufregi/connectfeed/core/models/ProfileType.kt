package paufregi.connectfeed.core.models

sealed class ProfileType(val order: Int, val allowCourse: Boolean) {
    data object Any : ProfileType(1,false)
    data object Running : ProfileType(2, true)
    data object Cycling : ProfileType(3, true)
    data object Swimming : ProfileType(4, false)
    data object Strength : ProfileType(5, false)
    data object Fitness : ProfileType(6, false)
    data object Other : ProfileType(7, false)

    fun compatible(type: ActivityType): Boolean =
        this == Any || this == type.profileType
}