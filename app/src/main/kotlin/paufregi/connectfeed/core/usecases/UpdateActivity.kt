package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class UpdateActivity @Inject constructor (private val garminRepository: GarminRepository) {
    suspend operator fun invoke(
        activity: Activity?,
        profile: Profile?,
        feel: Float?,
        effort: Float?
    ):Result<Unit> {
        if (activity == null || profile == null) return Result.Failure("Validation error")
        return garminRepository.updateActivity(activity, profile, feel, effort)
    }
}