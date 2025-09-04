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
import paufregi.connectfeed.MockWebServerRule
import paufregi.connectfeed.githubDispatcher
import paufregi.connectfeed.githubPort
import paufregi.connectfeed.githubRelease
import paufregi.connectfeed.sslSocketFactory
import javax.inject.Inject

@HiltAndroidTest
class GithubRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: GithubRepository


    @JvmField @Rule val githubServer = MockWebServerRule(githubPort, sslSocketFactory, githubDispatcher)

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

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(githubRelease)
    }
}