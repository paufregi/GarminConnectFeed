package paufregi.connectfeed.data.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.preferences.core.edit
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.authToken
import paufregi.connectfeed.connectDispatcher
import paufregi.connectfeed.connectPort
import paufregi.connectfeed.data.api.garmin.models.AuthToken
import paufregi.connectfeed.data.api.garmin.models.PreAuthToken
import paufregi.connectfeed.data.database.GarminDatabase
import paufregi.connectfeed.data.datastore.AuthStore
import paufregi.connectfeed.garminSSODispatcher
import paufregi.connectfeed.garminSSOPort
import paufregi.connectfeed.preAuthToken
import paufregi.connectfeed.sslSocketFactory
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
    lateinit var authStore: AuthStore

    @Inject
    lateinit var database: GarminDatabase

    private val garminSSOServer = MockWebServer()
    private val connectServer = MockWebServer()

    @Before
    fun setup() {
        hiltRule.inject()
        garminSSOServer.useHttps(sslSocketFactory, false)
        garminSSOServer.start(garminSSOPort)
        connectServer.useHttps(sslSocketFactory, false)
        connectServer.start(connectPort)

        garminSSOServer.dispatcher = garminSSODispatcher
        connectServer.dispatcher = connectDispatcher
    }

    @After
    fun tearDown() {
        garminSSOServer.shutdown()
        connectServer.shutdown()
        database.close()
        runBlocking(Dispatchers.IO){
            authStore.dataStore.edit { it.clear() }
        }
    }

    @Test
    fun `Store PreAuthToken`() = runTest {
        val token1 = PreAuthToken(token = "TOKEN_1", secret = "SECRET_1")
        val token2 = PreAuthToken(token = "TOKEN_2", secret = "SECRET_2")
        repo.getPreAuth().test{
            assertThat(awaitItem()).isNull()
            repo.savePreAuth(token1)
            assertThat(awaitItem()).isEqualTo(token1)
            repo.savePreAuth(token2)
            assertThat(awaitItem()).isEqualTo(token2)
            repo.clear()
            assertThat(awaitItem()).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Store AuthToken`() = runTest {
        val token1 = AuthToken(accessToken = "ACCESS_TOKEN_1", expiresAt = 1)
        val token2 = AuthToken(accessToken = "ACCESS_TOKEN_2", expiresAt = 2)
        repo.getAuthToken().test{
            assertThat(awaitItem()).isNull()
            repo.saveAuthToken(token1)
            assertThat(awaitItem()).isEqualTo(token1)
            repo.saveAuthToken(token2)
            assertThat(awaitItem()).isEqualTo(token2)
            repo.clear()
            assertThat(awaitItem()).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Authorize user`() = runTest {
        val res = repo.authorize("user", "pass")

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(preAuthToken)
    }

    @Test
    fun `Exchange token`() = runTest {
        val res = repo.exchange(preAuthToken)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(authToken)
    }
}