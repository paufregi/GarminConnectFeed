package paufregi.connectfeed.data.api.github

import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.MockWebServerRule
import paufregi.connectfeed.githubLatestReleaseJson
import paufregi.connectfeed.githubRelease

class GithubTest {

    @JvmField @Rule val server = MockWebServerRule()
    private lateinit var api: Github

    @Before
    fun setup() {
        api = Github.client(server.url("/").toString())
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `Get latest release`() = runTest {
        server.enqueue(code = 200, body = githubLatestReleaseJson)

        val res = api.getLatestRelease()

        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(githubRelease)
    }

    @Test
    fun `Get activities - failure`() = runTest {
        server.enqueue(400)

        val res = api.getLatestRelease()

        assertThat(res.isSuccessful).isFalse()
    }
}