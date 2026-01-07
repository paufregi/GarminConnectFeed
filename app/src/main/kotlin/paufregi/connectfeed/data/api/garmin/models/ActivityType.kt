package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import paufregi.connectfeed.core.models.ActivityType as CoreActivityType

@Serializable
data class ActivityType(
    @SerialName("typeId")
    val id: Long,
    @SerialName("typeKey")
    val key: String
) {
    fun toCore(): CoreActivityType = when (this.id) {
        // Running
        1L -> CoreActivityType.Running
        8L -> CoreActivityType.TrackRunning
        6L -> CoreActivityType.TrailRunning
        18L -> CoreActivityType.TreadmillRunning
        181L -> CoreActivityType.UltraRun

        // Cycling
        2L -> CoreActivityType.Cycling
        20L -> CoreActivityType.DownhillBiking
        176L -> CoreActivityType.EBiking
        175L -> CoreActivityType.EBikingMountain
        143L -> CoreActivityType.GravelCycling
        5L -> CoreActivityType.MountainBiking
        10L -> CoreActivityType.RoadBiking
        25L -> CoreActivityType.IndoorRide
        152L -> CoreActivityType.VirtualRide

        // Fitness
        180L -> CoreActivityType.HIIT
        164L -> CoreActivityType.Breathwork
        11L -> CoreActivityType.Cardio
        254L -> CoreActivityType.JumpRope
        13L -> CoreActivityType.StrengthTraining
        163L -> CoreActivityType.Yoga

        // Swimming
        27L -> CoreActivityType.PoolSwimming
        28L -> CoreActivityType.OpenWaterSwimming
        26L -> CoreActivityType.Swimming

        // Other
        9L -> CoreActivityType.Walking
        3L -> CoreActivityType.Hiking
        252L -> CoreActivityType.Snowboarding
        231L -> CoreActivityType.Kayaking
        239L -> CoreActivityType.StandUpPaddling
        240L -> CoreActivityType.Surfing
        242L -> CoreActivityType.Windsurf

        else -> CoreActivityType.Unknown
    }
}