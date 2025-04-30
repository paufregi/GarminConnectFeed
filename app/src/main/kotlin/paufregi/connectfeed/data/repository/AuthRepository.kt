package paufregi.connectfeed.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.utils.mapFailure
import paufregi.connectfeed.core.utils.toResult
import paufregi.connectfeed.data.api.garmin.GarminAuth1
import paufregi.connectfeed.data.api.garmin.GarminAuth2
import paufregi.connectfeed.data.api.garmin.GarminSSO
import paufregi.connectfeed.data.api.garmin.Garth
import paufregi.connectfeed.data.api.garmin.models.OAuth1
import paufregi.connectfeed.data.api.garmin.models.OAuth2
import paufregi.connectfeed.data.api.garmin.models.OAuthConsumer
import paufregi.connectfeed.data.datastore.AuthStore
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val garth: Garth,
    private val garminSSO: GarminSSO,
    private val authStore: AuthStore,
    private val makeGarminAuth1: (consumer: OAuthConsumer) -> GarminAuth1,
    private val makeGarminAuth2: (consumer: OAuthConsumer, oauth: OAuth1) -> GarminAuth2,
) {
    fun getOAuth1() = authStore.getOAuth1()

    suspend fun saveOAuth1(token: OAuth1) = authStore.saveOAuth1(token)

    fun getOAuth2() = authStore.getOAuth2()

    suspend fun saveOAuth2(token: OAuth2) = authStore.saveOAuth2(token)

    fun getUser(): Flow<User?> =
        authStore.getUser()

    suspend fun saveUser(user: User) =
        authStore.saveUser(user)

    suspend fun clear() = authStore.clear()

    suspend fun getOrFetchConsumer(): OAuthConsumer? {
        var consumer = authStore.getConsumer().firstOrNull()
        if (consumer != null) return consumer
        return garth.getOAuthConsumer()
            .toResult()
            .onSuccess { authStore.saveConsumer(it) }
            .getOrNull()
    }

    suspend fun authorize(
        username: String,
        password: String,
        consumer: OAuthConsumer
    ): Result<OAuth1> {
        val csrf = garminSSO.getCSRF().toResult().getOrNull()
        if (csrf == null) return Result.failure(Exception("Problem with the login page"))

        val ticket = garminSSO.login(username = username, password = password, csrf = csrf)
            .toResult()
            .getOrNull()
        if (ticket == null) return Result.failure(Exception("Couldn't login"))

        val connect = makeGarminAuth1(consumer)
        return connect.getOauth1(ticket)
            .toResult()
            .mapFailure { Exception("Couldn't get OAuth1 token") }
    }

    suspend fun exchange(consumer: OAuthConsumer, oauth: OAuth1): Result<OAuth2> {
        val connect = makeGarminAuth2(consumer, oauth)
        return connect.getOauth2()
            .toResult()
            .onSuccess { authStore.saveOAuth2(it) }
            .mapFailure { Exception("Couldn't get OAuth2 token") }
    }
}