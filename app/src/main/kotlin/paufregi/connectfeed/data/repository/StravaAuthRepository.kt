package paufregi.connectfeed.data.repository

import android.util.Log
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
            {
                Log.i("StravaAuthRepository", "exchange")
                Log.i("StravaAuthRepository", "clientId: $clientId")
                Log.i("StravaAuthRepository", "clientSecret: $clientSecret")
                Log.i("StravaAuthRepository", "code: $code")
                stravaAuth.exchange(clientId, clientSecret, code) },
            { res -> res.body()!! }
        )

    suspend fun refreshAccessToken(clientId: String, clientSecret: String, refreshToken: String) =
        callApi(
            {
                Log.i("StravaAuthRepository", "refreshAccessToken")
                Log.i("StravaAuthRepository", "exchange")
                Log.i("StravaAuthRepository", "clientId: $clientId")
                Log.i("StravaAuthRepository", "clientSecret: $clientSecret")
                Log.i("StravaAuthRepository", "refreshToken: $refreshToken")
                stravaAuth.refreshAccessToken(clientId, clientSecret, refreshToken) },
            { res -> res.body()!! }
        )
}