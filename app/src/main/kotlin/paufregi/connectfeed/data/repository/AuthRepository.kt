package paufregi.connectfeed.data.repository

import kotlinx.coroutines.flow.Flow
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.utils.andThen
import paufregi.connectfeed.core.utils.toResult
import paufregi.connectfeed.data.api.garmin.GarminAuth
import paufregi.connectfeed.data.api.garmin.GarminSSO
import paufregi.connectfeed.data.api.garmin.models.Ticket
import paufregi.connectfeed.data.api.strava.StravaAuth
import paufregi.connectfeed.data.datastore.AuthStore
import javax.inject.Inject
import paufregi.connectfeed.data.api.garmin.models.AuthTokenDto as GarminAuthToken

class AuthRepository @Inject constructor(
    private val garminSSO: GarminSSO,
    private val garminAuth: GarminAuth,
    private val stravaAuth: StravaAuth,
    private val authStore: AuthStore,
) {
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
        garminAuth.exchange(GarminAuth.buildBasicAuth(clientId), clientId, ticket.value).toResult()

    suspend fun refreshGarminToken(
        refreshToken: String,
        clientId: String
    ): Result<GarminAuthToken> =
        garminAuth.refresh(GarminAuth.buildBasicAuth(clientId), clientId, refreshToken).toResult()

    fun getUser(): Flow<User?> =
        authStore.getUser()

    suspend fun saveUser(user: User) =
        authStore.saveUser(user)

    suspend fun clear() = authStore.clear()
}