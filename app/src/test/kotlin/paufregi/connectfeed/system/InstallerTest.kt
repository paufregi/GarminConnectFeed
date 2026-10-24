package paufregi.connectfeed.system

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import paufregi.connectfeed.core.models.Release
import paufregi.connectfeed.core.models.Version

@RunWith(AndroidJUnit4::class)
class InstallerTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val downloadManager = context.getSystemService(DownloadManager::class.java)
    private val store = UpdateStore(context)
    private val downloader = Downloader(context, downloadManager, store)
    private val installer = Installer()

    private val release = Release(Version(1, 2, 3), "https://example.com/app.apk")

    private fun completeIntent(id: Long) =
        Intent(DownloadManager.ACTION_DOWNLOAD_COMPLETE).putExtra(DownloadManager.EXTRA_DOWNLOAD_ID, id)

    @After
    fun tearDown() {
        store.clearPendingDownload()
    }

    @Test
    fun `Install downloaded APK`() {
        val id = downloader.downloadApk(release).getOrThrow()
        shadowOf(shadowOf(downloadManager).getRequest(id)).status = DownloadManager.STATUS_SUCCESSFUL

        installer.onReceive(context, completeIntent(id))

        val intent = shadowOf(context as android.app.Application).nextStartedActivity
        assertThat(intent).isNotNull()
        assertThat(intent.action).isEqualTo(Intent.ACTION_VIEW)
        assertThat(intent.type).isEqualTo("application/vnd.android.package-archive")
        assertThat(intent.data).isEqualTo("content://paufregi.connectfeed.provider/update/app.apk".toUri())
        assertThat(intent.flags and Intent.FLAG_GRANT_READ_URI_PERMISSION).isNotEqualTo(0)
        assertThat(intent.flags and Intent.FLAG_ACTIVITY_NEW_TASK).isNotEqualTo(0)
        assertThat(store.pendingDownloadId).isNull()
    }

    @Test
    fun `Ignore download not requested by the app`() {
        val id = downloader.downloadApk(release).getOrThrow()
        shadowOf(shadowOf(downloadManager).getRequest(id)).status = DownloadManager.STATUS_SUCCESSFUL

        installer.onReceive(context, completeIntent(id + 1))

        assertThat(shadowOf(context as android.app.Application).nextStartedActivity).isNull()
        assertThat(store.pendingDownloadId).isEqualTo(id)
    }

    @Test
    fun `Ignore failed download`() {
        val id = downloader.downloadApk(release).getOrThrow()
        shadowOf(shadowOf(downloadManager).getRequest(id)).status = DownloadManager.STATUS_FAILED

        installer.onReceive(context, completeIntent(id))

        assertThat(shadowOf(context as android.app.Application).nextStartedActivity).isNull()
        assertThat(store.pendingDownloadId).isNull()
    }

    @Test
    fun `Ignore unrelated action`() {
        val id = downloader.downloadApk(release).getOrThrow()
        shadowOf(shadowOf(downloadManager).getRequest(id)).status = DownloadManager.STATUS_SUCCESSFUL

        installer.onReceive(context, Intent(Intent.ACTION_VIEW).putExtra(DownloadManager.EXTRA_DOWNLOAD_ID, id))

        assertThat(shadowOf(context as android.app.Application).nextStartedActivity).isNull()
        assertThat(store.pendingDownloadId).isEqualTo(id)
    }

    @Test
    fun `Ignore missing download id`() {
        installer.onReceive(context, Intent(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        assertThat(shadowOf(context as android.app.Application).nextStartedActivity).isNull()
    }
}

