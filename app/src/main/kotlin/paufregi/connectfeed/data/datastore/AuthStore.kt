package paufregi.connectfeed.data.datastore

import androidx.datastore.core.DataStore
import jakarta.inject.Inject
import kotlinx.coroutines.flow.map
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.datastore.models.Auth
import paufregi.connectfeed.data.api.garmin.models.AuthToken as GarminAuthToken
import paufregi.connectfeed.data.api.strava.models.AuthToken as StravaAuthToken

class AuthStore @Inject constructor(private val store: DataStore<Auth>) {

    val user = store.data.map { it.user }
    val garminToken = store.data.map { it.garminToken }
    val stravaToken = store.data.map { it.stravaToken }

    suspend fun saveUser(user: User) {
        store.updateData {
            it.copy(user = user)
        }
    }

    suspend fun saveGarminToken(token: GarminAuthToken) {
        store.updateData {
            it.copy(garminToken = token)
        }
    }

    suspend fun saveStravaToken(token: StravaAuthToken) {
        store.updateData {
            it.copy(stravaToken = token)
        }
    }

    suspend fun clearStravaToken() {
        store.updateData {
            it.copy(stravaToken = null)
        }
    }

    suspend fun clear() {
        store.updateData { Auth() }
    }
}