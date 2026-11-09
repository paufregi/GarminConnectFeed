package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.flow.first
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.data.repository.StravaRepository
import javax.inject.Inject

class GetStravaActivities @Inject constructor(
    private val stravaRepo: StravaRepository,
    private val isStravaLoggedIn: IsStravaLoggedIn,
) {
    suspend operator fun invoke(): Result<List<Activity>> =
        when (isStravaLoggedIn().first()) {
            true -> stravaRepo.getActivities(5)
            false -> Result.success(emptyList())
        }
}