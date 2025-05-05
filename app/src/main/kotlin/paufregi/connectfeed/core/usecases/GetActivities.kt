package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class GetActivities @Inject constructor(private val garminRepository: GarminRepository) {
    suspend operator fun invoke(force: Boolean = false): Result<List<Activity>> =
        garminRepository.getActivities(limit = 5, force = force)
}