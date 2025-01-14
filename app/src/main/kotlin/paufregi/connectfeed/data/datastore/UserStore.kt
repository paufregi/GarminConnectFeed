package paufregi.connectfeed.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map
import paufregi.connectfeed.core.models.User

class UserStore (val dataStore: DataStore<Preferences>) {

    companion object {
        private val NAME = stringPreferencesKey("name")
        private val PROFILE_IMAGE_URL = stringPreferencesKey("profileImageUrl")
    }

    fun get() = dataStore.data.map {
        it[NAME]?.let { name ->
            it[PROFILE_IMAGE_URL]?.let { profileImageUrl ->
                User(name = name, profileImageUrl = profileImageUrl)
            }
        }
    }

    suspend fun save(user: User) {
        dataStore.edit {
            it[NAME] = user.name
            it[PROFILE_IMAGE_URL] = user.profileImageUrl
        }
    }

    suspend fun delete() = dataStore.edit { it.clear() }
}
