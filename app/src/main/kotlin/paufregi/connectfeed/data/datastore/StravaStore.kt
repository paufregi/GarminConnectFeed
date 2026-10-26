package paufregi.connectfeed.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import paufregi.connectfeed.data.api.strava.models.AuthTokenDto
import paufregi.connectfeed.data.utils.Crypto

class StravaStore(val dataStore: DataStore<Preferences>) {

    companion object {
        private val AUTH_TOKEN = byteArrayPreferencesKey("authToken")
    }

    fun getToken() = dataStore.data.map {
        it[AUTH_TOKEN]?.let { data ->
            Crypto.decrypt(data).let {
                Json.decodeFromString<AuthTokenDto>(it)
            }
        }
    }

    suspend fun saveToken(authTokenDto: AuthTokenDto) {
        dataStore.edit {
            it[AUTH_TOKEN] = Crypto.encrypt(Json.encodeToString(authTokenDto))
        }
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

}