package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseDto(
    @SerialName("courseId")
    val id: Long,
    @SerialName("courseName")
    val name: String,
    @SerialName("distanceInMeters")
    val distance: Double,
    @SerialName("activityType")
    val type: ActivityTypeDto
)