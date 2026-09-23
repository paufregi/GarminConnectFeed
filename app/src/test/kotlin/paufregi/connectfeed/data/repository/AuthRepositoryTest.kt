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
import paufregi.connectfeed.data.api.garmin.GarminSSO
import paufregi.connectfeed.data.api.garmin.models.LoginResponse
import paufregi.connectfeed.data.api.strava.StravaAuth
import paufregi.connectfeed.data.datastore.AuthStore
import paufregi.connectfeed.stravaAuthToken
import retrofit2.Response

class AuthRepositoryTest {

    private lateinit var repo: AuthRepository
    private val garminSSO = mockk<GarminSSO>()
    private val garminAuth = mockk<GarminAuth>()
    private val stravaAuth = mockk<StravaAuth>()
    private val datastore = mockk<AuthStore>()

    @Before
    fun setup(){
        repo = AuthRepository(garminSSO, garminAuth, stravaAuth, datastore)
    }

    @After
    fun tearDown(){
        confirmVerified(garminSSO, garminAuth, stravaAuth, datastore)
        clearAllMocks()
    }

    @Test
    fun `Garmin login - success`() = runTest {
        val responseStatus = LoginResponse.ResponseStatus("SUCCESSFUL")
        val loginResponse = LoginResponse(responseStatus, "ST-0123456-XXXXXXXXXXXXXXXXXXXX-sso")
        val httpResponse = Response.success(loginResponse)

        coEvery { garminSSO.login(any()) } returns httpResponse

        val res = repo.garminLogin("user@example.com", "password123")

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo("ST-0123456-XXXXXXXXXXXXXXXXXXXX-sso")

        coVerify { garminSSO.login(any()) }
    }

    @Test
    fun `Garmin login - invalid credentials`() = runTest {
        val responseStatus = LoginResponse.ResponseStatus("INVALID_USERNAME_PASSWORD")
        val loginResponse = LoginResponse(responseStatus, null)
        val httpResponse = Response.success(loginResponse)

        coEvery { garminSSO.login(any()) } returns httpResponse

        val res = repo.garminLogin("user@example.com", "wrongpassword")

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).contains("Invalid username or password")

        coVerify { garminSSO.login(any()) }
    }

    @Test
    fun `Garmin login - captcha required`() = runTest {
        val responseStatus = LoginResponse.ResponseStatus("CAPTCHA_REQUIRED")
        val loginResponse = LoginResponse(responseStatus, null)
        val httpResponse = Response.success(loginResponse)

        coEvery { garminSSO.login(any()) } returns httpResponse

        val res = repo.garminLogin("user@example.com", "password123")

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).contains("CAPTCHA required")

        coVerify { garminSSO.login(any()) }
    }

    @Test
    fun `Garmin login - no service ticket`() = runTest {
        val responseStatus = LoginResponse.ResponseStatus("SUCCESSFUL")
        val loginResponse = LoginResponse(responseStatus, null)
        val httpResponse = Response.success(loginResponse)

        coEvery { garminSSO.login(any()) } returns httpResponse

        val res = repo.garminLogin("user@example.com", "password123")

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).contains("no ticket found")

        coVerify { garminSSO.login(any()) }
    }

    @Test
    fun `Garmin exchange token - success`() = runTest {
        coEvery { garminAuth.exchange(any(), any(), any()) } returns Response.success(authToken)

        val res = repo.exchangeGarminToken("ST-0123456-XXXXXXXXXXXXXXXXXXXX-sso", "client-id")

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(authToken)

        coVerify { garminAuth.exchange(any(), any(), any()) }
    }

    @Test
    fun `Garmin exchange token - failure`() = runTest {
        coEvery { garminAuth.exchange(any(), any(), any()) } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.exchangeGarminToken("invalid-ticket", "client-id")

        assertThat(res.isSuccess).isFalse()

        coVerify { garminAuth.exchange(any(), any(), any()) }
    }

    @Test
    fun `Garmin refresh token - success`() = runTest {
        coEvery { garminAuth.refresh(any(), any(), any()) } returns Response.success(authToken)

        val res = repo.refreshGarminToken(authToken.refreshToken, "client-id")

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(authToken)

        coVerify { garminAuth.refresh(any(), any(), any()) }
    }

    @Test
    fun `Garmin refresh token - failure`() = runTest {
        coEvery { garminAuth.refresh(any(), any(), any()) } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.refreshGarminToken("invalid-refresh-token", "client-id")

        assertThat(res.isSuccess).isFalse()

        coVerify { garminAuth.refresh(any(), any(), any()) }
    }

    @Test
    fun `Get Garmin token`() = runTest {
        every { datastore.garminToken } returns flowOf(authToken)

        repo.getGarminToken().test {
            assertThat(awaitItem()).isEqualTo(authToken)
            cancelAndIgnoreRemainingEvents()
        }

        verify { datastore.garminToken }
    }

    @Test
    fun `Save Garmin token`() = runTest {
        coEvery { datastore.saveGarminToken(any()) } returns Unit

        repo.saveGarminToken(authToken)

        coVerify { datastore.saveGarminToken(authToken) }
    }

    @Test
    fun `Strava exchange token - success`() = runTest {
        coEvery { stravaAuth.exchange(any(), any(), any()) } returns Response.success(stravaAuthToken)

        val res = repo.exchangeStravaToken("client-id", "client-secret", "auth-code")

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(stravaAuthToken)

        coVerify { stravaAuth.exchange("client-id", "client-secret", "auth-code") }
    }

    @Test
    fun `Strava exchange token - failure`() = runTest {
        coEvery { stravaAuth.exchange(any(), any(), any()) } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.exchangeStravaToken("client-id", "client-secret", "invalid-code")

        assertThat(res.isSuccess).isFalse()

        coVerify { stravaAuth.exchange("client-id", "client-secret", "invalid-code") }
    }

    @Test
    fun `Strava refresh token - success`() = runTest {
        coEvery { stravaAuth.refresh(any(), any(), any()) } returns Response.success(stravaAuthToken)

        val res = repo.refreshStravaToken("client-id", "client-secret", "refresh-token")

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(stravaAuthToken)

        coVerify { stravaAuth.refresh("client-id", "client-secret", "refresh-token") }
    }

    @Test
    fun `Strava refresh token - failure`() = runTest {
        coEvery { stravaAuth.refresh(any(), any(), any()) } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.refreshStravaToken("client-id", "client-secret", "invalid-refresh-token")

        assertThat(res.isSuccess).isFalse()

        coVerify { stravaAuth.refresh("client-id", "client-secret", "invalid-refresh-token") }
    }

    @Test
    fun `Get Strava token`() = runTest {
        every { datastore.stravaToken } returns flowOf(stravaAuthToken)

        repo.getStravaToken().test {
            assertThat(awaitItem()).isEqualTo(stravaAuthToken)
            cancelAndIgnoreRemainingEvents()
        }

        verify { datastore.stravaToken }
    }

    @Test
    fun `Save Strava token`() = runTest {
        coEvery { datastore.saveStravaToken(any()) } returns Unit

        repo.saveStravaToken(stravaAuthToken)

        coVerify { datastore.saveStravaToken(stravaAuthToken) }
    }

    @Test
    fun `Clear Strava token`() = runTest {
        coEvery { datastore.clearStravaToken() } returns Unit

        repo.clearStravaToken()

        coVerify { datastore.clearStravaToken() }
    }

    @Test
    fun `Clear all auth data`() = runTest {
        coEvery { datastore.clear() } returns Unit

        repo.clear()

        coVerify { datastore.clear() }
    }
}