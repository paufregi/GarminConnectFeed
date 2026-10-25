package paufregi.connectfeed.di

import android.app.DownloadManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.tink.AeadSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import paufregi.connectfeed.BuildConfig
import paufregi.connectfeed.data.datastore.models.Auth
import paufregi.connectfeed.data.datastore.serializers.AuthSerializer
import paufregi.connectfeed.data.utils.SecurityManager
import paufregi.connectfeed.system.Downloader
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @Named("AppScope")
    fun provideAppScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Provides
    @Singleton
    @Named("currentVersion")
    fun provideCurrentVersion(): String = BuildConfig.VERSION_NAME

    @Provides
    @Singleton
    @Named("tempFolder")
    fun provideTempFolder(@ApplicationContext context: Context): File = context.cacheDir

    @Provides
    @Singleton
    fun provideDownloader(@ApplicationContext context: Context): Downloader =
        Downloader(context, context.getSystemService(DownloadManager::class.java))

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context,
        @Named("AppScope") scope: CoroutineScope,
        @Named("MasterKey") masterKey: String,
        @Named("DataStoreFileName") dataStoreFileName: String,
        @Named("DataStoreDirectory") dataStoreDirectory: File,
    ): DataStore<Auth> {
        val encryptedSerializer = AeadSerializer(
            aead = SecurityManager.getAead(context, masterKey),
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

