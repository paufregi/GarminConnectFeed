package paufregi.connectfeed.presentation.info

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import paufregi.connectfeed.core.models.Release
import paufregi.connectfeed.core.models.Version

@ExperimentalCoroutinesApi
class InfoStateTest {

    @Test
    fun `Has update`() = runTest {
        val state = InfoState(
            currentVersion = Version(1,0,0),
            latestRelease = Release(
                version = Version(2, 0, 0),
                downloadUrl = "http://example.com/app.apk"
            )
        )

        assertThat(state.hasUpdate).isTrue()
    }

    @Test
    fun `Has no update`() = runTest {
        val state = InfoState(
            currentVersion = Version(1,0,0),
            latestRelease = Release(
                version = Version(1, 0, 0),
                downloadUrl = "http://example.com/app.apk"
            )
        )

        assertThat(state.hasUpdate).isFalse()
    }
}
