package paufregi.connectfeed.data.api.garmin.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import paufregi.connectfeed.data.api.garmin.converters.TrainingEffectConverter
import kotlin.math.round
import paufregi.connectfeed.core.models.Activity as CoreActivity

@Keep
@Serializable
data class Activity(
    @SerializedName("activityId")
    val id: Long,
    @SerializedName("activityName")
    val name: String,
    @SerializedName("activityType")
    val type: ActivityType,
    @SerializedName("distance")
    val distance: Double,
    @SerializedName("trainingEffectLabel")
    val trainingEffectLabel: String?,
) {
    fun toCore(): CoreActivity =
        CoreActivity(
            id = this.id,
            name = this.name,
            type = this.type.toCore(),
            distance = round(this.distance),
            trainingEffect = TrainingEffectConverter.convert(this.trainingEffectLabel)
        )
}