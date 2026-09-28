package paufregi.connectfeed.data.repository

import android.util.Log
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.utils.andThen
import paufregi.connectfeed.core.utils.toResult
import paufregi.connectfeed.data.api.garmin.GarminAuth
import paufregi.connectfeed.data.api.garmin.GarminSSO
import paufregi.connectfeed.data.api.garmin.models.Ticket
import paufregi.connectfeed.data.api.strava.StravaAuth
import paufregi.connectfeed.data.datastore.AuthStore
import javax.inject.Inject
import paufregi.connectfeed.data.api.garmin.models.AuthToken as GarminAuthToken
import paufregi.connectfeed.data.api.strava.models.AuthToken as StravaAuthToken

class AuthRepository @Inject constructor(
    private val garminSSO: GarminSSO,
    private val garminAuth: GarminAuth,
    private val stravaAuth: StravaAuth,
    private val authStore: AuthStore,
) {
    // GARMIN
    suspend fun garminLogin(
        username: String,
        password: String
    ): Result<Ticket> =
        garminSSO.getCSRF().toResult()
            .andThen { garminSSO.login(username, password, it).toResult() }

    suspend fun exchangeGarminToken(
        ticket: Ticket,
        clientId: String
    ): Result<GarminAuthToken> =
        garminAuth.exchange(GarminAuth.buildBasicAuth(clientId), clientId, ticket.value)
            .toResult()

    suspend fun refreshGarminToken(
        refreshToken: String,
        clientId: String
    ): Result<GarminAuthToken> =
        garminAuth.refresh(GarminAuth.buildBasicAuth(clientId), clientId, refreshToken)
            .toResult().onFailure {
                Log.e("AuthRepository", "Failed to refresh Garmin token: ${it.cause}")
            }

    fun getUser() = authStore.user
    fun getGarminToken() = authStore.garminToken

    suspend fun saveUser(user: User) = authStore.saveUser(user)
    suspend fun saveGarminToken(token: GarminAuthToken) = authStore.saveGarminToken(token)


    // STRAVA
    suspend fun exchangeStravaToken(clientId: String, clientSecret: String, code: String) =
        stravaAuth.exchange(clientId, clientSecret, code).toResult()

    suspend fun refreshStravaToken(clientId: String, clientSecret: String, refreshToken: String) =
        stravaAuth.refresh(clientId, clientSecret, refreshToken).toResult()

    fun getStravaToken() = authStore.stravaToken

    suspend fun saveStravaToken(token: StravaAuthToken) = authStore.saveStravaToken(token)

    suspend fun clearStravaToken() = authStore.clearStravaToken()

    // CLEAN UP
    suspend fun clear() = authStore.clear()
}