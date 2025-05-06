package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import kotlin.math.round
import paufregi.connectfeed.core.models.Course as CoreCourse

@Serializable
@JsonIgnoreUnknownKeys
data class Course(
    @SerialName("courseId")
    val id: Long,
    @SerialName("courseName")
    val name: String,
    @SerialName("distanceInMeters")
    val distance: Double,
    @SerialName("activityType")
    val type: ActivityType
) {
    fun toCore(): CoreCourse =
        CoreCourse(
            id = this.id,
            name = this.name,
            distance = round(this.distance),
            type = this.type.toCore()
        )
}
