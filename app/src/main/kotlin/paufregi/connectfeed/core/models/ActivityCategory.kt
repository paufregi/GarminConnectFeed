package paufregi.connectfeed.core.models

sealed class ActivityCategory(val name: String, val order: Int, val allowCourseInProfile: Boolean = false, val types: List<ActivityType> = emptyList()) {
    data object Any : ActivityCategory("Any", 1, false)
    data object Running : ActivityCategory("Running", 2, true, listOf(
        ActivityType.Running,
        ActivityType.TrackRunning,
        ActivityType.TrailRunning,
        ActivityType.TreadmillRunning,
        ActivityType.UltraRun,
        ActivityType.StravaRunning,
        ActivityType.StravaTrailRun
    ))
    data object Cycling : ActivityCategory("Cycling", 3, true, listOf(
        ActivityType.Cycling,
        ActivityType.DownhillBiking,
        ActivityType.EBiking,
        ActivityType.EBikingMountain,
        ActivityType.GravelCycling,
        ActivityType.MountainBiking,
        ActivityType.RoadBiking,
        ActivityType.VirtualRide,
        ActivityType.StravaRide,
        ActivityType.StravaMountainBikeRide,
        ActivityType.StravaGravelRide,
        ActivityType.StravaEBikeRide,
        ActivityType.StravaEMountainBikeRide,
        ActivityType.StravaVirtualRide
    ))
    data object Swimming : ActivityCategory("Swimming", 4, false, listOf(
        ActivityType.Swimming,
        ActivityType.PoolSwimming,
        ActivityType.OpenWaterSwimming,
        ActivityType.StravaSwim
    ))
    data object Strength : ActivityCategory("Strength", 5, false, listOf(
        ActivityType.StrengthTraining,
        ActivityType.StravaWeightTraining
    ))
    data object Fitness : ActivityCategory("Fitness", 6, false, listOf(
        ActivityType.HIIT,
        ActivityType.Breathwork,
        ActivityType.Cardio,
        ActivityType.JumpRope,
        ActivityType.Yoga,
        ActivityType.StravaHIIT,
        ActivityType.StravaWorkout,
        ActivityType.StravaYoga
    ))
    data object Other : ActivityCategory("Other", 99, false, listOf(
        ActivityType.Walking,
        ActivityType.Hiking,
        ActivityType.Snowboarding,
        ActivityType.Kayaking,
        ActivityType.StandUpPaddling,
        ActivityType.Surfing,
        ActivityType.Windsurf,
        ActivityType.Football,
        ActivityType.MultiSport,
        ActivityType.Other,
        ActivityType.StravaWalk,
        ActivityType.StravaHike,
        ActivityType.StravaSnowboard,
        ActivityType.StravaKayaking,
        ActivityType.StravaStandUpPaddling,
        ActivityType.StravaSurfing,
        ActivityType.StravaWindsurf,
        ActivityType.StravaFootball
    ))

    fun compatibleWith(activity: Activity): Boolean =
        this == Any || this.types.contains(activity.type)

    companion object {
        fun findCategory(activity: Activity): ActivityCategory =
            when {
                Running.compatibleWith(activity) -> Running
                Cycling.compatibleWith(activity) -> Cycling
                Swimming.compatibleWith(activity) -> Swimming
                Strength.compatibleWith(activity) -> Strength
                Fitness.compatibleWith(activity) -> Fitness
                else -> Other
            }
        }
    }
