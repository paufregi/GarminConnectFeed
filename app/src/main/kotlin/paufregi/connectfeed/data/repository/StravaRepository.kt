package paufregi.connectfeed.data.repository

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.utils.apiCall
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.api.strava.Strava
import paufregi.connectfeed.data.api.strava.models.ActivityDto
import paufregi.connectfeed.data.api.strava.models.Athlete
import paufregi.connectfeed.data.api.strava.models.UpdateActivityDto
import paufregi.connectfeed.data.api.strava.models.UpdateAthlete
import javax.inject.Inject

class StravaRepository @Inject constructor(
    private val strava: Strava,
) {
    suspend fun getAthlete(): Result<Athlete> =
        apiCall { strava.getAthlete() }

    suspend fun getActivities(limit: Int): Result<List<ActivityDto>> =
        apiCall { strava.getActivities(perPage = limit) }

    suspend fun updateAthlete(
        weight: Float
    ): Result<Unit> {
        val request = UpdateAthlete(weight = weight)
        return apiCall { strava.updateAthlete(request) }
    }

    suspend fun updateActivity(
        activity: Activity,
        name: String?,
        description: String?,
        commute: Boolean?,
        gear: Gear?,
    ): Result<Unit> = activity.stravaId?.let {
        val request = UpdateActivityDto(
            name = name,
            description = description,
            commute = commute,
            gearId = gear?.stravaId
        )

        return apiCall { strava.updateActivity(activity.stravaId, request) }
    }?: Result.failure("No Strava activity")
}