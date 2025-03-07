package paufregi.connectfeed.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.verify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.createStravaToken
import paufregi.connectfeed.data.api.strava.StravaAuth
import paufregi.connectfeed.data.datastore.StravaStore
import paufregi.connectfeed.tomorrow
import retrofit2.Response

class StravaAuthRepositoryTest {

    private lateinit var repo: StravaAuthRepository
    private val store = mockk<StravaStore>()
    private val auth = mockk<StravaAuth>()

    @Before
    fun setup(){
        repo = StravaAuthRepository(store, auth)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Get token`() = runTest {
        val token = createStravaToken(tomorrow)
        every { store.getToken() } returns flowOf(token)

        repo.getToken().test {
            assertThat(awaitItem()).isEqualTo(token)
            cancelAndIgnoreRemainingEvents()
        }

        verify { store.getToken() }
        confirmVerified(store, auth)
    }

    @Test
    fun `Save token`() = runTest {
        val token = createStravaToken(tomorrow)
        coEvery { store.saveToken(any()) } returns Unit

        repo.saveToken(token)

        coVerify { store.saveToken(token) }
        confirmVerified(store, auth)
    }

    @Test
    fun `Clear store`() = runTest {
        coEvery { store.clear() } returns Unit

        repo.clear()

        coVerify { store.clear() }
        confirmVerified(store, auth)
    }

    @Test
    fun `Exchange token`() = runTest {
        val token = createStravaToken(tomorrow)
        coEvery { auth.exchange(any(), any(), any()) } returns Response.success(token)

        val res = repo.exchange("CLIENT_ID", "CLIENT_SECRET", "CODE")

        assertThat(res.isSuccessful).isTrue()
        res as Result.Success
        assertThat(res.data).isEqualTo(token)

        coVerify { auth.exchange("CLIENT_ID", "CLIENT_SECRET", "CODE") }
        confirmVerified(store, auth)
    }

    @Test
    fun `Exchange token - failure`() = runTest {
        coEvery { auth.exchange(any(), any(), any()) } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.exchange("CLIENT_ID", "CLIENT_SECRET", "CODE")

        assertThat(res.isSuccessful).isFalse()

        coVerify { auth.exchange("CLIENT_ID", "CLIENT_SECRET", "CODE") }
        confirmVerified(store, auth)
    }

    @Test
    fun `Refresh token`() = runTest {
        val token = createStravaToken(tomorrow)
        coEvery { auth.refreshAccessToken(any(), any(), any()) } returns Response.success(token)

        val res = repo.refresh("CLIENT_ID", "CLIENT_SECRET", "CODE")

        assertThat(res.isSuccessful).isTrue()
        res as Result.Success
        assertThat(res.data).isEqualTo(token)

        coVerify { auth.refreshAccessToken("CLIENT_ID", "CLIENT_SECRET", "CODE") }
        confirmVerified(store, auth)
    }

    @Test
    fun `Refresh token - failure`() = runTest {
        coEvery { auth.refreshAccessToken(any(), any(), any()) } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.refresh("CLIENT_ID", "CLIENT_SECRET", "CODE")

        assertThat(res.isSuccessful).isFalse()

        coVerify { auth.refreshAccessToken("CLIENT_ID", "CLIENT_SECRET", "CODE") }
        confirmVerified(store, auth)
    }
}