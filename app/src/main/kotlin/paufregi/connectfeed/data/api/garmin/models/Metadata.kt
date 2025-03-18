package paufregi.connectfeed.data.api.garmin.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Metadata(
    @SerializedName("associatedCourseId")
    val courseId: Long?,
)