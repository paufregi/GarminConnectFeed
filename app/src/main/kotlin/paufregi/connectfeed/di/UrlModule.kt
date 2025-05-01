package paufregi.connectfeed.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import paufregi.connectfeed.data.api.garmin.GarminAuth1
import paufregi.connectfeed.data.api.garmin.GarminAuth
import paufregi.connectfeed.data.api.garmin.GarminConnect
import paufregi.connectfeed.data.api.garmin.GarminSSO
import paufregi.connectfeed.data.api.garmin.Garth
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UrlModule {

    @Provides
    @Singleton
    @Named("GarminConnectUrl")
    fun provideGarminConnectUrl(): String = GarminConnect.BASE_URL

    @Provides
    @Singleton
    @Named("GarminConnectOAuth1Url")
    fun provideGarminConnectOAuth1Url(): String = GarminAuth1.BASE_URL

    @Provides
    @Singleton
    @Named("GarminAuthUrl")
    fun provideGarminAuthUrl(): String = GarminAuth.BASE_URL

    @Provides
    @Singleton
    @Named("GarminSSOUrl")
    fun provideGarminSSOUrl(): String = GarminSSO.BASE_URL

    @Provides
    @Singleton
    @Named("GarthUrl")
    fun provideGarthUrl(): String = Garth.BASE_URL
}