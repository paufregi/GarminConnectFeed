package paufregi.connectfeed.data.api.strava.utils

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
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.createStravaToken
import paufregi.connectfeed.data.api.strava.interceptors.StravaAuthInterceptor
import paufregi.connectfeed.data.repository.StravaAuthRepository
import paufregi.connectfeed.tomorrow
import paufregi.connectfeed.yesterday
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class StravaAuthInterceptorTest {

    private lateinit var auth: StravaAuthInterceptor
    private lateinit var api: TestApi

    private val authRepo = mockk<StravaAuthRepository>()

    private val server = MockWebServer()

    interface TestApi {
        @GET("/test")
        suspend fun test(): Response<Unit>
    }

    @Before
    fun setup() {
        server.start()

        auth = StravaAuthInterceptor(authRepo, "CLIENT_ID", "CLIENT_SECRET")
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
    fun `Success - cached token`() = runTest {
        val token = createStravaToken(tomorrow)

        every { authRepo.getToken() } returns flowOf(token)

        api.test()

        val req = server.takeRequest()
        assertThat(req.headers["Authorization"]).isEqualTo("Bearer ${token.accessToken}")

        verify { authRepo.getToken() }
        confirmVerified(authRepo)
    }

    @Test
    fun `Success - expired token`() = runTest {
        val expiredToken = createStravaToken(yesterday)
        val validToken = createStravaToken(yesterday)

        every { authRepo.getToken() } returns flowOf(expiredToken)
        coEvery { authRepo.refresh(any(), any(), any()) } returns Result.Success(validToken)
        coEvery { authRepo.saveToken(any()) } returns Unit

        api.test()

        val req = server.takeRequest()
        assertThat(req.headers["Authorization"]).isEqualTo("Bearer ${validToken.accessToken}")

        verify { authRepo.getToken() }
        coVerify {
            authRepo.refresh("CLIENT_ID", "CLIENT_SECRET", expiredToken.refreshToken)
            authRepo.saveToken(validToken)
        }
        confirmVerified(authRepo)
    }

    @Test
    fun `Failure - no token`() = runTest {
        every { authRepo.getToken() } returns flowOf(null)

        val res = api.test()

        assertThat(res.isSuccessful).isFalse()

        verify { authRepo.getToken() }
        confirmVerified(authRepo)
    }

    @Test
    fun `Failure - refresh`() = runTest {
        val expiredToken = createStravaToken(yesterday)

        every { authRepo.getToken() } returns flowOf(expiredToken)
        coEvery { authRepo.refresh(any(), any(), any()) } returns Result.Failure("error")

        val res = api.test()

        assertThat(res.isSuccessful).isFalse()


        verify { authRepo.getToken() }
        coVerify { authRepo.refresh("CLIENT_ID", "CLIENT_SECRET", expiredToken.refreshToken) }
        confirmVerified(authRepo)
    }
}