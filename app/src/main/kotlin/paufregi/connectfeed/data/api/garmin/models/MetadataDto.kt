package paufregi.connectfeed.data.api.garmin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MetadataDto(
    @SerialName("associatedCourseId")
    val courseId: Long?,
)