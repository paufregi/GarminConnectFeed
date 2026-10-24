package paufregi.connectfeed.system

import android.app.DownloadManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import paufregi.connectfeed.core.models.Release
import paufregi.connectfeed.core.models.Version

@RunWith(AndroidJUnit4::class)
class DownloaderTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val downloadManager = context.getSystemService(DownloadManager::class.java)
    private val store = UpdateStore(context)
    private val downloader = Downloader(context, downloadManager, store)

    private val release = Release(Version(1, 2, 3), "https://example.com/releases/app.apk")

    @After
    fun tearDown() {
        store.clearPendingDownload()
    }

    @Test
    fun `Download APK`() {
        val result = downloader.downloadApk(release)

        assertThat(result.isSuccess).isTrue()
        val id = result.getOrThrow()
        assertThat(store.pendingDownloadId).isEqualTo(id)

        val request = shadowOf(shadowOf(downloadManager).getRequest(id))
        assertThat(request.uri.toString()).isEqualTo(release.downloadUrl)
        assertThat(request.title.toString()).isEqualTo("app.apk")
        assertThat(request.mimeType.toString()).isEqualTo("application/vnd.android.package-archive")
        assertThat(request.allowedNetworkTypes)
            .isEqualTo(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        assertThat(request.notificationVisibility).isEqualTo(DownloadManager.Request.VISIBILITY_HIDDEN)
        assertThat(request.destination.path).isEqualTo("${context.getExternalFilesDir("update")}/app.apk")
    }

    @Test
    fun `Download APK - invalid URL`() {
        val result = downloader.downloadApk(release.copy(downloadUrl = "https://example.com/release.zip"))

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).hasMessageThat().isEqualTo("Invalid APK URL")
        assertThat(store.pendingDownloadId).isNull()
        assertThat(shadowOf(downloadManager).requestCount).isEqualTo(0)
    }

    @Test
    fun `Status - completed`() = runTest {
        val id = downloader.downloadApk(release).getOrThrow()
        val request = shadowOf(shadowOf(downloadManager).getRequest(id))
        request.status = DownloadManager.STATUS_RUNNING

        downloader.status(id).test {
            assertThat(awaitItem()).isEqualTo(DownloadStatus.Running)
            request.status = DownloadManager.STATUS_SUCCESSFUL
            assertThat(awaitItem()).isEqualTo(DownloadStatus.Completed)
            awaitComplete()
        }
    }

    @Test
    fun `Status - failed`() = runTest {
        val id = downloader.downloadApk(release).getOrThrow()
        shadowOf(shadowOf(downloadManager).getRequest(id)).status = DownloadManager.STATUS_FAILED

        downloader.status(id).test {
            assertThat(awaitItem()).isEqualTo(DownloadStatus.Failed)
            awaitComplete()
        }
    }

    @Test
    fun `Status - unknown download`() = runTest {
        downloader.status(999L).test {
            assertThat(awaitItem()).isEqualTo(DownloadStatus.Failed)
            awaitComplete()
        }
    }
}


