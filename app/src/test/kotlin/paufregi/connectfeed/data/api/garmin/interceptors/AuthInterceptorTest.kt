package paufregi.connectfeed.data.api.garmin.interceptors

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
import paufregi.connectfeed.MockWebServerRule
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.createAuthToken
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.today
import paufregi.connectfeed.tomorrow
import paufregi.connectfeed.yesterday
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET

class AuthInterceptorTest {

    private lateinit var auth: AuthInterceptor
    private lateinit var api: TestApi

    private val authRepo = mockk<AuthRepository>()
    private val clientId = "test-client-id"

    @JvmField @Rule val server = MockWebServerRule()

    interface TestApi {
        @GET("/test")
        suspend fun test(): Response<Unit>
    }

    @Before
    fun setup() {
        auth = AuthInterceptor(authRepo, clientId)
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
        val token = createAuthToken(tomorrow)

        every { authRepo.getGarminToken() } returns flowOf(token)

        api.test()

        val req = server.takeRequest()
        assertThat(req.headers["Authorization"]).isEqualTo("Bearer ${token.accessToken}")

        verify { authRepo.getGarminToken() }
    }

    @Test
    fun `Success - refresh expired token`() = runTest {
        val expiredToken = createAuthToken(yesterday)
        val validToken = createAuthToken(tomorrow)

        every { authRepo.getGarminToken() } returns flowOf(expiredToken)
        coEvery { authRepo.garminRefreshToken(clientId, expiredToken.refreshToken) } returns Result.success(validToken)
        coEvery { authRepo.saveGarminToken(validToken) } returns Unit

        api.test()

        val req = server.takeRequest()
        assertThat(req.headers["Authorization"]).isEqualTo("Bearer ${validToken.accessToken}")

        verify { authRepo.getGarminToken() }
        coVerify {
            authRepo.garminRefreshToken(clientId, expiredToken.refreshToken)
            authRepo.saveGarminToken(validToken)
        }
    }

    @Test
    fun `Failure - no token found`() = runTest {
        every { authRepo.getGarminToken() } returns flowOf(null)

        val res = api.test()

        assertThat(res.isSuccessful).isFalse()

        verify { authRepo.getGarminToken() }
    }

    @Test
    fun `Failure - refresh expired token`() = runTest {
        val expiredToken = createAuthToken(today)

        every { authRepo.getGarminToken() } returns flowOf(expiredToken)
        coEvery { authRepo.garminRefreshToken(clientId, expiredToken.refreshToken) } returns Result.failure("Refresh failed")

        val res = api.test()

        assertThat(res.isSuccessful).isFalse()

        verify { authRepo.getGarminToken() }
        coVerify {
            authRepo.garminRefreshToken(clientId, expiredToken.refreshToken)
        }
    }
}