package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.flow.first
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class GetLatestStravaActivities @Inject constructor(
    private val repo: GarminRepository,
    private val isStravaLoggedIn: IsStravaLoggedIn
) {
    suspend operator fun invoke(): Result<List<Activity>> =
        when (isStravaLoggedIn().first()) {
            true -> repo.getLatestStravaActivities(5)
            false -> Result.Success(emptyList())
        }
}