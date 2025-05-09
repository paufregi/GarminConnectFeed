package paufregi.connectfeed.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.api.garmin.models.AuthToken
import paufregi.connectfeed.data.api.garmin.models.PreAuthToken
import paufregi.connectfeed.data.utils.Crypto

class AuthStore(val dataStore: DataStore<Preferences>) {

    companion object {
        private val PRE_AUTH_TOKEN = byteArrayPreferencesKey("preAuthToken")
        private val AUTH_TOKEN = byteArrayPreferencesKey("authToken")
        private val USER = stringPreferencesKey("user")
    }

    fun getPreAuthToken() = dataStore.data.map {
        it[PRE_AUTH_TOKEN]?.let { tokenBytes ->
            Crypto.decrypt(tokenBytes).let {
                Json.decodeFromString<PreAuthToken>(it)
            }
        }
    }

    suspend fun savePreAuthToken(token: PreAuthToken) {
        dataStore.edit { preferences ->
            preferences[PRE_AUTH_TOKEN] = Crypto.encrypt(Json.encodeToString(token))
        }
    }

    fun getAuthToken() = dataStore.data.map {
        it[AUTH_TOKEN]?.let { tokenBytes  ->
            Crypto.decrypt(tokenBytes).let {
                Json.decodeFromString<AuthToken>(it)
            }
        }
    }

    suspend fun saveAuthToken(token: AuthToken) {
        dataStore.edit { preferences ->
            preferences[AUTH_TOKEN] = Crypto.encrypt(Json.encodeToString(token))
        }
    }

    fun getUser() = dataStore.data.map {
        it[USER]?.let { data ->
            Json.decodeFromString<User>(data)
        }
    }

    suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            preferences[USER] = Json.encodeToString(user)
        }
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }
}