package paufregi.connectfeed.data.repository

import paufregi.connectfeed.data.api.strava.StravaAuth
import paufregi.connectfeed.data.api.strava.models.Token
import paufregi.connectfeed.data.api.utils.callApi
import paufregi.connectfeed.data.datastore.StravaStore

class StravaAuthRepository(
    private val stravaStore: StravaStore,
    private val stravaAuth: StravaAuth,
) {
    fun getToken() = stravaStore.getToken()

    suspend fun saveToken(token: Token) = stravaStore.saveToken(token)

    suspend fun clear() = stravaStore.clear()

    suspend fun exchange(clientId: String, clientSecret: String, code: String) =
        callApi(
            { stravaAuth.exchange(clientId, clientSecret, code) },
            { res -> res.body()!! }
        )

    suspend fun refreshAccessToken(clientId: String, clientSecret: String, refreshToken: String) =
        callApi(
            { stravaAuth.refreshAccessToken(clientId, clientSecret, refreshToken) },
            { res -> res.body()!! }
        )
}