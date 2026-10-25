package paufregi.connectfeed.system

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import paufregi.connectfeed.core.models.Release
import paufregi.connectfeed.core.models.Version

@RunWith(AndroidJUnit4::class)
class DownloaderTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val downloadManager = context.getSystemService(DownloadManager::class.java)
    private val downloader = Downloader(context, downloadManager)

    private val release = Release(Version(1, 2, 3), "https://example.com/releases/app.apk")

    @Test
    fun `Download APK`() {
        val result = downloader.downloadApk(release)

        assertThat(result.isSuccess).isTrue()
        val id = result.getOrThrow()

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

    @Test
    fun `Install downloaded APK`() {
        val id = downloader.downloadApk(release).getOrThrow()
        shadowOf(shadowOf(downloadManager).getRequest(id)).status = DownloadManager.STATUS_SUCCESSFUL

        val result = downloader.install(id)

        assertThat(result.isSuccess).isTrue()
        val intent = shadowOf(context as android.app.Application).nextStartedActivity
        assertThat(intent).isNotNull()
        assertThat(intent.action).isEqualTo(Intent.ACTION_VIEW)
        assertThat(intent.type).isEqualTo("application/vnd.android.package-archive")
        assertThat(intent.data).isEqualTo("content://paufregi.connectfeed.provider/update/app.apk".toUri())
        assertThat(intent.flags and Intent.FLAG_GRANT_READ_URI_PERMISSION).isNotEqualTo(0)
        assertThat(intent.flags and Intent.FLAG_ACTIVITY_NEW_TASK).isNotEqualTo(0)
    }

    @Test
    fun `Install - failed download`() {
        val id = downloader.downloadApk(release).getOrThrow()
        shadowOf(shadowOf(downloadManager).getRequest(id)).status = DownloadManager.STATUS_FAILED

        val result = downloader.install(id)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).hasMessageThat().isEqualTo("Downloaded update not found")
        assertThat(shadowOf(context as android.app.Application).nextStartedActivity).isNull()
    }

    @Test
    fun `Install - unknown download`() {
        val result = downloader.install(999L)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).hasMessageThat().isEqualTo("Downloaded update not found")
        assertThat(shadowOf(context as android.app.Application).nextStartedActivity).isNull()
    }
}

