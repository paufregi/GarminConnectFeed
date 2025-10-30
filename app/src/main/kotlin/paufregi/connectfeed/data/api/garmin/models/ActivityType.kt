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
        1L -> CoreActivityType.Running
        2L, 10L -> CoreActivityType.Cycling
        3L -> CoreActivityType.Hiking
        4L -> CoreActivityType.Other
        6L -> CoreActivityType.TrailRunning
        9L -> CoreActivityType.Walking
        11L -> CoreActivityType.Cardio
        180L -> CoreActivityType.HIIT
        13L -> CoreActivityType.StrengthTraining
        18L -> CoreActivityType.TreadmillRunning
        26L -> CoreActivityType.Swimming
        27L -> CoreActivityType.PoolSwimming
        28L -> CoreActivityType.OpenWaterSwimming
        176L -> CoreActivityType.EBiking
        163L -> CoreActivityType.Yoga
        164L -> CoreActivityType.Breathwork
        254L -> CoreActivityType.JumpRope
        else -> CoreActivityType.Other
    }
}