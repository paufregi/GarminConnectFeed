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
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.MockWebServerRule
import paufregi.connectfeed.data.api.strava.models.AuthToken
import paufregi.connectfeed.data.datastore.StravaStore
import paufregi.connectfeed.sslSocketFactory
import paufregi.connectfeed.stravaAuthToken
import paufregi.connectfeed.stravaDispatcher
import paufregi.connectfeed.stravaPort
import paufregi.connectfeed.stravaRefreshedAuthToken
import paufregi.connectfeed.today
import paufregi.connectfeed.tomorrow
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

    @JvmField @Rule val stravaServer = MockWebServerRule(stravaPort, sslSocketFactory, stravaDispatcher)

    @Before
    fun setup() {
        hiltRule.inject()
        runBlocking(Dispatchers.IO){
            store.dataStore.edit { it.clear() }
        }
    }

    @After
    fun tearDown() {
        runBlocking(Dispatchers.IO){
            store.dataStore.edit { it.clear() }
        }
    }

    @Test
    fun `Store token`() = runTest {
        val authToken1 = AuthToken("ACCESS_TOKEN_1", "REFRESH_TOKEN_1", today)
        val authToken2 = AuthToken("ACCESS_TOKEN_2", "REFRESH_TOKEN_2", tomorrow)
        repo.getToken().test{
            assertThat(awaitItem()).isNull()
            repo.saveToken(authToken1)
            assertThat(awaitItem()).isEqualTo(authToken1)
            repo.saveToken(authToken2)
            assertThat(awaitItem()).isEqualTo(authToken2)
            repo.clear()
            assertThat(awaitItem()).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Exchange token`() = runTest {
        val res = repo.exchange("CLIENT_ID", "CLIENT_SECRET", "CODE")

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(stravaAuthToken)
    }

    @Test
    fun `Refresh token`() = runTest {
        val res = repo.refresh("CLIENT_ID", "CLIENT_SECRET", "REFRESH_TOKEN")

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(stravaRefreshedAuthToken)
    }
}