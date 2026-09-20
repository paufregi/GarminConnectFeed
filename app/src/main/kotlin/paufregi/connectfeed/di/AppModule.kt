package paufregi.connectfeed.di

import android.app.DownloadManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
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
import paufregi.connectfeed.data.api.garmin.GarminAuth
import paufregi.connectfeed.data.api.garmin.GarminConnect
import paufregi.connectfeed.data.api.garmin.GarminSSO
import paufregi.connectfeed.data.api.github.Github
import paufregi.connectfeed.data.api.strava.Strava
import paufregi.connectfeed.data.api.strava.StravaAuth
import paufregi.connectfeed.data.datastore.AuthStore
import paufregi.connectfeed.data.datastore.models.Auth
import paufregi.connectfeed.data.datastore.serializers.AuthSerializer
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.data.repository.GithubRepository
import paufregi.connectfeed.data.repository.StravaRepository
import paufregi.connectfeed.data.utils.SecurityManager
import paufregi.connectfeed.system.Downloader
import java.io.File
import javax.inject.Named
import javax.inject.Singleton
import paufregi.connectfeed.data.api.garmin.interceptors.AuthInterceptor as GarminAuthInterceptor
import paufregi.connectfeed.data.api.strava.interceptors.AuthInterceptor as StravaAuthInterceptor

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context,
        @Named("MasterKey") masterKey: String,
        @Named("DataStoreFileName") dataStoreFileName: String
    ): DataStore<Auth> {
        val aead = SecurityManager.getAead(context, masterKey)

        val encryptedSerializer = AeadSerializer(
            aead = aead,
            wrappedSerializer = AuthSerializer,
            associatedData = dataStoreFileName.toByteArray(Charsets.UTF_8)
        )

        return DataStoreFactory.create(
            serializer = encryptedSerializer,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { File(context.filesDir, "datastore/$dataStoreFileName") }
        )
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        garminSSO: GarminSSO,
        garminAuth: GarminAuth,
        stravaAuth: StravaAuth,
        authStore: AuthStore,
    ): AuthRepository = AuthRepository(garminSSO, garminAuth, stravaAuth, authStore)

    @Provides
    @Singleton
    fun provideGarminRepository(garmin: GarminConnect): GarminRepository =
        GarminRepository(garmin)

    @Provides
    @Singleton
    fun provideStravaRepository(strava: Strava): StravaRepository =
        StravaRepository(strava)

    @Provides
    @Singleton
    fun provideGithubRepository(github: Github): GithubRepository =
        GithubRepository(github)

    @Provides
    @Singleton
    fun provideGarminAuthInterceptor(
        authRepository: AuthRepository,
        @Named("GarminClientId") clientId: String
    ): GarminAuthInterceptor = GarminAuthInterceptor(authRepository, clientId)

    @Provides
    @Singleton
    fun provideStravaAuthInterceptor(
        authRepository: AuthRepository,
        @Named("StravaClientId") clientId: String,
        @Named("StravaClientSecret") clientSecret: String,
    ): StravaAuthInterceptor = StravaAuthInterceptor(authRepository, clientId, clientSecret)

    @Provides
    @Singleton
    fun provideGarminSSO(
        @Named("GarminSSOUrl") url: String
    ): GarminSSO = GarminSSO.client(url)

    @Provides
    @Singleton
    fun provideStravaAuth(
        @Named("StravaAuthUrl") url: String
    ): StravaAuth = StravaAuth.client(url)


    @Provides
    @Singleton
    fun provideStrava(
        authInterceptor: paufregi.connectfeed.data.api.strava.interceptors.AuthInterceptor,
        @Named("StravaUrl") url: String,
    ): Strava = Strava.client(authInterceptor, url)

    @Provides
    @Singleton
    fun provideGithub(
        @Named("GithubUrl") url: String,
    ): Github = Github.client(url)

    @Provides
    @Singleton
    @Named("tempFolder")
    fun provideTempFolder(@ApplicationContext context: Context): File =
        context.cacheDir

    @Provides
    @Singleton
    @Named("currentVersion")
    fun provideCurrentVersion(): String =
        BuildConfig.VERSION_NAME

    @Provides
    @Singleton
    @Named("downloader")
    fun provideDownloader(@ApplicationContext context: Context): Downloader =
        Downloader(context, context.getSystemService(DownloadManager::class.java))

}