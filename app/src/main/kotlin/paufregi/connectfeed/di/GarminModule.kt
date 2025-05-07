package paufregi.connectfeed.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import paufregi.connectfeed.BuildConfig
import paufregi.connectfeed.data.api.garmin.GarminAuth
import paufregi.connectfeed.data.api.garmin.GarminConnect
import paufregi.connectfeed.data.api.garmin.GarminPreAuth
import paufregi.connectfeed.data.api.garmin.GarminSSO
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GarminModule {

    @Provides
    @Singleton
    @Named("GarminConsumerKey")
    fun provideGarminConsumerKey(): String = BuildConfig.GARMIN_CONSUMER_KEY

    @Provides
    @Singleton
    @Named("GarminConsumerSecret")
    fun provideGarminConsumerSecret(): String = BuildConfig.GARMIN_CONSUMER_SECRET

    @Provides
    @Singleton
    @Named("GarminConnectUrl")
    fun provideGarminConnectUrl(): String = GarminConnect.BASE_URL

    @Provides
    @Singleton
    @Named("GarminPreAuthUrl")
    fun provideGarminPreAuthUrl(): String = GarminPreAuth.BASE_URL

    @Provides
    @Singleton
    @Named("GarminAuthUrl")
    fun provideGarminAuthUrl(): String = GarminAuth.BASE_URL

    @Provides
    @Singleton
    @Named("GarminSSOUrl")
    fun provideGarminSSOUrl(): String = GarminSSO.BASE_URL
}