package paufregi.connectfeed.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.api.garmin.models.OAuth1
import paufregi.connectfeed.data.api.garmin.models.OAuth2
import paufregi.connectfeed.data.api.garmin.models.OAuthConsumer
import paufregi.connectfeed.data.utils.Crypto

class AuthStore(val dataStore: DataStore<Preferences>) {

    companion object {
        private val OAUTH_CONSUMER_KEY = stringPreferencesKey("oauthConsumerKey")
        private val OAUTH_CONSUMER_SECRET = stringPreferencesKey("oauthConsumerSecret")
        private val OAUTH1_TOKEN = byteArrayPreferencesKey("oauth1Token")
        private val OAUTH1_SECRET = byteArrayPreferencesKey("oauth1Secret")
        private val OAUTH2_TOKEN = byteArrayPreferencesKey("oauth2Token")
        private val USER_NAME = stringPreferencesKey("userName")
        private val USER_PROFILE_IMAGE_URL = stringPreferencesKey("userProfileImageUrl")
    }

    fun getConsumer() = dataStore.data.map {
        it[OAUTH_CONSUMER_KEY]?.let { key ->
            it[OAUTH_CONSUMER_SECRET]?.let { secret ->
                OAuthConsumer(key = key, secret = secret)
            }
        }
    }

    suspend fun saveConsumer(consumer: OAuthConsumer) {
        dataStore.edit { preferences ->
            preferences[OAUTH_CONSUMER_KEY] = consumer.key
            preferences[OAUTH_CONSUMER_SECRET] = consumer.secret
        }
    }

    fun getOAuth1() = dataStore.data.map {
        it[OAUTH1_TOKEN]?.let { token ->
            it[OAUTH1_SECRET]?.let { secret ->
                OAuth1(token = Crypto.decrypt(token), secret = Crypto.decrypt(secret))
            }
        }
    }

    suspend fun saveOAuth1(token: OAuth1) {
        dataStore.edit { preferences ->
            preferences[OAUTH1_TOKEN] = Crypto.encrypt(token.token)
            preferences[OAUTH1_SECRET] = Crypto.encrypt(token.secret)
        }
    }

    fun getOAuth2() = dataStore.data.map {
        it[OAUTH2_TOKEN]
            ?.let { OAuth2(Crypto.decrypt(it)) }
    }

    suspend fun saveOAuth2(token: OAuth2) {
        dataStore.edit { preferences ->
            preferences[OAUTH2_TOKEN] = Crypto.encrypt(token.accessToken)
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