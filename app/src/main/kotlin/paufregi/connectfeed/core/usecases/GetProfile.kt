package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.data.repository.AppRepository
import javax.inject.Inject

class GetProfile @Inject constructor(private val repo: AppRepository) {
    suspend operator fun invoke(id: Long): Profile? = repo.getProfile(id)
}