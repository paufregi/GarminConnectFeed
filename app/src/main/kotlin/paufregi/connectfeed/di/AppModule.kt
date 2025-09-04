package paufregi.connectfeed.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import paufregi.connectfeed.data.api.garmin.GarminAuth
import paufregi.connectfeed.data.api.garmin.GarminConnect
import paufregi.connectfeed.data.api.garmin.GarminPreAuth
import paufregi.connectfeed.data.api.garmin.GarminSSO
import paufregi.connectfeed.data.api.garmin.interceptors.AuthInterceptor
import paufregi.connectfeed.data.api.garmin.models.PreAuthToken
import paufregi.connectfeed.data.api.github.Github
import paufregi.connectfeed.data.api.strava.Strava
import paufregi.connectfeed.data.api.strava.StravaAuth
import paufregi.connectfeed.data.api.strava.interceptors.StravaAuthInterceptor
import paufregi.connectfeed.data.database.GarminDao
import paufregi.connectfeed.data.datastore.AuthStore
import paufregi.connectfeed.data.datastore.StravaStore
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.data.repository.GithubRepository
import paufregi.connectfeed.data.repository.StravaAuthRepository
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

val Context.authStore: DataStore<Preferences> by preferencesDataStore(name = "authStore")
val Context.stravaStore: DataStore<Preferences> by preferencesDataStore(name = "stravaStore")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthStore(
        @ApplicationContext context: Context,
    ): AuthStore =
        AuthStore(dataStore = context.authStore)

    @Provides
    @Singleton
    fun provideStravaStore(
        @ApplicationContext context: Context,
    ): StravaStore =
        StravaStore(dataStore = context.stravaStore)

    @Provides
    @Singleton
    fun provideAuthRepository(
        garminSSO: GarminSSO,
        authDatastore: AuthStore,
        @Named("GarminConsumerKey") consumerKey: String,
        @Named("GarminConsumerSecret") consumerSecret: String,
        @Named("GarminPreAuthUrl") garminPreAuthUrl: String,
        @Named("GarminAuthUrl") garminAuthUrl: String,
    ): AuthRepository = AuthRepository(
        garminSSO = garminSSO,
        authStore = authDatastore,
        preAuth = GarminPreAuth.client(consumerKey, consumerSecret, garminPreAuthUrl),
        makeGarminAuth = { oauth: PreAuthToken ->
            GarminAuth.client(consumerKey, consumerSecret, oauth, garminAuthUrl)
        }
    )

    @Provides
    @Singleton
    fun provideStravaAuthRepository(
        stravaStore: StravaStore,
        stravaAuth: StravaAuth,
    ): StravaAuthRepository = StravaAuthRepository(
        stravaStore,
        stravaAuth
    )

    @Provides
    @Singleton
    fun provideGarminRepository(
        dao: GarminDao,
        connect: GarminConnect,
        strava: Strava,
    ): GarminRepository = GarminRepository(dao, connect, strava)

    @Provides
    @Singleton
    fun provideGithubRepository(
        github: Github,
    ): GithubRepository = GithubRepository(github)


    @Provides
    @Singleton
    fun provideAuthInterceptor(
        authRepository: AuthRepository
    ): AuthInterceptor = AuthInterceptor(authRepository)

    @Provides
    @Singleton
    fun provideGarminConnect(
        authInterceptor: AuthInterceptor,
        @Named("GarminConnectUrl") url: String
    ): GarminConnect = GarminConnect.client(authInterceptor, url)

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
    fun provideStravaAuthInterceptor(
        authRepo: StravaAuthRepository,
        @Named("StravaClientId") clientId: String,
        @Named("StravaClientSecret") clientSecret: String,
    ): StravaAuthInterceptor = StravaAuthInterceptor(authRepo, clientId, clientSecret)

    @Provides
    @Singleton
    fun provideStrava(
        authInterceptor: StravaAuthInterceptor,
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
}