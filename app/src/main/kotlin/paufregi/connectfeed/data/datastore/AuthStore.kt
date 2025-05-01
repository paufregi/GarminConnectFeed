package paufregi.connectfeed.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.api.garmin.models.AuthToken
import paufregi.connectfeed.data.api.garmin.models.Consumer
import paufregi.connectfeed.data.api.garmin.models.PreAuthToken
import paufregi.connectfeed.data.utils.Crypto

class AuthStore(val dataStore: DataStore<Preferences>) {

    companion object {
        private val CONSUMER_KEY = stringPreferencesKey("consumerKey")
        private val CONSUMER_SECRET = stringPreferencesKey("consumerSecret")
        private val PRE_AUTH_TOKEN = byteArrayPreferencesKey("preAuthToken")
        private val PRE_AUTH_SECRET = byteArrayPreferencesKey("preAuthSecret")
        private val AUTH_TOKEN_ACCESS_TOKEN = byteArrayPreferencesKey("authTokenAccessToken")
        private val AUTH_TOKEN_EXPIRES_AT = byteArrayPreferencesKey("authTokenExpiresAt")
        private val USER_NAME = stringPreferencesKey("userName")
        private val USER_PROFILE_IMAGE_URL = stringPreferencesKey("userProfileImageUrl")
    }

    fun getConsumer() = dataStore.data.map {
        it[CONSUMER_KEY]?.let { key ->
            it[CONSUMER_SECRET]?.let { secret ->
                Consumer(key = key, secret = secret)
            }
        }
    }

    suspend fun saveConsumer(consumer: Consumer) {
        dataStore.edit { preferences ->
            preferences[CONSUMER_KEY] = consumer.key
            preferences[CONSUMER_SECRET] = consumer.secret
        }
    }

    fun getPreAuthToken() = dataStore.data.map {
        it[PRE_AUTH_TOKEN]?.let { token ->
            it[PRE_AUTH_SECRET]?.let { secret ->
                PreAuthToken(
                    token = Crypto.decrypt(token),
                    secret = Crypto.decrypt(secret)
                )
            }
        }
    }

    suspend fun savePreAuthToken(token: PreAuthToken) {
        dataStore.edit { preferences ->
            preferences[PRE_AUTH_TOKEN] = Crypto.encrypt(token.token)
            preferences[PRE_AUTH_SECRET] = Crypto.encrypt(token.secret)
        }
    }

    fun getAuthToken() = dataStore.data.map {
        it[AUTH_TOKEN_ACCESS_TOKEN]?.let { accessToken ->
            it[AUTH_TOKEN_EXPIRES_AT]?.let { expiresAt ->
                AuthToken(
                    accessToken = Crypto.decrypt(accessToken),
                    expiresAt = Crypto.decrypt(expiresAt).toLong()
                )
            }
        }
    }

    suspend fun saveAuthToken(token: AuthToken) {
        dataStore.edit { preferences ->
            preferences[AUTH_TOKEN_ACCESS_TOKEN] = Crypto.encrypt(token.accessToken)
            preferences[AUTH_TOKEN_EXPIRES_AT] = Crypto.encrypt(token.expiresAt.toString())
        }
    }

    fun getUser() = dataStore.data.map {
        it[USER_NAME]?.let { name ->
            it[USER_PROFILE_IMAGE_URL]?.let { profileImageUrl ->
                User(name = name, profileImageUrl = profileImageUrl)
            }
        }
    }

    suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = user.name
            preferences[USER_PROFILE_IMAGE_URL] = user.profileImageUrl
        }
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

}