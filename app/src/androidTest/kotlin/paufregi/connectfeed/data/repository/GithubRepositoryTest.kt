package paufregi.connectfeed.data.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.MockServer
import paufregi.connectfeed.core.models.Release
import paufregi.connectfeed.core.models.Version
import paufregi.connectfeed.githubDispatcher
import paufregi.connectfeed.githubPort
import javax.inject.Inject

@HiltAndroidTest
class GithubRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: GithubRepository

    @JvmField @Rule val githubServer = MockServer.createSecure(githubPort, githubDispatcher)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `Get latest release`() = runTest {
        val res = repo.getLatestRelease()

        val expected = Release(
            version = Version(2, 2, 2),
            downloadUrl = "https://github.com/paufregi/GarminConnectFeed/releases/download/v2.2.2/ConnectFeed-v2.2.2.apk"
        )


        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)
    }
}