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
import paufregi.connectfeed.data.api.garmin.GarminConnect
import paufregi.connectfeed.data.api.garmin.GarminAuth1
import paufregi.connectfeed.data.api.garmin.GarminAuth2
import paufregi.connectfeed.data.api.garmin.GarminSSO
import paufregi.connectfeed.data.api.garmin.Garth
import paufregi.connectfeed.data.api.garmin.models.OAuth1
import paufregi.connectfeed.data.api.garmin.models.OAuthConsumer
import paufregi.connectfeed.data.api.garmin.interceptors.AuthInterceptor
import paufregi.connectfeed.data.api.strava.Strava
import paufregi.connectfeed.data.api.strava.StravaAuth
import paufregi.connectfeed.data.api.strava.interceptors.StravaAuthInterceptor
import paufregi.connectfeed.data.database.GarminDao
import paufregi.connectfeed.data.datastore.AuthStore
import paufregi.connectfeed.data.datastore.StravaStore
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.data.repository.StravaAuthRepository
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

val Context.authStore: DataStore<Preferences> by preferencesDataStore(name = "authStore")
val Context.stravaStore: DataStore<Preferences> by preferencesDataStore(name = "stravaStore")

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

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
        garth: Garth,
        garminSSO: GarminSSO,
        authDatastore: AuthStore,
        @Named("GarminConnectOAuth1Url") garminConnectOAuth1Url: String,
        @Named("GarminConnectOAuth2Url") garminConnectOAuth2Url: String,
    ): AuthRepository = AuthRepository(
        garth,
        garminSSO,
        authDatastore,
        makeGarminAuth1 = { oauthConsumer: OAuthConsumer -> GarminAuth1.client(oauthConsumer, garminConnectOAuth1Url) },
        makeGarminAuth2 = { oauthConsumer: OAuthConsumer, oauth: OAuth1 -> GarminAuth2.client(oauthConsumer, oauth, garminConnectOAuth2Url) }
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
    fun provideGarth(
        @Named("GarthUrl") url: String
    ): Garth = Garth.client(url)

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
    @Named("tempFolder")
    fun provideTempFolder(@ApplicationContext context: Context): File =
        context.cacheDir
}