package paufregi.connectfeed.data.api.garmin.utils

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
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.consumer
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.createOAuth2
import paufregi.connectfeed.data.api.garmin.interceptors.AuthInterceptor
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.oauth1
import paufregi.connectfeed.tomorrow
import paufregi.connectfeed.yesterday
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class AuthInterceptorTest {

    private lateinit var auth: AuthInterceptor
    private lateinit var api: TestApi

    private val authRepo = mockk<AuthRepository>()

    private val server = MockWebServer()

    interface TestApi {
        @GET("/test")
        suspend fun test(): Response<Unit>
    }

    @Before
    fun setup() {
        server.start()

        auth = AuthInterceptor(authRepo)
        server.enqueue(MockResponse().setResponseCode(200))
        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor(auth).build())
            .build()
            .create(TestApi::class.java)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        server.shutdown()
    }

    @Test
    fun `Success - cached oauth2`() = runTest {
        val oauth2 = createOAuth2(tomorrow)

        every { authRepo.getOAuth2() } returns flowOf(oauth2)

        api.test()

        val req = server.takeRequest()
        assertThat(req.headers["Authorization"]).isEqualTo("Bearer ${oauth2.accessToken}")

        verify { authRepo.getOAuth2() }
        confirmVerified(authRepo)
    }

    @Test
    fun `Success - no oauth2`() = runTest {
        val expiredToken = createOAuth2(yesterday)
        val validToken = createOAuth2(tomorrow)

        every { authRepo.getOAuth2() } returns flowOf(expiredToken)
        coEvery { authRepo.getOrFetchConsumer() } returns consumer
        every { authRepo.getOAuth1() } returns flowOf(oauth1)
        coEvery { authRepo.exchange(any(), any()) } returns Result.Success(validToken)
        coEvery { authRepo.saveOAuth2(any()) } returns Unit

        api.test()

        val req = server.takeRequest()
        assertThat(req.headers["Authorization"]).isEqualTo("Bearer ${validToken.accessToken}")

        verify {
            authRepo.getOAuth1()
            authRepo.getOAuth2()
        }
        coVerify {
            authRepo.getOrFetchConsumer()
            authRepo.exchange(consumer, oauth1)
            authRepo.saveOAuth2(validToken)
        }
        confirmVerified(authRepo)
    }

    @Test
    fun `Success - exchange no oauth2`() = runTest {
        val validToken = createOAuth2(tomorrow)

        every { authRepo.getOAuth2() } returns flowOf(null)
        coEvery { authRepo.getOrFetchConsumer() } returns consumer
        every { authRepo.getOAuth1() } returns flowOf(oauth1)
        coEvery { authRepo.exchange(any(), any()) } returns Result.Success(validToken)
        coEvery { authRepo.saveOAuth2(any()) } returns Unit

        api.test()

        val req = server.takeRequest()
        assertThat(req.headers["Authorization"]).isEqualTo("Bearer ${validToken.accessToken}")

        verify {
            authRepo.getOAuth1()
            authRepo.getOAuth2()
        }
        coVerify {
            authRepo.getOrFetchConsumer()
            authRepo.exchange(consumer, oauth1)
            authRepo.saveOAuth2(validToken)
        }
        confirmVerified(authRepo)
    }

    @Test
    fun `Failure - no consumer`() = runTest {
        every { authRepo.getOAuth2() } returns flowOf(null)
        coEvery { authRepo.getOrFetchConsumer() } returns null

        val res = api.test()

        assertThat(res.isSuccessful).isFalse()

        verify {
            authRepo.getOAuth2()
        }
        coVerify {
            authRepo.getOrFetchConsumer()
        }
        confirmVerified(authRepo)
    }

    @Test
    fun `Failure - no oauth1`() = runTest {
        every { authRepo.getOAuth2() } returns flowOf(null)
        coEvery { authRepo.getOrFetchConsumer() } returns consumer
        every { authRepo.getOAuth1() } returns flowOf(null)

        val res = api.test()

        assertThat(res.isSuccessful).isFalse()

        verify {
            authRepo.getOAuth1()
            authRepo.getOAuth2()
        }
        coVerify {
            authRepo.getOrFetchConsumer()
        }
        confirmVerified(authRepo)
    }


    @Test
    fun `Failure - exchange`() = runTest {
        every { authRepo.getOAuth2() } returns flowOf(null)
        coEvery { authRepo.getOrFetchConsumer() } returns consumer
        every { authRepo.getOAuth1() } returns flowOf(oauth1)
        coEvery { authRepo.exchange(any(), any()) } returns Result.Failure("Couldn't exchange token")

        val res = api.test()

        assertThat(res.isSuccessful).isFalse()

        verify {
            authRepo.getOAuth1()
            authRepo.getOAuth2()
        }
        coVerify {
            authRepo.getOrFetchConsumer()
            authRepo.exchange(consumer, oauth1)
        }
        confirmVerified(authRepo)
    }
}