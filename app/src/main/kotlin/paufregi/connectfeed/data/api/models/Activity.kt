package paufregi.connectfeed.data.api.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
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
) {
    fun toCore(): CoreActivity =
        CoreActivity(
            id = this.id,
            name = this.name,
            type = this.type.toCore(),
            distance = round(this.distance)
        )
}