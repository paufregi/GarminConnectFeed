package paufregi.connectfeed.data.api.strava.interceptors

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
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.MockServer
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.createStravaToken
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.tomorrow
import paufregi.connectfeed.yesterday
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET

class StravaAuthInterceptorTest {

    private lateinit var auth: StravaAuthInterceptor
    private lateinit var api: TestApi

    private val authRepo = mockk<AuthRepository>()
    private val clientId = "test-client-id"
    private val clientSecret = "test-client-secret"

    @JvmField @Rule val server = MockServer()

    interface TestApi {
        @GET("/test")
        suspend fun test(): Response<Unit>
    }

    @Before
    fun setup() {

        auth = StravaAuthInterceptor(authRepo, clientId, clientSecret)
        server.enqueue(200)
        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .client(OkHttpClient.Builder().addInterceptor(auth).build())
            .build()
            .create(TestApi::class.java)
    }

    @After
    fun tearDown() {
        confirmVerified(authRepo)
        clearAllMocks()
    }

    @Test
    fun `Success - valid token`() = runTest {
        val token = createStravaToken(tomorrow)

        every { authRepo.getStravaToken() } returns flowOf(token)

        api.test()

        val req = server.takeRequest()
        assertThat(req.headers["Authorization"]).isEqualTo("Bearer ${token.accessToken}")

        verify { authRepo.getStravaToken() }
    }

    @Test
    fun `Success - refresh expired token`() = runTest {
        val expiredToken = createStravaToken(yesterday)
        val validToken = createStravaToken(tomorrow)

        every { authRepo.getStravaToken() } returns flowOf(expiredToken)
        coEvery { authRepo.stravaRefreshToken(clientId, clientSecret, expiredToken.refreshToken) } returns Result.success(validToken)
        coEvery { authRepo.saveStravaToken(validToken) } returns Unit

        api.test()

        val req = server.takeRequest()
        assertThat(req.headers["Authorization"]).isEqualTo("Bearer ${validToken.accessToken}")

        verify { authRepo.getStravaToken() }
        coVerify {
            authRepo.stravaRefreshToken(clientId, clientSecret, expiredToken.refreshToken)
            authRepo.saveStravaToken(validToken)
        }
    }

    @Test
    fun `Failure - no token`() = runTest {
        every { authRepo.getStravaToken() } returns flowOf(null)

        val res = api.test()

        assertThat(res.isSuccessful).isFalse()

        verify { authRepo.getStravaToken() }
    }

    @Test
    fun `Failure - refresh expired token`() = runTest {
        val expiredToken = createStravaToken(yesterday)

        every { authRepo.getStravaToken() } returns flowOf(expiredToken)
        coEvery { authRepo.stravaRefreshToken(clientId, clientSecret, expiredToken.refreshToken) } returns Result.failure("error")

        val res = api.test()

        assertThat(res.isSuccessful).isFalse()

        verify { authRepo.getStravaToken() }
        coVerify { authRepo.stravaRefreshToken(clientId, clientSecret, expiredToken.refreshToken) }
    }
}