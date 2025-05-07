package paufregi.connectfeed.di

import android.net.Uri
import androidx.core.net.toUri
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
object TestStravaModule {

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
    fun provideStravaAuthUri(): Uri =
        Uri.parse("paufregi.connectfeed://strava/auth")
            .buildUpon()
            .appendQueryParameter("code", "123456")
            .build()
}