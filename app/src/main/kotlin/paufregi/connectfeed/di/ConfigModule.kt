package paufregi.connectfeed.di

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import paufregi.connectfeed.BuildConfig
import paufregi.connectfeed.data.api.garmin.GarminAuth
import paufregi.connectfeed.data.api.garmin.GarminConnect
import paufregi.connectfeed.data.api.garmin.GarminSSO
import paufregi.connectfeed.data.api.github.Github
import paufregi.connectfeed.data.api.strava.Strava
import paufregi.connectfeed.data.api.strava.StravaAuth
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

// Environment-specific values only. Tests replace this module as a whole, so keep
// object construction (clients, stores) out of here.
@Module
@InstallIn(SingletonComponent::class)
object ConfigModule {

    @Provides
    @Singleton
    @Named("GarminClientId")
    fun provideGarminClientId(): String = BuildConfig.GARMIN_CLIENT_ID

    @Provides
    @Singleton
    @Named("GarminSSOUrl")
    fun provideGarminSSOUrl(): String = GarminSSO.BASE_URL

    @Provides
    @Singleton
    @Named("GarminAuthUrl")
    fun provideGarminAuthUrl(): String = GarminAuth.BASE_URL

    @Provides
    @Singleton
    @Named("GarminConnectUrl")
    fun provideGarminConnectUrl(): String = GarminConnect.BASE_URL

    @Provides
    @Singleton
    @Named("StravaClientId")
    fun provideStravaClientId(): String = BuildConfig.STRAVA_CLIENT_ID

    @Provides
    @Singleton
    @Named("StravaClientSecret")
    fun provideStravaClientSecret(): String = BuildConfig.STRAVA_CLIENT_SECRET

    @Provides
    @Singleton
    @Named("StravaAuthUrl")
    fun provideStravaAuthUrl(): String = StravaAuth.BASE_URL

    @Provides
    @Singleton
    @Named("StravaUrl")
    fun provideStravaUrl(): String = Strava.BASE_URL

    @Provides
    @Singleton
    @Named("StravaAuthUri")
    fun provideStravaAuthUri(
        @Named("StravaClientId") clientId: String,
    ): Uri = "https://www.strava.com/oauth/mobile/authorize".toUri()
        .buildUpon()
        .appendQueryParameter("client_id", clientId)
        .appendQueryParameter("redirect_uri", "paufregi.connectfeed://strava/auth")
        .appendQueryParameter("response_type", "code")
        .appendQueryParameter("approval_prompt", "auto")
        .appendQueryParameter("scope", "activity:read_all,activity:write,profile:read_all,profile:write")
        .build()

    @Provides
    @Singleton
    @Named("GithubUrl")
    fun provideGithubUrl(): String = Github.BASE_URL

    @Provides
    @Singleton
    @Named("MasterKey")
    fun provideMasterKey(): String = "connect_feed_key"

    @Provides
    @Singleton
    @Named("DataStoreFileName")
    fun provideDataStoreFileName(): String = "connect_feed.pb"

    @Provides
    @Singleton
    @Named("DataStoreDirectory")
    fun provideDataStoreDirectory(@ApplicationContext context: Context): File =
        File(context.filesDir, "datastore").also { it.mkdirs() }
}

