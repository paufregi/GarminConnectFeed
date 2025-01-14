package paufregi.connectfeed.data.repository

import kotlinx.coroutines.flow.firstOrNull
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.data.api.GarminAuth1
import paufregi.connectfeed.data.api.GarminAuth2
import paufregi.connectfeed.data.api.GarminSSO
import paufregi.connectfeed.data.api.Garth
import paufregi.connectfeed.data.api.models.OAuth1
import paufregi.connectfeed.data.api.models.OAuth2
import paufregi.connectfeed.data.api.models.OAuthConsumer
import paufregi.connectfeed.data.datastore.AuthStore
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val garth: Garth,
    private val garminSSO: GarminSSO,
    private val authDatastore: AuthStore,
    private val garminDatastore: AuthStore,
    private val makeGarminAuth1: (consumer: OAuthConsumer) -> GarminAuth1,
    private val makeGarminAuth2: (consumer: OAuthConsumer, oauth: OAuth1) -> GarminAuth2,
) {
    suspend fun getOAuth1() = authDatastore.getOAuth1().firstOrNull()

    suspend fun saveOAuth1(token: OAuth1) = authDatastore.saveOAuth1(token)

    suspend fun getOAuth2() = authDatastore.getOAuth2().firstOrNull()

    suspend fun saveOAuth2(token: OAuth2) = authDatastore.saveOAuth2(token)

    suspend fun wipeAll() = authDatastore.clear()

    suspend fun getOrFetchConsumer(): OAuthConsumer? {
        var consumer = garminDatastore.getConsumer().firstOrNull()
        if (consumer == null) {
            consumer = garth.getOAuthConsumer().body()!!
            garminDatastore.saveConsumer(consumer)
        }

        return consumer
    }

    suspend fun authorize(username: String, password: String, consumer: OAuthConsumer): Result<OAuth1> {
        val resCSRF = garminSSO.getCSRF()
        if (!resCSRF.isSuccessful) return Result.Failure("Problem with the login page")
        val csrf = resCSRF.body()!!

        val resLogin = garminSSO.login(username = username, password = password, csrf = csrf)
        if (!resLogin.isSuccessful) return Result.Failure("Couldn't login")
        val ticket = resLogin.body()!!

        val connect = makeGarminAuth1(consumer)
        val resOAuth1 = connect.getOauth1(ticket)
        if (!resOAuth1.isSuccessful) return Result.Failure("Couldn't get OAuth1 token")
        return Result.Success(resOAuth1.body()!!)
    }

    suspend fun exchange(consumer: OAuthConsumer, oauth: OAuth1): Result<OAuth2> {
        val connect = makeGarminAuth2(consumer, oauth)
        val resOAuth2 = connect.getOauth2()
        if (!resOAuth2.isSuccessful) return Result.Failure("Couldn't get OAuth2 token")
        val oAuth2 = resOAuth2.body()!!
        authDatastore.saveOAuth2(oAuth2)
        return Result.Success(oAuth2)
    }
}