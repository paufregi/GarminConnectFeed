package paufregi.connectfeed.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.tink.AeadSerializer
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.datastore.models.Auth
import paufregi.connectfeed.data.datastore.serializers.AuthSerializer
import java.io.File
import javax.inject.Inject
import paufregi.connectfeed.data.api.garmin.models.AuthTokenDto as GarminAuthToken
import paufregi.connectfeed.data.api.strava.models.AuthTokenDto as StravaAuthToken

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

    companion object {
        fun create(
            context: Context,
            masterKey: String,
            scope: CoroutineScope,
            dataStoreFileName: String,
            dataStoreDirectory: File,
        ): DataStore<Auth> {
            AeadConfig.register()

            val keysetHandle = AndroidKeysetManager.Builder()
                .withSharedPref(context, "${masterKey}_keyset", "${masterKey}_pref")
                .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
                .withMasterKeyUri("android-keystore://$masterKey")
                .build()
                .keysetHandle

            val aead = keysetHandle.getPrimitive(
                RegistryConfiguration.get(),
                Aead::class.java
            )

            val encryptedSerializer = AeadSerializer(
                aead = aead,
                wrappedSerializer = AuthSerializer,
                associatedData = dataStoreFileName.toByteArray(Charsets.UTF_8),
            )

            return DataStoreFactory.create(
                serializer = encryptedSerializer,
                corruptionHandler = ReplaceFileCorruptionHandler { Auth() },
                scope = scope,
                produceFile = { File(dataStoreDirectory, dataStoreFileName) },
            )
        }
    }
}