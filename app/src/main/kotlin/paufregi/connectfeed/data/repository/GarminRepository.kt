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
import paufregi.connectfeed.core.utils.mapList
import paufregi.connectfeed.core.utils.toResult
import paufregi.connectfeed.data.api.garmin.GarminConnect
import paufregi.connectfeed.data.api.garmin.models.GearDto
import paufregi.connectfeed.data.api.garmin.models.MetadataDto
import paufregi.connectfeed.data.api.garmin.models.SummaryDto
import paufregi.connectfeed.data.api.garmin.models.UpdateActivityDto
import paufregi.connectfeed.data.repository.converters.toCore
import paufregi.connectfeed.data.utils.Cache
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import paufregi.connectfeed.data.api.garmin.models.EventTypeDto as DataEventType

@Singleton
class GarminRepository @Inject constructor(private val garminConnect: GarminConnect) {

    private val courseCache: Cache<Result<List<Course>>> = Cache()

    suspend fun getUser(): Result<User> =
        garminConnect.getUserProfile()
            .toResult()
            .map { it.toCore() }

    suspend fun getActivities(limit: Int, force: Boolean = false): Result<List<Activity>> =
        garminConnect.getActivities(limit)
            .toResult(emptyList())
            .mapList { it.toCore() }

    suspend fun getCourses(force: Boolean = false): Result<List<Course>> =
        courseCache.get(force) {
            garminConnect.getCourses()
                .toResult(emptyList())
                .mapList { it.toCore() }
        }

    suspend fun getWorkout(id: Long): Result<Workout> =
        garminConnect.getWorkout(id)
            .toResult()
            .map { it.toCore() }

    suspend fun getGears(): Result<List<GearDto>> =
        garminConnect.getGears()
            .toResult()

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
            eventType = DataEventType(eventType?.id, eventType?.key),
            metadata = MetadataDto(course?.id),
            summary = SummaryDto(water, feel, effort)
        )

        return garminConnect.updateActivity(activity.id, request).toResult()
            .andThen {
                gear
                    ?.let { garminConnect.associateGears(activity.id, listOf(it.id)).toResult() }
                    ?: Result.success(Unit)
            }
    }

    suspend fun uploadFile(file: File): Result<Unit> {
        val multipartBody =
            MultipartBody.Part.createFormData("fit", file.name, file.asRequestBody())
        return garminConnect.uploadFile(multipartBody).toResult()
    }
}