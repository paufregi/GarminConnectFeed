package paufregi.connectfeed.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import paufregi.connectfeed.data.api.strava.models.Token
import paufregi.connectfeed.data.utils.Crypto

class StravaStore(val dataStore: DataStore<Preferences>) {

    companion object {
        private val ACCESS_TOKEN = byteArrayPreferencesKey("accessToken")
        private val REFRESH_TOKEN = byteArrayPreferencesKey("refreshToken")
        private val EXPIRE_AT = byteArrayPreferencesKey("expire_at")
    }

    fun getToken() = dataStore.data.map {
        it[ACCESS_TOKEN]?.let { accessToken ->
            it[REFRESH_TOKEN]?.let { refreshToken ->
                it[EXPIRE_AT]?.let { expireAt ->
                    Token(
                        accessToken = Crypto.decrypt(accessToken),
                        refreshToken = Crypto.decrypt(refreshToken),
                        expiresAt = Crypto.decrypt(expireAt).toLong()
                    )
                }
            }
        }
    }

    suspend fun saveToken(token: Token) {
        dataStore.edit {
            it[ACCESS_TOKEN] = Crypto.encrypt(token.accessToken)
            it[REFRESH_TOKEN] = Crypto.encrypt(token.refreshToken)
            it[EXPIRE_AT] = Crypto.encrypt(token.expiresAt.toString())
        }
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

}