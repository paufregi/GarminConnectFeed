package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class DeleteProfile @Inject constructor (private val garminRepository: GarminRepository) {
    suspend operator fun invoke(profile: Profile) = garminRepository.deleteProfile(profile)
}