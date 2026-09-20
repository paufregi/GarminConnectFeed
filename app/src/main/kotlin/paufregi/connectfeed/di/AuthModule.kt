package paufregi.connectfeed.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    @Named("DataStoreFileName")
    fun provideDataStoreFileName(): String = "connect_feed.pb"

    @Provides
    @Singleton
    @Named("MasterKey")
    fun provideMasterKey(): String = "connect_feed_key"
}