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
    fun `Save retrieve and delete OAuthConsumer`() = runTest {
        val consumer1 = OAuthConsumer(key = "KEY_1", secret = "SECRET_1")
        val consumer2 = OAuthConsumer(key = "KEY_2", secret = "SECRET_2")

        dataStore.getConsumer().test {
            assertThat(awaitItem()).isNull()
            dataStore.saveConsumer(consumer1)
            assertThat(awaitItem()).isEqualTo(consumer1)
            dataStore.saveConsumer(consumer2)
            assertThat(awaitItem()).isEqualTo(consumer2)
            dataStore.clear()
            assertThat(awaitItem()).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Save retrieve and delete OAuth1`() = runTest {
        val token1 = OAuth1(token = "TOKEN_1", secret = "SECRET_2")
        val token2 = OAuth1(token = "TOKEN_2", secret = "SECRET_2")

        dataStore.getOAuth1().test {
            assertThat(awaitItem()).isNull()
            dataStore.saveOAuth1(token1)
            assertThat(awaitItem()).isEqualTo(token1)
            dataStore.saveOAuth1(token2)
            assertThat(awaitItem()).isEqualTo(token2)
            dataStore.clear()
            assertThat(awaitItem()).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Save retrieve and delete OAuth2`() = runTest {
        val token1 = OAuth2(accessToken = "ACCESS_TOKEN_1")
        val token2 = OAuth2(accessToken = "ACCESS_TOKEN_2")

        dataStore.getOAuth2().test {
            assertThat(awaitItem()).isNull()
            dataStore.saveOAuth2(token1)
            assertThat(awaitItem()).isEqualTo(token1)
            dataStore.saveOAuth2(token2)
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