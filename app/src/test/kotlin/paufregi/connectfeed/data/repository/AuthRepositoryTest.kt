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
import paufregi.connectfeed.consumer
import paufregi.connectfeed.data.api.garmin.GarminAuth1
import paufregi.connectfeed.data.api.garmin.GarminAuth
import paufregi.connectfeed.data.api.garmin.GarminSSO
import paufregi.connectfeed.data.api.garmin.Garth
import paufregi.connectfeed.data.api.garmin.models.CSRF
import paufregi.connectfeed.data.api.garmin.models.Ticket
import paufregi.connectfeed.data.datastore.AuthStore
import paufregi.connectfeed.oauth1
import paufregi.connectfeed.authToken
import paufregi.connectfeed.user
import retrofit2.Response

class AuthRepositoryTest {

    private lateinit var repo: AuthRepository
    private val garth = mockk<Garth>()
    private val garminSSO = mockk<GarminSSO>()
    private val authDatastore = mockk<AuthStore>()
    private val garminAuth1 = mockk<GarminAuth1>()
    private val garminAuth = mockk<GarminAuth>()

    @Before
    fun setup(){
        repo = AuthRepository(
            garth,
            garminSSO,
            authDatastore,
            { _ -> garminAuth1},
            { _, _ -> garminAuth}
        )
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Get oauth1`() = runTest {
        every { authDatastore.getOAuth1() } returns flowOf(oauth1)

        repo.getOAuth1().test {
            assertThat(awaitItem()).isEqualTo(oauth1)
            cancelAndIgnoreRemainingEvents()
        }

        verify { authDatastore.getOAuth1() }
        confirmVerified(garth, garminSSO, authDatastore)
    }

    @Test
    fun `Save oauth1`() = runTest {
        coEvery { authDatastore.saveOAuth1(any()) } returns Unit

        repo.saveOAuth1(oauth1)

        coVerify { authDatastore.saveOAuth1(oauth1) }
        confirmVerified(garth, garminSSO, authDatastore)
    }

    @Test
    fun `Get oauth2`() = runTest {
        every { authDatastore.getAuthToken() } returns flowOf(authToken)

        repo.getAuthToken().test {
            assertThat(awaitItem()).isEqualTo(authToken)
            cancelAndIgnoreRemainingEvents()
        }

        verify { authDatastore.getAuthToken() }
        confirmVerified(garth, garminSSO, authDatastore)
    }

    @Test
    fun `Save oauth2`() = runTest {
        coEvery { authDatastore.saveAuthToken(any()) } returns Unit

        repo.saveAuthToken(authToken)

        coVerify { authDatastore.saveAuthToken(authToken) }
        confirmVerified(garth, garminSSO, authDatastore)
    }

    @Test
    fun `Get user`() = runTest {
        every { authDatastore.getUser() } returns flowOf(user)

        repo.getUser().test {
            assertThat(awaitItem()).isEqualTo(user)
            cancelAndIgnoreRemainingEvents()
        }

        verify { authDatastore.getUser() }
        confirmVerified(garth, garminSSO, authDatastore)
    }

    @Test
    fun `Save user`() = runTest {
        coEvery { authDatastore.saveUser(any()) } returns Unit

        repo.saveUser(user)

        coVerify { authDatastore.saveUser(user) }
        confirmVerified(garth, garminSSO, authDatastore)
    }

    @Test
    fun `Clear datastore`() = runTest {
        coEvery { authDatastore.clear() } returns Unit

        repo.clear()

        coVerify { authDatastore.clear() }
        confirmVerified(garth, garminSSO, authDatastore)
    }

    @Test
    fun `Get consumer`() = runTest {
        every { authDatastore.getConsumer() } returns flowOf(consumer)

        val res = repo.getOrFetchConsumer()

        assertThat(res).isEqualTo(consumer)

        verify { authDatastore.getConsumer() }
        confirmVerified(garth, garminSSO, authDatastore)
    }

    @Test
    fun `Fetch consumer`() = runTest {
        every { authDatastore.getConsumer() } returns flowOf(null)
        coEvery { garth.getOAuthConsumer() } returns Response.success(consumer)
        coEvery { authDatastore.saveConsumer(any()) } returns Unit

        val res = repo.getOrFetchConsumer()

        assertThat(res).isEqualTo(consumer)

        verify { authDatastore.getConsumer() }
        coVerify {
            garth.getOAuthConsumer()
            authDatastore.saveConsumer(consumer)
        }
        confirmVerified(garth, garminSSO, authDatastore)
    }

    @Test
    fun `Fetch consumer failure`() = runTest {
        every { authDatastore.getConsumer() } returns flowOf(null)
        coEvery { garth.getOAuthConsumer() } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.getOrFetchConsumer()

        assertThat(res).isNull()

        verify { authDatastore.getConsumer() }
        coVerify { garth.getOAuthConsumer() }
        confirmVerified(garth, garminSSO, authDatastore)
    }

    @Test
    fun `Authorize success`() = runTest {
        val csrf = CSRF("csrf")
        val ticket = Ticket("ticket")

        coEvery { garminSSO.getCSRF() } returns Response.success(csrf)
        coEvery { garminSSO.login(any(), any(), any()) } returns Response.success(ticket)
        coEvery { garminAuth1.getOauth1(any()) } returns Response.success(oauth1)

        val res = repo.authorize("user", "pass", consumer)


        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(oauth1)

        coVerify{
            garminSSO.getCSRF()
            garminSSO.login("user", "pass", csrf)
            garminAuth1.getOauth1(ticket)
        }
        confirmVerified(garth, garminSSO, authDatastore)
    }

    @Test
    fun `Authorize failure`() = runTest {
        val csrf = CSRF("csrf")
        val ticket = Ticket("ticket")

        coEvery { garminSSO.getCSRF() } returns Response.success(csrf)
        coEvery { garminSSO.login(any(), any(), any()) } returns Response.success(ticket)
        coEvery { garminAuth1.getOauth1(any()) } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.authorize("user", "pass", consumer)

        assertThat(res.isSuccess).isFalse()

        coVerify{
            garminSSO.getCSRF()
            garminSSO.login("user", "pass", csrf)
            garminAuth1.getOauth1(ticket)
        }
        confirmVerified(garth, garminSSO, authDatastore)
    }

    @Test
    fun `Authorize failure - login page`() = runTest {
        coEvery { garminSSO.getCSRF() } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.authorize("user", "pas", consumer)

        assertThat(res.isSuccess).isFalse()

        coVerify{
            garminSSO.getCSRF()
        }
        confirmVerified(garth, garminSSO, authDatastore)
    }

    @Test
    fun `Authorize failure - login`() = runTest {
        val csrf = CSRF("csrf")

        coEvery { garminSSO.getCSRF() } returns Response.success(csrf)
        coEvery { garminSSO.login(any(), any(), any()) } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.authorize("user", "pass", consumer)

        assertThat(res.isSuccess).isFalse()

        coVerify{
            garminSSO.getCSRF()
            garminSSO.login("user", "pass", csrf)
        }
        confirmVerified(garth, garminSSO, authDatastore)
    }

    @Test
    fun `Exchange success`() = runTest {
        coEvery { garminAuth.exchange() } returns Response.success(authToken)
        coEvery { authDatastore.saveAuthToken(any()) } returns Unit

        val res = repo.exchange(consumer, oauth1)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(authToken)

        coVerify {
            garminAuth.exchange()
            authDatastore.saveAuthToken(authToken)
        }
        confirmVerified(garth, garminSSO, authDatastore)
    }

    @Test
    fun `Exchange failure`() = runTest {
        coEvery { garminAuth.exchange() } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.exchange(consumer, oauth1)

        assertThat(res.isSuccess).isFalse()

        coVerify {
            garminAuth.exchange()
        }
        confirmVerified(garth, garminSSO, authDatastore)
    }
}