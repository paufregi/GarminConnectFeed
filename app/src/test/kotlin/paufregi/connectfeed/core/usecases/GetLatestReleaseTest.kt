package paufregi.connectfeed.core.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.core.models.Release
import paufregi.connectfeed.core.models.Version
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.repository.GithubRepository

class GetLatestReleaseTest {

    private val repo = mockk<GithubRepository>()
    private lateinit var useCase: GetLatestRelease

    @Before
    fun setup(){
        useCase = GetLatestRelease(repo)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Get latest release`() = runTest {
        val release = Release(
            version = Version(1, 2, 3),
            downloadUrl = "https://example.com/release",
        )
        coEvery { repo.getLatestRelease() } returns Result.success(release)
        val res = useCase()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(release)
        coVerify { repo.getLatestRelease() }
        confirmVerified(repo)
    }

    @Test
    fun `Get latest release - null`() = runTest {
        coEvery { repo.getLatestRelease() } returns Result.success(null)
        val res = useCase()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isNull()
        coVerify { repo.getLatestRelease() }
        confirmVerified(repo)
    }

    @Test
    fun `Get latest release - failure`() = runTest {
        coEvery { repo.getLatestRelease() } returns Result.failure("error")
        val res = useCase()

        assertThat(res.isSuccess).isFalse()
        coVerify { repo.getLatestRelease() }
        confirmVerified(repo)
    }
}