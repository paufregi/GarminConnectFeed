package paufregi.connectfeed.data.repository

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.utils.mapList
import paufregi.connectfeed.core.utils.toResult
import paufregi.connectfeed.data.api.strava.Strava
import paufregi.connectfeed.data.api.strava.models.AthleteDto
import paufregi.connectfeed.data.api.strava.models.UpdateActivityDto
import paufregi.connectfeed.data.api.strava.models.UpdateAthleteDto
import paufregi.connectfeed.data.repository.converters.toCore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StravaRepository @Inject constructor(
    private val strava: Strava,
) {
    suspend fun getAthlete(): Result<AthleteDto> =
        strava.getAthlete().toResult()

    suspend fun getActivities(limit: Int): Result<List<Activity>> =
        strava.getActivities(perPage = limit).toResult()
            .mapList { it.toCore() }

    suspend fun updateAthlete(weight: Float): Result<Unit> =
        strava.updateAthlete(UpdateAthleteDto(weight = weight)).toResult()

    suspend fun updateActivity(
        activity: Activity,
        name: String?,
        description: String?,
        commute: Boolean?,
    ): Result<Unit> {
        val request = UpdateActivityDto(
            name = name,
            description = description,
            commute = commute
        )
        return strava.updateActivity(activity.id, request)
            .toResult()
    }
}