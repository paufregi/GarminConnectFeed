package paufregi.connectfeed.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import paufregi.connectfeed.data.api.garmin.GarminConnect
import paufregi.connectfeed.data.api.garmin.GarminAuth1
import paufregi.connectfeed.data.api.garmin.GarminAuth2
import paufregi.connectfeed.data.api.garmin.GarminSSO
import paufregi.connectfeed.data.api.garmin.Garth
import paufregi.connectfeed.data.api.strava.StravaAuth
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
    @Named("GarminConnectOAuth2Url")
    fun provideGarminConnectOAuth2Url(): String = GarminAuth2.BASE_URL

    @Provides
    @Singleton
    @Named("GarminSSOUrl")
    fun provideGarminSSOUrl(): String = GarminSSO.BASE_URL

    @Provides
    @Singleton
    @Named("GarthUrl")
    fun provideGarthUrl(): String = Garth.BASE_URL

    @Provides
    @Singleton
    @Named("StravaAuthUrl")
    fun provideStravaAuthUrl(): String = StravaAuth.BASE_URL
}