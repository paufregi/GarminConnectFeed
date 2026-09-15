package paufregi.connectfeed.data.datastore

import paufregi.connectfeed.data.api.garmin.models.AuthToken as GarminAuthToken
import paufregi.connectfeed.data.api.strava.models.AuthToken as StravaAuthToken
import androidx.datastore.core.DataStore
import jakarta.inject.Inject
import kotlinx.coroutines.flow.map
import paufregi.connectfeed.data.datastore.models.Auth

class AuthStore @Inject constructor(private val store: DataStore<Auth>) {

    val garminToken = store.data.map { it.garminToken }
    val stravaToken = store.data.map { it.stravaToken }

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


    suspend fun clear() {
        store.updateData { Auth() }
    }
}