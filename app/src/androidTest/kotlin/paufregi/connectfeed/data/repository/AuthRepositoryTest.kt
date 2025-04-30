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
import paufregi.connectfeed.connectDispatcher
import paufregi.connectfeed.connectPort
import paufregi.connectfeed.consumer
import paufregi.connectfeed.data.api.garmin.models.OAuth1
import paufregi.connectfeed.data.api.garmin.models.OAuth2
import paufregi.connectfeed.data.database.GarminDatabase
import paufregi.connectfeed.data.datastore.AuthStore
import paufregi.connectfeed.garminSSODispatcher
import paufregi.connectfeed.garminSSOPort
import paufregi.connectfeed.garthDispatcher
import paufregi.connectfeed.garthPort
import paufregi.connectfeed.oauth1
import paufregi.connectfeed.oauth2
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
    private val garthServer = MockWebServer()
    private val connectServer = MockWebServer()

    @Before
    fun setup() {
        hiltRule.inject()
        garthServer.useHttps(sslSocketFactory, false)
        garthServer.start(garthPort)
        garminSSOServer.useHttps(sslSocketFactory, false)
        garminSSOServer.start(garminSSOPort)
        connectServer.useHttps(sslSocketFactory, false)
        connectServer.start(connectPort)

        garthServer.dispatcher = garthDispatcher
        garminSSOServer.dispatcher = garminSSODispatcher
        connectServer.dispatcher = connectDispatcher
    }

    @After
    fun tearDown() {
        garthServer.shutdown()
        garminSSOServer.shutdown()
        connectServer.shutdown()
        database.close()
        runBlocking(Dispatchers.IO){
            authStore.dataStore.edit { it.clear() }
        }
    }

    @Test
    fun `Store oAuth1`() = runTest {
        val token1 = OAuth1(token = "TOKEN_1", secret = "SECRET_1")
        val token2 = OAuth1(token = "TOKEN_2", secret = "SECRET_2")
        repo.getOAuth1().test{
            assertThat(awaitItem()).isNull()
            repo.saveOAuth1(token1)
            assertThat(awaitItem()).isEqualTo(token1)
            repo.saveOAuth1(token2)
            assertThat(awaitItem()).isEqualTo(token2)
            repo.clear()
            assertThat(awaitItem()).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Store oAuth2`() = runTest {
        val token1 = OAuth2(accessToken = "ACCESS_TOKEN_1")
        val token2 = OAuth2(accessToken = "ACCESS_TOKEN_2")
        repo.getOAuth2().test{
            assertThat(awaitItem()).isNull()
            repo.saveOAuth2(token1)
            assertThat(awaitItem()).isEqualTo(token1)
            repo.saveOAuth2(token2)
            assertThat(awaitItem()).isEqualTo(token2)
            repo.clear()
            assertThat(awaitItem()).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Get consumer`() = runTest {
        authStore.saveConsumer(consumer)

        val res = repo.getOrFetchConsumer()
        assertThat(res).isEqualTo(consumer)
    }

    @Test
    fun `Fetch consumer`() = runTest {
        val res = repo.getOrFetchConsumer()
        assertThat(res).isEqualTo(consumer)
    }

    @Test
    fun `Authorize user`() = runTest {
        val res = repo.authorize("user", "pass", consumer)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(oauth1)
    }

    @Test
    fun `Exchange token`() = runTest {
        val res = repo.exchange(consumer, oauth1)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(oauth2)
    }
}