package paufregi.connectfeed.di

import android.net.Uri
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import paufregi.connectfeed.stravaPort
import javax.inject.Named
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [StravaModule::class]
)
class TestStravaModule {

    @Provides
    @Singleton
    @Named("StravaClientId")
    fun provideStravaClientId(): String = "123456"

    @Provides
    @Singleton
    @Named("StravaClientSecret")
    fun provideStravaClientSecret(): String = "abcdefghijklmopqrstuvwxyz1234567890"

    @Provides
    @Singleton
    @Named("StravaAuthUrl")
    fun provideStravaAuthUrl(): String = "https://localhost:${stravaPort}/"

    @Provides
    @Singleton
    @Named("StravaUrl")
    fun provideStravaUrl(): String = "https://localhost:${stravaPort}/"

    @Provides
    @Singleton
    @Named("StravaAuthUri")
    fun provideStravaUri(
        @Named("StravaClientId") clientId: String,
    ): Uri = Uri.parse("https://localhost:${stravaPort}/oauth/mobile/authorize")
        .buildUpon()
        .appendQueryParameter("client_id", clientId)
        .appendQueryParameter("redirect_uri", "paufregi.connectfeed://strava/auth") // Replace with your app's redirect URI
        .appendQueryParameter("response_type", "code")
        .appendQueryParameter("approval_prompt", "auto")
        .appendQueryParameter("scope", "activity:write,read")
        .build()
}