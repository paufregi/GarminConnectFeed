package paufregi.connectfeed.data.repository

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.core.utils.toResult
import paufregi.connectfeed.data.api.strava.Strava
import paufregi.connectfeed.data.api.strava.models.Athlete
import paufregi.connectfeed.data.api.strava.models.UpdateActivity
import paufregi.connectfeed.data.api.strava.models.UpdateAthlete
import javax.inject.Inject
import paufregi.connectfeed.data.api.strava.models.Activity as StravaActivity

class StravaRepository @Inject constructor(
    private val strava: Strava,
) {
    suspend fun getAthlete(): Result<Athlete> =
        strava.getAthlete().toResult()

    suspend fun getActivities(limit: Int): Result<List<StravaActivity>> =
        strava.getActivities(perPage = limit).toResult()

    suspend fun updateAthlete(
        weight: Float
    ): Result<Unit> {
        val request = UpdateAthlete(weight = weight)
        return strava.updateAthlete(request).toResult()
    }

    suspend fun updateActivity(
        activity: Activity,
        name: String?,
        description: String?,
        commute: Boolean?,
        gear: Gear?,
    ): Result<Unit> = activity.stravaId?.let {
        val request = UpdateActivity(
            name = name,
            description = description,
            commute = commute,
            gearId = gear?.stravaId
        )

        return strava.updateActivity(activity.stravaId, request).toResult()
    }?: Result.failure("No Strava activity")
}