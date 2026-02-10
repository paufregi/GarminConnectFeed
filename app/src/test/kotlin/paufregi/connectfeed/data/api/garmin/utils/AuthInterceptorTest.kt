package paufregi.connectfeed.data.api.garmin.utils

import com.appstractive.jwt.jwt
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
import paufregi.connectfeed.authToken
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.core.utils.toKotlinInstant
import paufregi.connectfeed.createAuthToken
import paufregi.connectfeed.data.api.garmin.interceptors.AuthInterceptor
import paufregi.connectfeed.data.api.garmin.models.AuthToken
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.preAuthToken
import paufregi.connectfeed.today
import paufregi.connectfeed.tomorrow
import paufregi.connectfeed.yesterday
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import java.time.temporal.ChronoUnit

class AuthInterceptorTest {

    private lateinit var auth: AuthInterceptor
    private lateinit var api: TestApi

    private val authRepo = mockk<AuthRepository>()

    @JvmField @Rule val server = MockWebServerRule()

    interface TestApi {
        @GET("/test")
        suspend fun test(): Response<Unit>
    }

    @Before
    fun setup() {
        auth = AuthInterceptor(authRepo)
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
    fun `Success - valid AuthToken`() = runTest {
        val token = createAuthToken(tomorrow)

        every { authRepo.getAuthToken() } returns flowOf(token)

        api.test()

        val req = server.takeRequest()
        assertThat(req.headers["Authorization"]).isEqualTo("Bearer ${token.accessToken}")

        verify { authRepo.getAuthToken() }
    }

    @Test
    fun `Success - exchange - expired AuthToken`() = runTest {
        val expiredToken = createAuthToken(yesterday)
        val validToken = createAuthToken(tomorrow)

        every { authRepo.getAuthToken() } returns flowOf(expiredToken)
        every { authRepo.getPreAuth() } returns flowOf(preAuthToken)
        coEvery { authRepo.exchange(any()) } returns Result.success(validToken)
        coEvery { authRepo.saveAuthToken(any()) } returns Unit

        api.test()

        val req = server.takeRequest()
        assertThat(req.headers["Authorization"]).isEqualTo("Bearer ${validToken.accessToken}")

        verify {
            authRepo.getPreAuth()
            authRepo.getAuthToken()
        }
        coVerify {
            authRepo.exchange(preAuthToken)
            authRepo.saveAuthToken(validToken)
        }
    }

    @Test
    fun `Success - exchange - no AuthToken`() = runTest {
        val validToken = createAuthToken(tomorrow)

        every { authRepo.getAuthToken() } returns flowOf(null)
        every { authRepo.getPreAuth() } returns flowOf(preAuthToken)
        coEvery { authRepo.exchange(any()) } returns Result.success(validToken)
        coEvery { authRepo.saveAuthToken(any()) } returns Unit

        api.test()

        val req = server.takeRequest()
        assertThat(req.headers["Authorization"]).isEqualTo("Bearer ${validToken.accessToken}")

        verify {
            authRepo.getPreAuth()
            authRepo.getAuthToken()
        }
        coVerify {
            authRepo.exchange(preAuthToken)
            authRepo.saveAuthToken(validToken)
        }
    }

    @Test
    fun `Success - refresh`() = runTest {
        val expiredToken = AuthToken(
            accessToken = jwt { claims { issuedAt(today.minus(10, ChronoUnit.SECONDS).toKotlinInstant()) } }.toString(),
            refreshToken = "REFRESH_TOKEN",
            expiresAt = today.minus(10, ChronoUnit.SECONDS),
            refreshExpiresAt = today.plus(1, ChronoUnit.DAYS)
        )
        val validToken = createAuthToken(tomorrow)

        every { authRepo.getAuthToken() } returns flowOf(expiredToken)
        every { authRepo.getPreAuth() } returns flowOf(preAuthToken)
        coEvery { authRepo.refresh(any(), any()) } returns Result.success(validToken)
        coEvery { authRepo.saveAuthToken(any()) } returns Unit

        api.test()

        val req = server.takeRequest()
        assertThat(req.headers["Authorization"]).isEqualTo("Bearer ${validToken.accessToken}")

        verify {
            authRepo.getPreAuth()
            authRepo.getAuthToken()
        }
        coVerify {
            authRepo.refresh(preAuthToken, authToken.refreshToken)
            authRepo.saveAuthToken(validToken)
        }
    }

    @Test
    fun `Success - refresh & exchange`() = runTest {
        val expiredToken = AuthToken(
            accessToken = jwt { claims { issuedAt(today.minus(10, ChronoUnit.SECONDS).toKotlinInstant()) } }.toString(),
            refreshToken = "REFRESH_TOKEN",
            expiresAt = today.minus(10, ChronoUnit.SECONDS),
            refreshExpiresAt = today.plus(1, ChronoUnit.DAYS)
        )
        val validToken = createAuthToken(tomorrow)

        every { authRepo.getAuthToken() } returns flowOf(expiredToken)
        every { authRepo.getPreAuth() } returns flowOf(preAuthToken)
        coEvery { authRepo.refresh(any(), any()) } returns Result.failure("Couldn't refresh token")
        coEvery { authRepo.exchange(any()) } returns Result.success(validToken)
        coEvery { authRepo.saveAuthToken(any()) } returns Unit

        api.test()

        val req = server.takeRequest()
        assertThat(req.headers["Authorization"]).isEqualTo("Bearer ${validToken.accessToken}")

        verify {
            authRepo.getPreAuth()
            authRepo.getAuthToken()
        }
        coVerify {
            authRepo.refresh(preAuthToken, authToken.refreshToken)
            authRepo.exchange(preAuthToken)
            authRepo.saveAuthToken(validToken)
        }
    }

    @Test
    fun `Failure - no PreAuthToken`() = runTest {
        every { authRepo.getAuthToken() } returns flowOf(null)
        every { authRepo.getPreAuth() } returns flowOf(null)

        val res = api.test()

        assertThat(res.isSuccessful).isFalse()

        verify {
            authRepo.getPreAuth()
            authRepo.getAuthToken()
        }
    }

    @Test
    fun `Failure - exchange`() = runTest {
        every { authRepo.getAuthToken() } returns flowOf(null)
        every { authRepo.getPreAuth() } returns flowOf(preAuthToken)
        coEvery { authRepo.exchange(any()) } returns Result.failure("Couldn't exchange token")

        val res = api.test()

        assertThat(res.isSuccessful).isFalse()

        verify {
            authRepo.getPreAuth()
            authRepo.getAuthToken()
        }
        coVerify {
            authRepo.exchange(preAuthToken)
        }
    }

    @Test
    fun `Failure - refresh`() = runTest {
        val expiredToken = AuthToken(
            accessToken = jwt { claims { issuedAt(today.minus(10, ChronoUnit.SECONDS).toKotlinInstant()) } }.toString(),
            refreshToken = "REFRESH_TOKEN",
            expiresAt = today.minus(10, ChronoUnit.SECONDS),
            refreshExpiresAt = today.plus(1, ChronoUnit.DAYS)
        )
        every { authRepo.getAuthToken() } returns flowOf(expiredToken)
        every { authRepo.getPreAuth() } returns flowOf(preAuthToken)
        coEvery { authRepo.refresh(any(), any()) } returns Result.failure("Couldn't refresh token")
        coEvery { authRepo.exchange(any()) } returns Result.failure("Couldn't exchange token")

        val res = api.test()

        assertThat(res.isSuccessful).isFalse()

        verify {
            authRepo.getPreAuth()
            authRepo.getAuthToken()
        }
        coVerify {
            authRepo.refresh(preAuthToken, expiredToken.refreshToken)
            authRepo.exchange(preAuthToken)
        }
    }
}