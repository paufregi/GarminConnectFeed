package paufregi.connectfeed.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import paufregi.connectfeed.connectPort
import paufregi.connectfeed.garminAuthPort
import paufregi.connectfeed.garminSSOPort
import javax.inject.Named
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [GarminModule::class]
)
object TestGarminModule {

    @Provides
    @Singleton
    @Named("GarminClientId")
    fun provideGarminClientId(): String = "GARMIN_CLIENT_ID"

    @Provides
    @Singleton
    @Named("GarminConnectUrl")
    fun provideGarminConnectUrl(): String = "https://localhost:${connectPort}/"

    @Provides
    @Singleton
    @Named("GarminAuthUrl")
    fun provideGarminAuthUrl(): String = "https://localhost:${garminAuthPort}/"

    @Provides
    @Singleton
    @Named("GarminSSOUrl")
    fun provideGarminSSOUrl(): String = "https://localhost:${garminSSOPort}/"
}