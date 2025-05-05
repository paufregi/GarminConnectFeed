package paufregi.connectfeed.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.utils.toResult
import paufregi.connectfeed.data.api.garmin.GarminConnect
import paufregi.connectfeed.data.api.garmin.models.Metadata
import paufregi.connectfeed.data.api.garmin.models.Summary
import paufregi.connectfeed.data.api.garmin.models.UpdateActivity
import paufregi.connectfeed.data.api.strava.Strava
import paufregi.connectfeed.data.api.strava.models.UpdateProfile
import paufregi.connectfeed.data.utils.Cache
import paufregi.connectfeed.data.utils.withCache
import paufregi.connectfeed.data.database.GarminDao
import paufregi.connectfeed.data.database.coverters.toCore
import paufregi.connectfeed.data.database.coverters.toEntity
import java.io.File
import javax.inject.Inject
import paufregi.connectfeed.data.api.garmin.models.EventType as DataEventType
import paufregi.connectfeed.data.api.strava.models.UpdateActivity as UpdateStravaActivity

class GarminRepository @Inject constructor(
    private val garminDao: GarminDao,
    private val garminConnect: GarminConnect,
    private val strava: Strava,
) {
    val activitiesCache: Cache<Result<List<Activity>>> = Cache()
    val courseCache: Cache<Result<List<Course>>> = Cache()
    val stravaActivityCache: Cache<Result<List<Activity>>> = Cache()


    suspend fun fetchUser(): Result<User> =
        garminConnect.getUserProfile()
            .toResult()
            .map { it.toCore() }

    fun getAllProfiles(): Flow<List<Profile>> =
        garminDao.getAllProfiles().map { it.map { it.toCore() } }

    suspend fun getProfile(id: Long): Profile? =
        garminDao.getProfile(id)?.toCore()

    suspend fun saveProfile(profile: Profile) =
        garminDao.saveProfile(profile.toEntity())

    suspend fun deleteProfile(profile: Profile) =
        garminDao.deleteProfile(profile.toEntity())

    suspend fun getActivities(limit: Int): Result<List<Activity>> =
        withCache(activitiesCache) {
            garminConnect.getActivities(limit)
                .toResult(emptyList())
                .map { it.map { it.toCore() } }
        }.onFailure { activitiesCache.invalidate() }

    suspend fun getStravaActivities(limit: Int): Result<List<Activity>> =
        withCache(stravaActivityCache) {
            strava.getActivities(perPage = limit)
                .toResult(emptyList())
                .map { it.map { it.toCore() } }
        }.onFailure { activitiesCache.invalidate() }

    suspend fun getCourses(): Result<List<Course>> =
        withCache(courseCache) {
            garminConnect.getCourses()
                .toResult(emptyList())
                .map { it.map { it.toCore() } }
        }.onFailure { activitiesCache.invalidate() }


    suspend fun updateActivity(
        activity: Activity,
        name: String?,
        eventType: EventType?,
        course: Course?,
        water: Int?,
        feel: Float?,
        effort: Float?
    ): Result<Unit> {
        val request = UpdateActivity(
            id = activity.id,
            name = name,
            eventType = DataEventType(eventType?.id, eventType?.key),
            metadata = Metadata(course?.id),
            summary = Summary(water, feel, effort)
        )
        return garminConnect.updateActivity(activity.id, request)
            .toResult()
            .onSuccess { activitiesCache.invalidate() }
    }

    suspend fun updateStravaActivity(
        activity: Activity,
        name: String?,
        description: String?,
        commute: Boolean?,
    ): Result<Unit> {
        val request = UpdateStravaActivity(
            name = name,
            description = description,
            commute = commute
        )
        return strava.updateActivity(activity.id, request)
            .toResult()
            .onSuccess { stravaActivityCache.invalidate() }
    }

    suspend fun updateStravaProfile(
        weight: Float
    ): Result<Unit> {
        val request = UpdateProfile(weight = weight)
        return strava.updateProfile(request).toResult()
    }

    suspend fun uploadFile(file: File): Result<Unit> {
        val multipartBody =
            MultipartBody.Part.createFormData("fit", file.name, file.asRequestBody())
        return garminConnect.uploadFile(multipartBody).toResult()
    }
}