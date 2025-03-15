package paufregi.connectfeed.data.datastore

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.preferences.core.edit
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.api.garmin.models.OAuth1
import paufregi.connectfeed.data.api.garmin.models.OAuth2
import paufregi.connectfeed.data.api.garmin.models.OAuthConsumer
import paufregi.connectfeed.data.api.strava.models.Token
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
class StravaStoreTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var dataStore: StravaStore

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        runBlocking(Dispatchers.IO) { dataStore.dataStore.edit { it.clear() } }
    }

    @Test
    fun `Save retrieve and delete Token`() = runTest {
        val token1 = Token("ACCESS_TOKEN_1", "REFRESH_TOKEN_1", 3600)
        val token2 = Token("ACCESS_TOKEN_2", "REFRESH_TOKEN_2", 5600)

        dataStore.getToken().test {
            assertThat(awaitItem()).isNull()
            dataStore.saveToken(token1)
            assertThat(awaitItem()).isEqualTo(token1)
            dataStore.saveToken(token2)
            assertThat(awaitItem()).isEqualTo(token2)
            dataStore.clear()
            assertThat(awaitItem()).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }
}