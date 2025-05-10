package paufregi.connectfeed.data.repository

import kotlinx.coroutines.flow.Flow
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.utils.mapFailure
import paufregi.connectfeed.core.utils.toResult
import paufregi.connectfeed.data.api.garmin.GarminAuth
import paufregi.connectfeed.data.api.garmin.GarminPreAuth
import paufregi.connectfeed.data.api.garmin.GarminSSO
import paufregi.connectfeed.data.api.garmin.models.AuthToken
import paufregi.connectfeed.data.api.garmin.models.PreAuthToken
import paufregi.connectfeed.data.datastore.AuthStore
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val garminSSO: GarminSSO,
    private val authStore: AuthStore,
    private val preAuth: GarminPreAuth,
    private val makeGarminAuth: (oauth: PreAuthToken) -> GarminAuth,
) {
    fun getPreAuth() = authStore.getPreAuthToken()

    suspend fun savePreAuth(token: PreAuthToken) = authStore.savePreAuthToken(token)

    fun getAuthToken() = authStore.getAuthToken()

    suspend fun saveAuthToken(token: AuthToken) = authStore.saveAuthToken(token)

    fun getUser(): Flow<User?> =
        authStore.getUser()

    suspend fun saveUser(user: User) =
        authStore.saveUser(user)

    suspend fun clear() = authStore.clear()

    suspend fun authorize(
        username: String,
        password: String,
    ): Result<PreAuthToken> {
        val csrf = garminSSO.getCSRF().toResult().getOrNull()
        if (csrf == null) return Result.failure(Exception("Problem with the login page"))

        val ticket = garminSSO.login(username = username, password = password, csrf = csrf)
            .toResult()
            .getOrNull()
        if (ticket == null) return Result.failure(Exception("Couldn't login"))

        return preAuth.preauthorize(ticket)
            .toResult()
            .mapFailure { Exception("Couldn't get PreAuth token") }
    }

    suspend fun exchange(token: PreAuthToken): Result<AuthToken> {
        val garminAuth = makeGarminAuth(token)
        return garminAuth.exchange()
            .toResult()
            .onSuccess { authStore.saveAuthToken(it) }
            .mapFailure { Exception("Couldn't get Auth token") }
    }

    suspend fun refresh(preAuthToken: PreAuthToken, refreshToken: String): Result<AuthToken> {
        val garminAuth = makeGarminAuth(preAuthToken)
        return garminAuth.refresh(refreshToken)
            .toResult()
            .onSuccess { authStore.saveAuthToken(it) }
            .mapFailure { Exception("Couldn't refresh Auth token") }
    }
}