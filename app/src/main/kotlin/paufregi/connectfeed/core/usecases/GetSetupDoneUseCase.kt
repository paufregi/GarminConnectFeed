package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class GetSetupDoneUseCase @Inject constructor (private val garminRepository: GarminRepository) {
    operator fun invoke(): Flow<Boolean> {
        return garminRepository.getCredential().map { cred ->
            cred != null && cred.username.isNotBlank() && cred.password.isNotBlank()
        }
    }
}