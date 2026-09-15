package paufregi.connectfeed.data.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.MockServer
import paufregi.connectfeed.authToken
import paufregi.connectfeed.createAuthToken
import paufregi.connectfeed.data.database.GarminDatabase
import paufregi.connectfeed.garminAuthDispatcher
import paufregi.connectfeed.garminAuthPort
import paufregi.connectfeed.garminSSODispatcher
import paufregi.connectfeed.garminSSOPort
import paufregi.connectfeed.refreshedToken
import paufregi.connectfeed.stravaAuthToken
import paufregi.connectfeed.stravaDispatcher
import paufregi.connectfeed.stravaPort
import paufregi.connectfeed.stravaRefreshedAuthToken
import paufregi.connectfeed.today
import paufregi.connectfeed.tomorrow
import javax.inject.Inject

@HiltAndroidTest
class AuthRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: AuthRepository

    @Inject
    lateinit var database: GarminDatabase

    @JvmField @Rule val garminSSOServer = MockServer.createSecure(garminSSOPort, garminSSODispatcher)
    @JvmField @Rule val garminAuthServer = MockServer.createSecure(garminAuthPort, garminAuthDispatcher)
    @JvmField @Rule val stravaAuthServer = MockServer.createSecure(stravaPort, stravaDispatcher)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `Store Garmin token`() = runTest {
        val token1 = createAuthToken(today)
        val token2 = createAuthToken(tomorrow)

        repo.getGarminToken().test {
            assertThat(awaitItem()).isNull()
            repo.saveGarminToken(token1)
            assertThat(awaitItem()).isEqualTo(token1)
            repo.saveGarminToken(token2)
            assertThat(awaitItem()).isEqualTo(token2)
            repo.clear()
            assertThat(awaitItem()).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Store Strava token`() = runTest {
        repo.getStravaToken().test {
            assertThat(awaitItem()).isNull()
            repo.saveStravaToken(stravaAuthToken)
            assertThat(awaitItem()).isEqualTo(stravaAuthToken)
            repo.clearStravaToken()
            assertThat(awaitItem()).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Garmin login`() = runTest {
        val res = repo.garminLogin("user", "password")

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isNotEmpty()
    }

    @Test
    fun `Garmin exchange token`() = runTest {
        val res = repo.garminExchangeToken("TICKET", "CLIENT_ID")

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(authToken)
    }

    @Test
    fun `Garmin refresh token`() = runTest {
        val res = repo.garminRefreshToken(authToken.refreshToken, "CLIENT_ID")

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(refreshedToken)
    }

    @Test
    fun `Strava exchange token`() = runTest {
        val res = repo.stravaExchangeToken("CLIENT_ID", "CLIENT_SECRET", "CODE")

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(stravaAuthToken)
    }

    @Test
    fun `Strava refresh token`() = runTest {
        val res = repo.stravaRefreshToken("CLIENT_ID", "CLIENT_SECRET", stravaAuthToken.refreshToken)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(stravaRefreshedAuthToken)
    }

    @Test
    fun `Clear strava auth data`() = runTest {
        repo.saveGarminToken(authToken)
        repo.saveStravaToken(stravaAuthToken)

        assertThat(repo.getGarminToken().firstOrNull()).isEqualTo(authToken)
        assertThat(repo.getStravaToken().firstOrNull()).isEqualTo(stravaAuthToken)

        repo.clearStravaToken()

        assertThat(repo.getGarminToken().firstOrNull()).isEqualTo(authToken)
        assertThat(repo.getStravaToken().firstOrNull()).isNull()
    }

    @Test
    fun `Clear all auth data`() = runTest {
        repo.saveGarminToken(authToken)
        repo.saveStravaToken(stravaAuthToken)

        assertThat(repo.getGarminToken().firstOrNull()).isEqualTo(authToken)
        assertThat(repo.getStravaToken().firstOrNull()).isEqualTo(stravaAuthToken)

        repo.clear()

        assertThat(repo.getGarminToken().firstOrNull()).isNull()
        assertThat(repo.getStravaToken().firstOrNull()).isNull()
    }
}