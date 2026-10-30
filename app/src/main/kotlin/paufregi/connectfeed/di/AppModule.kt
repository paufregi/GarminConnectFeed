package paufregi.connectfeed.di

import android.app.DownloadManager
import android.content.Context
import androidx.datastore.core.DataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import paufregi.connectfeed.BuildConfig
import paufregi.connectfeed.data.datastore.AuthStore
import paufregi.connectfeed.data.datastore.models.Auth
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
        @Named("MasterKey") masterKey: String,
        @Named("AppScope") scope: CoroutineScope,
        @Named("DataStoreFileName") dataStoreFileName: String,
        @Named("DataStoreDirectory") dataStoreDirectory: File,
    ): DataStore<Auth> = AuthStore.create(
        context = context,
        masterKey = masterKey,
        scope = scope,
        dataStoreFileName = dataStoreFileName,
        dataStoreDirectory = dataStoreDirectory,
    )
}

