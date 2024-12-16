package paufregi.connectfeed.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import paufregi.connectfeed.core.models.Credential
import paufregi.connectfeed.data.api.models.OAuth1
import paufregi.connectfeed.data.api.models.OAuth2
import paufregi.connectfeed.data.api.models.OAuthConsumer
import paufregi.connectfeed.data.keystore.CryptoManager

class UserDataStore (
    val dataStore: DataStore<Preferences>,
    val crypto: CryptoManager
) {

    companion object {
        private val SETUP = booleanPreferencesKey("setup")
        private val CREDENTIAL = byteArrayPreferencesKey("credential")
        private val OAUTH_CONSUMER = byteArrayPreferencesKey("oauthConsumer")
        private val OAUTH1 = byteArrayPreferencesKey("oauth1")
        private val OAUTH2 = byteArrayPreferencesKey("oauth2")
    }

    fun getSetup(): Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[SETUP] == true
        }

    fun getCredential(): Flow<Credential?> =
        dataStore.data.map { preferences ->
            val credentialJson = preferences[CREDENTIAL]
            credentialJson?.let { Json.decodeFromString<Credential>(crypto.decrypt(it)) }
        }

    fun getOAuthConsumer(): Flow<OAuthConsumer?> =
        dataStore.data.map { preferences ->
            val consumerJson = preferences[OAUTH_CONSUMER]
            consumerJson?.let { Json.decodeFromString<OAuthConsumer>(crypto.decrypt(it)) }
        }

    fun getOauth1(): Flow<OAuth1?> =
        dataStore.data.map { preferences ->
            val oauthJson = preferences[OAUTH1]
            oauthJson?.let { Json.decodeFromString<OAuth1>(crypto.decrypt(it)) }
        }

    fun getOauth2(): Flow<OAuth2?> =
        dataStore.data.map { preferences ->
            val oauth2Json = preferences[OAUTH2]
            oauth2Json?.let { Json.decodeFromString<OAuth2>(crypto.decrypt(it)) }
        }

    suspend fun saveSetup(setup: Boolean) {
        dataStore.edit { preferences ->
            preferences[SETUP] = setup
        }
    }

    suspend fun saveCredential(credential: Credential) {
        dataStore.edit { preferences ->
            preferences[CREDENTIAL] = crypto.encrypt(Json.encodeToString(credential))
        }
    }

    suspend fun saveOAuthConsumer(consumer: OAuthConsumer) {
        dataStore.edit { preferences ->
            preferences[OAUTH_CONSUMER] = crypto.encrypt(
                Json.encodeToString(consumer)
            )
        }
    }

    suspend fun saveOAuth1(oauth1: OAuth1) {
        dataStore.edit { preferences ->
            preferences[OAUTH1] = crypto.encrypt(
                Json.encodeToString(oauth1)
            )
        }
    }

    suspend fun saveOAuth2(oauth2: OAuth2) {
        dataStore.edit { preferences ->
            preferences[OAUTH2] = crypto.encrypt(
                Json.encodeToString(oauth2)
            )
        }
    }

    suspend fun deleteCredential() {
        dataStore.edit { preferences ->
            preferences.remove(CREDENTIAL)
        }
    }

    suspend fun deleteOAuthConsumer() {
        dataStore.edit { preferences ->
            preferences.remove(OAUTH_CONSUMER)
        }
    }

    suspend fun deleteOAuth1() {
        dataStore.edit { preferences ->
            preferences.remove(OAUTH1)
        }
    }

    suspend fun deleteOAuth2() {
        dataStore.edit { preferences ->
            preferences.remove(OAUTH2)
        }
    }
}