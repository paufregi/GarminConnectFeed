package paufregi.connectfeed.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.authToken
import paufregi.connectfeed.data.api.garmin.GarminAuth
import paufregi.connectfeed.data.api.garmin.GarminPreAuth
import paufregi.connectfeed.data.api.garmin.GarminSSO
import paufregi.connectfeed.data.api.garmin.models.CSRF
import paufregi.connectfeed.data.api.garmin.models.Ticket
import paufregi.connectfeed.data.datastore.AuthStore
import paufregi.connectfeed.preAuthToken
import paufregi.connectfeed.user
import retrofit2.Response

class AuthRepositoryTest {

    private lateinit var repo: AuthRepository
    private val garminSSO = mockk<GarminSSO>()
    private val authDatastore = mockk<AuthStore>()
    private val garminPreAuth = mockk<GarminPreAuth>()
    private val garminAuth = mockk<GarminAuth>()

    @Before
    fun setup(){
        repo = AuthRepository(
            garminSSO,
            authDatastore,
            garminPreAuth,
            { _ -> garminAuth}
        )
    }

    @After
    fun tearDown(){
        confirmVerified(garminSSO, authDatastore)
        clearAllMocks()
    }

    @Test
    fun `Get PreAuthToken`() = runTest {
        every { authDatastore.getPreAuthToken() } returns flowOf(preAuthToken)

        repo.getPreAuth().test {
            assertThat(awaitItem()).isEqualTo(preAuthToken)
            cancelAndIgnoreRemainingEvents()
        }

        verify { authDatastore.getPreAuthToken() }
    }

    @Test
    fun `Save PreAuthToken`() = runTest {
        coEvery { authDatastore.savePreAuthToken(any()) } returns Unit

        repo.savePreAuth(preAuthToken)

        coVerify { authDatastore.savePreAuthToken(preAuthToken) }
    }

    @Test
    fun `Get AuthToken`() = runTest {
        every { authDatastore.getAuthToken() } returns flowOf(authToken)

        repo.getAuthToken().test {
            assertThat(awaitItem()).isEqualTo(authToken)
            cancelAndIgnoreRemainingEvents()
        }

        verify { authDatastore.getAuthToken() }
    }

    @Test
    fun `Save AuthToken`() = runTest {
        coEvery { authDatastore.saveAuthToken(any()) } returns Unit

        repo.saveAuthToken(authToken)

        coVerify { authDatastore.saveAuthToken(authToken) }
    }

    @Test
    fun `Get user`() = runTest {
        every { authDatastore.getUser() } returns flowOf(user)

        repo.getUser().test {
            assertThat(awaitItem()).isEqualTo(user)
            cancelAndIgnoreRemainingEvents()
        }

        verify { authDatastore.getUser() }
    }

    @Test
    fun `Save user`() = runTest {
        coEvery { authDatastore.saveUser(any()) } returns Unit

        repo.saveUser(user)

        coVerify { authDatastore.saveUser(user) }
    }

    @Test
    fun `Clear datastore`() = runTest {
        coEvery { authDatastore.clear() } returns Unit

        repo.clear()

        coVerify { authDatastore.clear() }
    }

    @Test
    fun `Authorize success`() = runTest {
        val csrf = CSRF("csrf")
        val ticket = Ticket("ticket")

        coEvery { garminSSO.getCSRF() } returns Response.success(csrf)
        coEvery { garminSSO.login(any(), any(), any()) } returns Response.success(ticket)
        coEvery { garminPreAuth.preauthorize(any()) } returns Response.success(preAuthToken)

        val res = repo.authorize("user", "pass")


        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(preAuthToken)

        coVerify{
            garminSSO.getCSRF()
            garminSSO.login("user", "pass", csrf)
            garminPreAuth.preauthorize(ticket)
        }
    }

    @Test
    fun `Authorize failure`() = runTest {
        val csrf = CSRF("csrf")
        val ticket = Ticket("ticket")

        coEvery { garminSSO.getCSRF() } returns Response.success(csrf)
        coEvery { garminSSO.login(any(), any(), any()) } returns Response.success(ticket)
        coEvery { garminPreAuth.preauthorize(any()) } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.authorize("user", "pass")

        assertThat(res.isSuccess).isFalse()

        coVerify{
            garminSSO.getCSRF()
            garminSSO.login("user", "pass", csrf)
            garminPreAuth.preauthorize(ticket)
        }
    }

    @Test
    fun `Authorize failure - login page`() = runTest {
        coEvery { garminSSO.getCSRF() } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.authorize("user", "pas")

        assertThat(res.isSuccess).isFalse()

        coVerify{
            garminSSO.getCSRF()
        }
    }

    @Test
    fun `Authorize failure - login`() = runTest {
        val csrf = CSRF("csrf")

        coEvery { garminSSO.getCSRF() } returns Response.success(csrf)
        coEvery { garminSSO.login(any(), any(), any()) } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.authorize("user", "pass")

        assertThat(res.isSuccess).isFalse()

        coVerify{
            garminSSO.getCSRF()
            garminSSO.login("user", "pass", csrf)
        }
    }

    @Test
    fun `Exchange success`() = runTest {
        coEvery { garminAuth.exchange() } returns Response.success(authToken)
        coEvery { authDatastore.saveAuthToken(any()) } returns Unit

        val res = repo.exchange(preAuthToken)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(authToken)

        coVerify {
            garminAuth.exchange()
            authDatastore.saveAuthToken(authToken)
        }
        confirmVerified( garminSSO, authDatastore)
    }

    @Test
    fun `Exchange failure`() = runTest {
        coEvery { garminAuth.exchange() } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.exchange(preAuthToken)

        assertThat(res.isSuccess).isFalse()

        coVerify {
            garminAuth.exchange()
        }
    }

    @Test
    fun `Refresh success`() = runTest {
        coEvery { garminAuth.refresh(any()) } returns Response.success(authToken)
        coEvery { authDatastore.saveAuthToken(any()) } returns Unit

        val res = repo.refresh(preAuthToken, authToken.refreshToken)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(authToken)

        coVerify {
            garminAuth.refresh(authToken.refreshToken)
            authDatastore.saveAuthToken(authToken)
        }
        confirmVerified( garminSSO, authDatastore)
    }

    @Test
    fun `Refresh failure`() = runTest {
        coEvery { garminAuth.refresh(any()) } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.refresh(preAuthToken, authToken.refreshToken)

        assertThat(res.isSuccess).isFalse()

        coVerify {
            garminAuth.refresh(authToken.refreshToken)
        }
    }
}