package paufregi.connectfeed.data.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.models.Workout
import paufregi.connectfeed.core.utils.andThen
import paufregi.connectfeed.core.utils.apiCall
import paufregi.connectfeed.data.api.garmin.GarminConnect
import paufregi.connectfeed.data.api.garmin.models.ActivityDto
import paufregi.connectfeed.data.api.garmin.models.GearDto
import paufregi.connectfeed.data.api.garmin.models.Metadata
import paufregi.connectfeed.data.api.garmin.models.Summary
import paufregi.connectfeed.data.api.garmin.models.UpdateActivityDto
import java.io.File
import javax.inject.Inject

class GarminRepository @Inject constructor(
    private val garmin: GarminConnect,
) {

    // Replaced atomically on each fetch so readers never observe a half-populated cache.
    @Volatile
    private var activities: Map<Long, ActivityDto> = emptyMap()

    suspend fun getUserProfile() =
        apiCall { garmin.getUserProfile() }.map {
            User(
                id = it.id,
                name = it.name,
                profileImageUrl = it.avatarUrl
            ) }

    suspend fun getActivities(limit: Int): Result<List<ActivityDto>> =
        apiCall(emptyList()) { garmin.getActivities(limit) }
            .onSuccess { activities = it.associateBy { a -> a.id } }

    fun getActivity(id: Long): ActivityDto? = activities[id]

    suspend fun getGears(): Result<List<GearDto>> =
        apiCall(emptyList()) { garmin.getGears() }

    suspend fun getCourses(): Result<List<Course>> =
        apiCall(emptyList()) { garmin.getCourses() }.map { r -> r.map {
            Course(
                id = it.id,
                name = it.name,
                distance = it.distance,
                type = it.type.type,
            ) } }

    suspend fun getWorkout(id: Long): Result<Workout> =
        apiCall { garmin.getWorkout(id) }.map {
            Workout(
                id = it.id,
                name = it.name,
            ) }

    suspend fun updateActivity(
        activity: Activity,
        name: String?,
        description: String?,
        eventType: EventType?,
        course: Course?,
        water: Int?,
        feel: Float?,
        effort: Float?,
        gear: Gear?,
    ): Result<Unit> {
        val request = UpdateActivityDto(
            id = activity.id,
            name = name,
            description = description,
            eventType = eventType,
            metadata = Metadata(course?.id),
            summary = Summary(water, feel, effort)
        )

        return apiCall { garmin.updateActivity(activity.id, request) }
            .andThen {
                gear?.let {
                    apiCall { garmin.associateGears(activity.id, listOf(it.id)) }
                } ?: Result.success(Unit)
            }
    }

    suspend fun uploadFile(file: File): Result<Unit> {
        val multipartBody =
            MultipartBody.Part.createFormData("fit", file.name, file.asRequestBody())
        return apiCall { garmin.uploadFile(multipartBody) }
    }
}