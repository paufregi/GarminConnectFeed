package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.flow.Flow
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class IsSetupDone @Inject constructor (private val garminRepository: GarminRepository) {
    operator fun invoke(): Flow<Boolean> = garminRepository.getSetup()
}