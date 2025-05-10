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
import paufregi.connectfeed.data.api.strava.models.AuthToken
import paufregi.connectfeed.today
import paufregi.connectfeed.tomorrow
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
        val authToken1 = AuthToken("ACCESS_TOKEN_1", "REFRESH_TOKEN_1", today)
        val authToken2 = AuthToken("ACCESS_TOKEN_2", "REFRESH_TOKEN_2", tomorrow)

        dataStore.getToken().test {
            assertThat(awaitItem()).isNull()
            dataStore.saveToken(authToken1)
            assertThat(awaitItem()).isEqualTo(authToken1)
            dataStore.saveToken(authToken2)
            assertThat(awaitItem()).isEqualTo(authToken2)
            dataStore.clear()
            assertThat(awaitItem()).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }
}