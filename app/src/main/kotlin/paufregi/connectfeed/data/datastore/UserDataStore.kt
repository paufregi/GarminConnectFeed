package paufregi.connectfeed.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import paufregi.connectfeed.core.models.Credential
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.api.models.OAuth1
import paufregi.connectfeed.data.api.models.OAuth2
import paufregi.connectfeed.data.api.models.OAuthConsumer
import paufregi.connectfeed.data.keystore.CryptoManager

class UserDataStore (
    val dataStore: DataStore<Preferences>,
    val crypto: CryptoManager
) {
    companion object {
        private val USER = byteArrayPreferencesKey("user")
        private val CREDENTIAL = byteArrayPreferencesKey("credential")
        private val OAUTH_CONSUMER = byteArrayPreferencesKey("oauthConsumer")
        private val OAUTH1 = byteArrayPreferencesKey("oauth1")
        private val OAUTH2 = byteArrayPreferencesKey("oauth2")
    }
    private val mutex = Mutex()

    private suspend fun <T> withLock(block: suspend () -> T): T = mutex.withLock { block() }

    private suspend inline fun <reified T>getValue(key: Preferences.Key<ByteArray>): Flow<T?> =
        withLock {
            dataStore.data.conflate().map { preferences ->
                preferences[key]?.let { crypto.decrypt(it) }?.let { Json.decodeFromString<T>(it) }
            }
        }

    private suspend inline fun <reified T>storeValue(value: T, key: Preferences.Key<ByteArray>) {
        withLock {
            dataStore.edit { preferences ->
                preferences[key] = crypto.encrypt(Json.encodeToString(value))
            }
        }
    }

    private suspend inline fun removeValue(key: Preferences.Key<ByteArray>) {
        withLock {
            dataStore.edit { preferences -> preferences.remove(key) }
        }
    }

    suspend fun getUser(): Flow<User?> = getValue<User>(USER)

    suspend fun getCredential(): Flow<Credential?> = getValue<Credential>(CREDENTIAL)

    suspend fun getOAuthConsumer(): Flow<OAuthConsumer?> = getValue<OAuthConsumer>(OAUTH_CONSUMER)

    suspend fun getOauth1(): Flow<OAuth1?> = getValue<OAuth1>(OAUTH1)

    suspend fun getOauth2(): Flow<OAuth2?> = getValue<OAuth2>(OAUTH2)

    suspend fun saveUser(user: User) = storeValue(user, USER)

    suspend fun saveCredential(credential: Credential) = storeValue(credential, CREDENTIAL)

    suspend fun saveOAuthConsumer(consumer: OAuthConsumer) = storeValue(consumer, OAUTH_CONSUMER)

    suspend fun saveOAuth1(oauth1: OAuth1) = storeValue(oauth1, OAUTH1)

    suspend fun saveOAuth2(oauth2: OAuth2) = storeValue(oauth2, OAUTH2)

    suspend fun deleteUser() = removeValue(USER)

    suspend fun deleteCredential() = removeValue(CREDENTIAL)

    suspend fun deleteOAuthConsumer() = removeValue(OAUTH_CONSUMER)

    suspend fun deleteOAuth1() = removeValue(OAUTH1)

    suspend fun deleteOAuth2() = removeValue(OAUTH2)
}
