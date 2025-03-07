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
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.data.api.strava.models.Token
import paufregi.connectfeed.data.datastore.StravaStore
import paufregi.connectfeed.oauth2
import paufregi.connectfeed.sslSocketFactory
import paufregi.connectfeed.stravaDispatcher
import paufregi.connectfeed.stravaPort
import paufregi.connectfeed.stravaToken
import javax.inject.Inject

@HiltAndroidTest
class StravaAuthRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: StravaAuthRepository

    @Inject
    lateinit var store: StravaStore

    private val stravaServer = MockWebServer()

    @Before
    fun setup() {
        hiltRule.inject()
        stravaServer.useHttps(sslSocketFactory, false)
        stravaServer.start(stravaPort)

        stravaServer.dispatcher = stravaDispatcher
    }

    @After
    fun tearDown() {
        stravaServer.shutdown()
        runBlocking(Dispatchers.IO){
            store.dataStore.edit { it.clear() }
        }
    }

    @Test
    fun `Store token`() = runTest {
        val token1 = Token("ACCESS_TOKEN_1", "REFRESH_TOKEN_1", 3600)
        val token2 = Token("ACCESS_TOKEN_2", "REFRESH_TOKEN_2", 5600)
        repo.getToken().test{
            assertThat(awaitItem()).isNull()
            repo.saveToken(token1)
            assertThat(awaitItem()).isEqualTo(token1)
            repo.saveToken(token2)
            assertThat(awaitItem()).isEqualTo(token2)
            repo.clear()
            assertThat(awaitItem()).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Exchange token`() = runTest {
        val res = repo.exchange("CLIENT_ID", "CLIENT_SECRET", "CODE")

        assertThat(res.isSuccessful).isTrue()
        res as Result.Success
        assertThat(res.data).isEqualTo(Token("ACCESS_TOKEN", "REFRESH_TOKEN", 1704067200))
    }

    @Test
    fun `Refresh token`() = runTest {
        val res = repo.refresh("CLIENT_ID", "CLIENT_SECRET", "REFRESH_TOKEN")

        assertThat(res.isSuccessful).isTrue()
        res as Result.Success
        assertThat(res.data).isEqualTo(Token("NEW_ACCESS_TOKEN", "NEW_REFRESH_TOKEN", 1704067200))
    }
}