package paufregi.connectfeed.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.tink.AeadSerializer
import com.google.crypto.tink.Aead
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import paufregi.connectfeed.data.datastore.models.Auth
import paufregi.connectfeed.data.datastore.serializers.AuthSerializer
import paufregi.connectfeed.data.utils.SecurityManager
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EncryptedDataStoreModule {

    private const val DATASTORE_FILE_NAME = "connect_feed.pb"
    private const val MASTER_KEY_ALIAS = "connect_feed_key"

    @Provides
    @Singleton
    fun provideAead(@ApplicationContext context: Context): Aead {
        return SecurityManager.getAead(context, MASTER_KEY_ALIAS)
    }

    @Provides
    @Singleton
    fun provideEncryptedDataStore(
        @ApplicationContext context: Context,
        aead: Aead,
    ): DataStore<Auth> {

        val encryptedSerializer = AeadSerializer(
            aead = aead,
            wrappedSerializer = AuthSerializer,
            associatedData = DATASTORE_FILE_NAME.toByteArray(Charsets.UTF_8)
        )

        return DataStoreFactory.create(
            serializer = encryptedSerializer,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { File(context.filesDir, "datastore/$DATASTORE_FILE_NAME") }
        )
    }
}