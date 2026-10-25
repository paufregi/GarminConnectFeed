package paufregi.connectfeed.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    @Named("MasterKey")
    fun provideMasterKey(): String = "connect_feed_key"

    @Provides
    @Singleton
    @Named("DataStoreFileName")
    fun provideDataStoreFileName(): String = "connect_feed.pb"

    @Provides
    @Singleton
    @Named("DataStoreDirectory")
    fun provideDataStoreDirectory(@ApplicationContext context: Context): File =
        File(context.filesDir, "datastore").also { it.mkdirs() }
}