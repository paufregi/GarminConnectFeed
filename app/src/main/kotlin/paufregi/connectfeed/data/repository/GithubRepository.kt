package paufregi.connectfeed.data.repository

import paufregi.connectfeed.core.utils.mapOrFailure
import paufregi.connectfeed.core.utils.toResult
import paufregi.connectfeed.data.api.github.Github
import paufregi.connectfeed.data.repository.converters.toCore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubRepository @Inject constructor(
    private val github: Github
) {
    suspend fun getLatestRelease() =
        github.getLatestRelease()
            .toResult()
            .mapOrFailure { it.toCore() }
}