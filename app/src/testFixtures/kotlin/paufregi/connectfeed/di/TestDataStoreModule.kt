package paufregi.connectfeed.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import paufregi.connectfeed.data.datastore.AuthStore
import paufregi.connectfeed.data.datastore.StravaStore
import java.io.File
import java.util.UUID
import javax.inject.Singleton

/**
 * Provides DataStores backed by a fresh file per Hilt component (i.e. per test),
 * avoiding the process-wide `preferencesDataStore` singleton which cannot be
 * re-created between tests (Robolectric) and leaks state between tests (instrumented).
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataStoreModule::class]
)
object TestDataStoreModule {

    private fun create(context: Context, name: String): DataStore<Preferences> =
        PreferenceDataStoreFactory.create {
            File(context.cacheDir, "${name}_${UUID.randomUUID()}.preferences_pb")
        }

    @Provides
    @Singleton
    fun provideAuthStore(@ApplicationContext context: Context): AuthStore =
        AuthStore(dataStore = create(context, "authStore"))

    @Provides
    @Singleton
    fun provideStravaStore(@ApplicationContext context: Context): StravaStore =
        StravaStore(dataStore = create(context, "stravaStore"))
}

