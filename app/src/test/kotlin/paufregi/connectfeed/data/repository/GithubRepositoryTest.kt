package paufregi.connectfeed.data.repository

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
import paufregi.connectfeed.data.api.github.Github
import paufregi.connectfeed.data.api.github.models.Asset
import paufregi.connectfeed.githubRelease
import retrofit2.Response

class GithubRepositoryTest {

    private lateinit var repo: GithubRepository
    private val github = mockk<Github>()

    @Before
    fun setup(){
        repo = GithubRepository(github)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Get Latest Release`() = runTest {
        coEvery { github.getLatestRelease() } returns Response.success(githubRelease)

        val res =  repo.getLatestRelease()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(githubRelease.toCore())

        coVerify { github.getLatestRelease() }
        confirmVerified(github)
    }

    @Test
    fun `Get Latest Release - invalid version`() = runTest {
        coEvery { github.getLatestRelease() } returns Response.success(githubRelease.copy(tagName = "invalid"))

        val res =  repo.getLatestRelease()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isNull()

        coVerify { github.getLatestRelease() }
        confirmVerified(github)
    }

    @Test
    fun `Get Latest Release - no assets`() = runTest {

        coEvery { github.getLatestRelease() } returns Response.success(githubRelease.copy(assets = emptyList()))

        val res =  repo.getLatestRelease()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isNull()

        coVerify { github.getLatestRelease() }
        confirmVerified(github)
    }

    @Test
    fun `Get Latest Release - invalid url`() = runTest {

        coEvery { github.getLatestRelease() } returns Response.success(githubRelease.copy(assets = listOf(
            Asset("application/vnd.android.package-archive",  "invalid"))))

        val res =  repo.getLatestRelease()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isNull()

        coVerify { github.getLatestRelease() }
        confirmVerified(github)
    }

    @Test
    fun `Get Latest Release - invalid contentType`() = runTest {

        coEvery { github.getLatestRelease() } returns Response.success(githubRelease.copy(assets = listOf(
            Asset("invalid",  "http://example.com/app.apk"))))

        val res =  repo.getLatestRelease()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isNull()

        coVerify { github.getLatestRelease() }
        confirmVerified(github)
    }
}