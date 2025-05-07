package paufregi.connectfeed.di

import android.net.Uri
import androidx.core.net.toUri
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import paufregi.connectfeed.BuildConfig
import paufregi.connectfeed.data.api.strava.Strava
import paufregi.connectfeed.data.api.strava.StravaAuth
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StravaModule {

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
        .appendQueryParameter("scope", "activity:read_all,activity:write,profile:write")
        .build()
}