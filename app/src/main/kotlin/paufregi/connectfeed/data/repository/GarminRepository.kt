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
import paufregi.connectfeed.core.utils.toResult
import paufregi.connectfeed.data.api.garmin.GarminConnect
import paufregi.connectfeed.data.api.garmin.models.Metadata
import paufregi.connectfeed.data.api.garmin.models.Summary
import paufregi.connectfeed.data.api.garmin.models.UpdateActivity
import java.io.File
import javax.inject.Inject
import paufregi.connectfeed.data.api.garmin.models.Activity as GarminActivity
import paufregi.connectfeed.data.api.garmin.models.Gear as GarminGear

class GarminRepository @Inject constructor(
    private val garmin: GarminConnect,
) {
    suspend fun getUserProfile() =
        garmin.getUserProfile().toResult().map {
            User(
                id = it.id,
                name = it.name,
                profileImageUrl = it.avatarUrl
            ) }

    suspend fun getActivities(limit: Int): Result<List<GarminActivity>> =
        garmin.getActivities(limit).toResult(emptyList())

    suspend fun getGears(): Result<List<GarminGear>> =
        garmin.getGears().toResult(emptyList())

    suspend fun getCourses(): Result<List<Course>> =
        garmin.getCourses().toResult(emptyList()).map { r -> r.map {
            Course(
                id = it.id,
                name = it.name,
                distance = it.distance,
                type = it.type.type,
            ) } }

    suspend fun getWorkout(id: Long): Result<Workout> =
        garmin.getWorkout(id).toResult().map {
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
        val request = UpdateActivity(
            id = activity.id,
            name = name,
            description = description,
            eventType = eventType,
            metadata = Metadata(course?.id),
            summary = Summary(water, feel, effort)
        )

        return garmin.updateActivity(activity.id, request).toResult()
            .andThen {
                gear?.let {
                    garmin.associateGears(activity.id, listOf(it.id)).toResult()
                } ?: Result.success(Unit)
            }
    }

    suspend fun uploadFile(file: File): Result<Unit> {
        val multipartBody =
            MultipartBody.Part.createFormData("fit", file.name, file.asRequestBody())
        return garmin.uploadFile(multipartBody).toResult()
    }
}