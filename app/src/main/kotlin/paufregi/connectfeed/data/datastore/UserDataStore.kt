package paufregi.connectfeed.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import paufregi.connectfeed.core.models.Credential
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.api.models.OAuth1
import paufregi.connectfeed.data.api.models.OAuth2
import paufregi.connectfeed.data.api.models.OAuthConsumer
import paufregi.connectfeed.data.utils.Crypto
import java.util.Base64

class UserDataStore (val dataStore: DataStore<Preferences>) {

    companion object {
        private val USER = byteArrayPreferencesKey("user")
        private val CREDENTIAL = byteArrayPreferencesKey("credential")
        private val OAUTH_CONSUMER = byteArrayPreferencesKey("oauthConsumer")
        private val OAUTH1 = byteArrayPreferencesKey("oauth1")
        private val OAUTH2 = byteArrayPreferencesKey("oauth2")
    }

    private inline fun <reified T>get(key: Preferences.Key<ByteArray>): Flow<T?> =
        dataStore.data.map {
            it[key]
                ?.let { Base64.getDecoder().decode(it) }
                ?.let { Crypto.decrypt(it) }?.decodeToString()
                ?.let { Json.decodeFromString(it) }
        }

    private suspend inline fun <reified T>store(value: T, key: Preferences.Key<ByteArray>) {
        dataStore.edit {
            it[key] = Json.encodeToString(value).toByteArray()
                .let { Crypto.encrypt(it) }
                .let { Base64.getEncoder().encode(it) }
        }
    }

    private suspend inline fun remove(key: Preferences.Key<ByteArray>) {
        dataStore.edit { it.remove(key) }
    }

    fun getUser(): Flow<User?> = get<User>(USER)

    fun getCredential(): Flow<Credential?> = get<Credential>(CREDENTIAL)

    fun getOAuthConsumer(): Flow<OAuthConsumer?> = get<OAuthConsumer>(OAUTH_CONSUMER)

    fun getOauth1(): Flow<OAuth1?> = get<OAuth1>(OAUTH1)

    fun getOauth2(): Flow<OAuth2?> = get<OAuth2>(OAUTH2)

    suspend fun saveUser(user: User) = store(user, USER)

    suspend fun saveCredential(credential: Credential) = store(credential, CREDENTIAL)

    suspend fun saveOAuthConsumer(consumer: OAuthConsumer) = store(consumer, OAUTH_CONSUMER)

    suspend fun saveOAuth1(oauth1: OAuth1) = store(oauth1, OAUTH1)

    suspend fun saveOAuth2(oauth2: OAuth2) = store(oauth2, OAUTH2)

    suspend fun deleteUser() = remove(USER)

    suspend fun deleteCredential() = remove(CREDENTIAL)

    suspend fun deleteOAuthConsumer() = remove(OAUTH_CONSUMER)

    suspend fun deleteOAuth1() = remove(OAUTH1)

    suspend fun deleteOAuth2() = remove(OAUTH2)
}
