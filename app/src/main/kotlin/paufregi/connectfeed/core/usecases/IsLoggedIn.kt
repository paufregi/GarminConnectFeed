package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class IsLoggedIn @Inject constructor (private val garminRepository: GarminRepository) {
    operator fun invoke(): Flow<Boolean> =
        combine(
            garminRepository.getUser(),
            garminRepository.getCredential()
        ) { user, credential ->
            user != null && credential != null && credential.username.isNotBlank() && credential.password.isNotBlank()
        }
}