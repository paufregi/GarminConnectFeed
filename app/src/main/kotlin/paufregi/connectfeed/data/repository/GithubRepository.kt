package paufregi.connectfeed.data.repository

import paufregi.connectfeed.core.utils.toResult
import paufregi.connectfeed.data.api.github.Github

class GithubRepository(
    private val github: Github
) {
    suspend fun getLatestRelease() = github.getLatestRelease().toResult()
}