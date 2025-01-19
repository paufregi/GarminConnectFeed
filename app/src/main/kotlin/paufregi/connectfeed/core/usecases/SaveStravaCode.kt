package paufregi.connectfeed.core.usecases

import android.R
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.data.repository.StravaAuthRepository
import javax.inject.Inject

class SaveStravaCode @Inject constructor (private val stravaAuthRepository: StravaAuthRepository) {
    suspend operator fun invoke(code: String) = stravaAuthRepository.saveCode(code)
}