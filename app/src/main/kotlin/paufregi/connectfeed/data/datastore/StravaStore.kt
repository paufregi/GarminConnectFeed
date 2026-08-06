package paufregi.connectfeed.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import paufregi.connectfeed.data.api.strava.models.AuthToken
import paufregi.connectfeed.data.utils.Crypto

class StravaStore(val dataStore: DataStore<Preferences>) {

    companion object {
        private val AUTH_TOKEN = byteArrayPreferencesKey("authToken")
        private val ATHLETE_ID = longPreferencesKey("athleteId")
    }

    fun getToken() = dataStore.data.map {
        it[AUTH_TOKEN]?.let { data ->
            Crypto.decrypt(data).let {
                Json.decodeFromString<AuthToken>(it)
            }
        }
    }

    suspend fun saveToken(authToken: AuthToken) {
        dataStore.edit {
            it[AUTH_TOKEN] = Crypto.encrypt(Json.encodeToString(authToken))
        }
    }

    fun getAthleteId() = dataStore.data.map { it[ATHLETE_ID] }

    suspend fun saveAthleteId(athleteId: Long) {
        dataStore.edit {
            it[ATHLETE_ID] = athleteId
        }
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

}