package paufregi.connectfeed.system

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UpdateStoreTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val store = UpdateStore(context)

    @After
    fun tearDown() {
        store.clearPendingDownload()
    }

    @Test
    fun `Pending download - none`() {
        assertThat(store.pendingDownloadId).isNull()
    }

    @Test
    fun `Save pending download`() {
        store.savePendingDownload(42L)

        assertThat(store.pendingDownloadId).isEqualTo(42L)
        assertThat(UpdateStore(context).pendingDownloadId).isEqualTo(42L)
    }

    @Test
    fun `Clear pending download`() {
        store.savePendingDownload(42L)

        store.clearPendingDownload()

        assertThat(store.pendingDownloadId).isNull()
    }
}

