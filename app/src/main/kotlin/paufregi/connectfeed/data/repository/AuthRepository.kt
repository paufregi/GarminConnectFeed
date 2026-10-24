package paufregi.connectfeed.data.repository

import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.utils.andThen
import paufregi.connectfeed.core.utils.apiCall
import paufregi.connectfeed.data.api.garmin.GarminAuth
import paufregi.connectfeed.data.api.garmin.GarminSSO
import paufregi.connectfeed.data.api.garmin.models.Ticket
import paufregi.connectfeed.data.api.strava.StravaAuth
import paufregi.connectfeed.data.datastore.AuthStore
import javax.inject.Inject
import javax.inject.Singleton
import paufregi.connectfeed.data.api.garmin.models.AuthTokenDto as GarminAuthToken
import paufregi.connectfeed.data.api.strava.models.AuthTokenDto as StravaAuthToken

@Singleton
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
        apiCall { garminSSO.getCSRF() }
            .andThen { apiCall { garminSSO.login(username, password, it) } }

    suspend fun exchangeGarminToken(
        ticket: Ticket,
        clientId: String
    ): Result<GarminAuthToken> =
        apiCall { garminAuth.exchange(GarminAuth.buildBasicAuth(clientId), clientId, ticket.value) }

    suspend fun refreshGarminToken(
        refreshToken: String,
        clientId: String
    ): Result<GarminAuthToken> =
        apiCall { garminAuth.refresh(GarminAuth.buildBasicAuth(clientId), clientId, refreshToken) }

    fun getUser() = authStore.user
    fun getGarminToken() = authStore.garminToken

    suspend fun saveUser(user: User) = authStore.saveUser(user)
    suspend fun saveGarminToken(token: GarminAuthToken) = authStore.saveGarminToken(token)


    // STRAVA
    suspend fun exchangeStravaToken(clientId: String, clientSecret: String, code: String) =
        apiCall { stravaAuth.exchange(clientId, clientSecret, code) }

    suspend fun refreshStravaToken(clientId: String, clientSecret: String, refreshToken: String) =
        apiCall { stravaAuth.refresh(clientId, clientSecret, refreshToken) }

    fun getStravaToken() = authStore.stravaToken

    suspend fun saveStravaToken(token: StravaAuthToken) = authStore.saveStravaToken(token)

    suspend fun clearStravaToken() = authStore.clearStravaToken()

    // CLEAN UP
    suspend fun clear() = authStore.clear()
}