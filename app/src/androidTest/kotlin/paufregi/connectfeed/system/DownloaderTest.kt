package paufregi.connectfeed.system

import android.app.DownloadManager
import android.content.Context
import androidx.core.net.toUri
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.core.models.Release
import paufregi.connectfeed.core.models.Version

class DownloaderTest {
    private val context = mockk<Context>()
    private val downloadManager= mockk<DownloadManager>()


    private lateinit var downloader: Downloader

    @Before
    fun setUp() {
        downloader = Downloader(context, downloadManager)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `Download Apk`() {
        every { downloadManager.enqueue(any()) } returns 2L

        val release = Release(Version(1, 0, 0), "https://example.com/app.apk")
        val expectedRequest = DownloadManager.Request("https://example.com/app.apk".toUri())
            .setMimeType("application/vnd.android.package-archive")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
            .setTitle("app.apk")
            .setDestinationInExternalFilesDir(context, "update", "app.apk")

        val res = downloader.downloadApk(release)
        assertThat(res.isSuccess).isTrue()

        verify { downloadManager.enqueue(expectedRequest) }
        confirmVerified(context, downloadManager)
    }

    @Test
    fun `Download Apk - invalid url`() {
        val release = Release(Version(1, 0, 0), "https://example.com/app.zip")

        val res = downloader.downloadApk(release)
        assertThat(res.isSuccess).isFalse()

        confirmVerified(context, downloadManager)
    }

    @Test
    fun `Download Apk - error`() {
        every { downloadManager.enqueue(any()) } throws Exception("error")
        val release = Release(Version(1, 0, 0), "https://example.com/app.apk")
        val expectedRequest = DownloadManager.Request("https://example.com/app.apk".toUri())
            .setMimeType("application/vnd.android.package-archive")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
            .setTitle("app.apk")
            .setDestinationInExternalFilesDir(context, "update", "app.apk")


        val res = downloader.downloadApk(release)
        assertThat(res.isSuccess).isFalse()

        verify { downloadManager.enqueue(expectedRequest) }
        confirmVerified(context, downloadManager)
    }
}