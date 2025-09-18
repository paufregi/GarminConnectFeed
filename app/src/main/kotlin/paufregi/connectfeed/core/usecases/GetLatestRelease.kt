package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Release
import paufregi.connectfeed.data.repository.GithubRepository
import javax.inject.Inject

class GetLatestRelease @Inject constructor(val repo: GithubRepository) {
    suspend operator fun invoke(): Result<Release> = repo.getLatestRelease()
}