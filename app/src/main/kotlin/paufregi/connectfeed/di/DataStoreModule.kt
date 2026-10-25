package paufregi.connectfeed.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import paufregi.connectfeed.data.datastore.AuthStore
import paufregi.connectfeed.data.datastore.StravaStore
import javax.inject.Singleton

val Context.authStore: DataStore<Preferences> by preferencesDataStore(name = "authStore")
val Context.stravaStore: DataStore<Preferences> by preferencesDataStore(name = "stravaStore")

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideAuthStore(
        @ApplicationContext context: Context,
    ): AuthStore =
        AuthStore(dataStore = context.authStore)

    @Provides
    @Singleton
    fun provideStravaStore(
        @ApplicationContext context: Context,
    ): StravaStore =
        StravaStore(dataStore = context.stravaStore)
}

