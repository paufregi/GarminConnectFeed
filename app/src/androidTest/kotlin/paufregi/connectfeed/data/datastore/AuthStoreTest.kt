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
import paufregi.connectfeed.createAuthToken
import paufregi.connectfeed.data.api.garmin.models.PreAuthToken
import java.time.Instant
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
class AuthStoreTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var dataStore: AuthStore

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        runBlocking(Dispatchers.IO) { dataStore.dataStore.edit { it.clear() } }
    }

    @Test
    fun `Save retrieve and delete PreAuthToken`() = runTest {
        val token1 = PreAuthToken(token = "TOKEN_1", secret = "SECRET_2")
        val token2 = PreAuthToken(token = "TOKEN_2", secret = "SECRET_2")

        dataStore.getPreAuthToken().test {
            assertThat(awaitItem()).isNull()
            dataStore.savePreAuthToken(token1)
            assertThat(awaitItem()).isEqualTo(token1)
            dataStore.savePreAuthToken(token2)
            assertThat(awaitItem()).isEqualTo(token2)
            dataStore.clear()
            assertThat(awaitItem()).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Save retrieve and delete AuthToken`() = runTest {
        val date = Instant.parse("2025-01-01T01:00:00Z")
        val token1 = createAuthToken(date)
        val token2 = createAuthToken(date.plusSeconds(60))

        dataStore.getAuthToken().test {
            assertThat(awaitItem()).isNull()
            dataStore.saveAuthToken(token1)
            assertThat(awaitItem()).isEqualTo(token1)
            dataStore.saveAuthToken(token2)
            assertThat(awaitItem()).isEqualTo(token2)
            dataStore.clear()
            assertThat(awaitItem()).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Save retrieve and delete User`() = runTest {
        val user1 = User("user_1", "avatar_1")
        val user2 = User("user_2", "avatar_2")
        dataStore.getUser().test {
            assertThat(awaitItem()).isNull()
            dataStore.saveUser(user1)
            assertThat(awaitItem()).isEqualTo(user1)
            dataStore.saveUser(user2)
            assertThat(awaitItem()).isEqualTo(user2)
            dataStore.clear()
            assertThat(awaitItem()).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }
}