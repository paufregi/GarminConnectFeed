package paufregi.connectfeed.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import paufregi.connectfeed.data.api.garmin.GarminAuth
import paufregi.connectfeed.data.api.garmin.GarminConnect
import paufregi.connectfeed.data.api.garmin.GarminSSO
import paufregi.connectfeed.data.api.github.Github
import paufregi.connectfeed.data.api.strava.Strava
import paufregi.connectfeed.data.api.strava.StravaAuth
import javax.inject.Named
import javax.inject.Singleton
import paufregi.connectfeed.data.api.garmin.interceptors.AuthInterceptor as GarminAuthInterceptor
import paufregi.connectfeed.data.api.strava.interceptors.AuthInterceptor as StravaAuthInterceptor

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGarminSSO(
        @Named("GarminSSOUrl") url: String,
    ): GarminSSO = GarminSSO.client(url)

    @Provides
    @Singleton
    fun provideGarminAuth(
        @Named("GarminAuthUrl") url: String,
    ): GarminAuth = GarminAuth.client(url)

    @Provides
    @Singleton
    fun provideGarminConnect(
        authInterceptor: GarminAuthInterceptor,
        @Named("GarminConnectUrl") url: String,
    ): GarminConnect = GarminConnect.client(authInterceptor, url)

    @Provides
    @Singleton
    fun provideStravaAuth(
        @Named("StravaAuthUrl") url: String,
    ): StravaAuth = StravaAuth.client(url)

    @Provides
    @Singleton
    fun provideStrava(
        authInterceptor: StravaAuthInterceptor,
        @Named("StravaUrl") url: String,
    ): Strava = Strava.client(authInterceptor, url)

    @Provides
    @Singleton
    fun provideGithub(
        @Named("GithubUrl") url: String,
    ): Github = Github.client(url)
}

