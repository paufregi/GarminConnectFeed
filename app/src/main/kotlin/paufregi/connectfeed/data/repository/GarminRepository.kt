package paufregi.connectfeed.data.repository

import androidx.compose.ui.util.fastMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.models.Weight
import paufregi.connectfeed.data.api.garmin.GarminConnect
import paufregi.connectfeed.data.api.garmin.models.Metadata
import paufregi.connectfeed.data.api.garmin.models.Summary
import paufregi.connectfeed.data.api.garmin.models.UpdateActivity
import paufregi.connectfeed.data.api.strava.Strava
import paufregi.connectfeed.data.api.strava.models.UpdateProfile
import paufregi.connectfeed.data.api.utils.callApi
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
    suspend fun fetchUser(): Result<User> =
        callApi(
            { garminConnect.getUserProfile() },
            { res -> res.body()!!.toCore() }
        )

    fun getAllProfiles(): Flow<List<Profile>> =
        garminDao.getAllProfiles().map { it.fastMap { it.toCore() } }

    suspend fun getProfile(id: Long): Profile? =
        garminDao.getProfile(id)?.toCore()

    suspend fun saveProfile(profile: Profile) =
        garminDao.saveProfile(profile.toEntity())

    suspend fun deleteProfile(profile: Profile) =
        garminDao.deleteProfile(profile.toEntity())

    suspend fun getLatestActivities(limit: Int): Result<List<Activity>> =
        callApi(
            { garminConnect.getLatestActivities(limit) },
            { res -> res.body()?.fastMap { it.toCore() } ?: emptyList() }
        )

    suspend fun getLatestStravaActivities(limit: Int): Result<List<Activity>> =
        callApi(
            { strava.getLatestActivities(perPage = limit) },
            { res -> res.body()?.fastMap { it.toCore() } ?: emptyList() }
        )

    suspend fun getCourses(): Result<List<Course>> =
        callApi(
            { garminConnect.getCourses() },
            { res -> res.body()?.fastMap { it.toCore() } ?: emptyList() }
        )

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
        return callApi(
            { garminConnect.updateActivity(activity.id, request) },
            { }
        )
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
        return callApi(
            { strava.updateActivity(activity.id, request) },
            { }
        )
    }

    suspend fun updateStravaProfile(
        weight: Float
    ): Result<Unit> {
        val request = UpdateProfile(weight = weight)
        return callApi(
            { strava.updateProfile(request) },
            { }
        )
    }

    suspend fun uploadFile(file: File): Result<Unit> {
        val multipartBody =
            MultipartBody.Part.createFormData("fit", file.name, file.asRequestBody())
        return callApi(
            { garminConnect.uploadFile(multipartBody) },
            { }
        )
    }
}