package paufregi.connectfeed.data.repository

import paufregi.connectfeed.core.utils.toResult
import paufregi.connectfeed.data.api.strava.StravaAuth
import paufregi.connectfeed.data.api.strava.models.AuthToken
import paufregi.connectfeed.data.datastore.StravaStore

class StravaAuthRepository(
    private val stravaStore: StravaStore,
    private val stravaAuth: StravaAuth,
) {
    fun getToken() = stravaStore.getToken()

    suspend fun saveToken(authToken: AuthToken) = stravaStore.saveToken(authToken)

    suspend fun clear() = stravaStore.clear()

    suspend fun exchange(clientId: String, clientSecret: String, code: String) =
        stravaAuth.exchange(clientId, clientSecret, code).toResult()

    suspend fun refresh(clientId: String, clientSecret: String, refreshToken: String) =
        stravaAuth.refreshAccessToken(clientId, clientSecret, refreshToken).toResult()
}