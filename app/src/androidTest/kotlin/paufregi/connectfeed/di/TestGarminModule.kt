package paufregi.connectfeed.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import paufregi.connectfeed.connectPort
import paufregi.connectfeed.garminSSOPort
import javax.inject.Named
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [GarminModule::class]
)
class TestGarminModule {

    @Provides
    @Singleton
    @Named("GarminConsumerKey")
    fun provideGarminConsumerKey(): String = "00000000-0000-0000-0000-000000000000"

    @Provides
    @Singleton
    @Named("GarminConsumerSecret")
    fun provideGarminConsumerSecret(): String = "abcdefghijklmopqrstuvwxyz"

    @Provides
    @Singleton
    @Named("GarminConnectUrl")
    fun provideGarminConnectUrl(): String = "https://localhost:${connectPort}/"

    @Provides
    @Singleton
    @Named("GarminPreAuthUrl")
    fun provideGarminPreAuthUrl(): String = "https://localhost:${connectPort}/"

    @Provides
    @Singleton
    @Named("GarminAuthUrl")
    fun provideGarminAuthUrl(): String = "https://localhost:${connectPort}/"

    @Provides
    @Singleton
    @Named("GarminSSOUrl")
    fun provideGarminSSOUrl(): String = "https://localhost:${garminSSOPort}/"
}