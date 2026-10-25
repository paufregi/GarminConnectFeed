package paufregi.connectfeed.di

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import paufregi.connectfeed.connectPort
import paufregi.connectfeed.garminAuthPort
import paufregi.connectfeed.garminSSOPort
import paufregi.connectfeed.githubPort
import paufregi.connectfeed.stravaPort
import java.io.File
import java.util.UUID
import javax.inject.Named
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ConfigModule::class]
)
object TestConfigModule {

    @Provides
    @Singleton
    @Named("GarminClientId")
    fun provideGarminClientId(): String = "GARMIN_CLIENT_ID"

    @Provides
    @Singleton
    @Named("GarminSSOUrl")
    fun provideGarminSSOUrl(): String = "https://localhost:${garminSSOPort}/"

    @Provides
    @Singleton
    @Named("GarminAuthUrl")
    fun provideGarminAuthUrl(): String = "https://localhost:${garminAuthPort}/"

    @Provides
    @Singleton
    @Named("GarminConnectUrl")
    fun provideGarminConnectUrl(): String = "https://localhost:${connectPort}/"

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
    fun provideStravaAuthUri(): Uri = "paufregi.connectfeed://strava/auth".toUri()
        .buildUpon()
        .appendQueryParameter("code", "123456")
        .build()

    @Provides
    @Singleton
    @Named("GithubUrl")
    fun provideGithubUrl(): String = "https://localhost:${githubPort}/"

    @Provides
    @Singleton
    @Named("MasterKey")
    fun provideMasterKey(): String = "test_connect_feed_key_${UUID.randomUUID()}"

    @Provides
    @Singleton
    @Named("DataStoreFileName")
    fun provideDataStoreFileName(): String = "connect_feed_${UUID.randomUUID()}.pb"

    @Provides
    @Singleton
    @Named("DataStoreDirectory")
    fun provideDataStoreDirectory(@ApplicationContext context: Context): File =
        File(context.filesDir, "test/datastore").also { it.mkdirs() }
}

