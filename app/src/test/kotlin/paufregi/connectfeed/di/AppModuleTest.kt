package paufregi.connectfeed.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.datastore.models.Auth
import java.io.File
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class AppModuleTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val directory = File(context.filesDir, "test/datastore").also { it.mkdirs() }
    private val fileName = "connect_feed_${UUID.randomUUID()}.pb"
    private val masterKey = "test_app_module_key_${UUID.randomUUID()}"
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @After
    fun tearDown() {
        scope.cancel()
        directory.deleteRecursively()
    }

    @Test
    fun `DataStore - undecryptable file is replaced with empty Auth`() = runTest {
        File(directory, fileName).writeBytes("not-ciphertext".toByteArray(Charsets.UTF_8))
        val store = AppModule.provideDataStore(context, scope, masterKey, fileName, directory)

        assertThat(store.data.first()).isEqualTo(Auth())
    }

    @Test
    fun `DataStore - recovered store accepts writes`() = runTest {
        File(directory, fileName).writeBytes("not-ciphertext".toByteArray(Charsets.UTF_8))
        val store = AppModule.provideDataStore(context, scope, masterKey, fileName, directory)
        val user = User(1, "Paul", "url")

        store.updateData { it.copy(user = user) }

        assertThat(store.data.first().user).isEqualTo(user)
    }
}

