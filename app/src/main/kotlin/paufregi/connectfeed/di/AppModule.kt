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
import paufregi.connectfeed.data.api.GarminConnect
import paufregi.connectfeed.data.api.GarminAuth1
import paufregi.connectfeed.data.api.GarminAuth2
import paufregi.connectfeed.data.api.GarminSSO
import paufregi.connectfeed.data.api.Garth
import paufregi.connectfeed.data.api.models.OAuth1
import paufregi.connectfeed.data.api.models.OAuthConsumer
import paufregi.connectfeed.data.api.utils.AuthInterceptor
import paufregi.connectfeed.data.database.GarminDao
import paufregi.connectfeed.data.datastore.AuthStore
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

val Context.authStore: DataStore<Preferences> by preferencesDataStore(name = "authStore")

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAuthDataStore(
        @ApplicationContext context: Context,
    ): AuthStore =
        AuthStore(dataStore = context.authStore)

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
    fun provideGarminRepository(
        dao: GarminDao,
        connect: GarminConnect,
    ): GarminRepository = GarminRepository(
        dao,
        connect,
    )

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
    @Named("tempFolder")
    fun provideTempFolder(@ApplicationContext context: Context): File =
        context.cacheDir
}