package paufregi.connectfeed.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import java.io.File
import java.util.UUID
import javax.inject.Named
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AuthModule::class]
)
object TestAuthModule {

    @Provides
    @Singleton
    @Named("DataStoreDirectory")
    fun provideDataStoreDirectory(@ApplicationContext context: Context): File =
        File(context.filesDir, "test/datastore").also { it.mkdirs() }

    @Provides
    @Singleton
    @Named("DataStoreFileName")
    fun provideDataStoreFileName(): String = "connect_feed_${UUID.randomUUID()}.pb"

    @Provides
    @Singleton
    @Named("MasterKey")
    fun provideMasterKey(): String = "test_connect_feed_key_${UUID.randomUUID()}"

    fun clearTestDataStore(@ApplicationContext context: Context) {
        val testDir = File(context.filesDir, "datastore/test")
        if (testDir.exists()) {
            testDir.deleteRecursively()
        }
    }
}
