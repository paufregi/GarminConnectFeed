package paufregi.connectfeed.data.datastore

import android.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.api.models.OAuth1
import paufregi.connectfeed.data.api.models.OAuth2
import paufregi.connectfeed.data.api.models.OAuthConsumer
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
class UserStoreTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var dataStore: UserStore

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun `Save retrieve and delete User`() = runTest {
        val user1 = User("user_1", "avatar_1")
        val user2 = User("user_2", "avatar_2")
        dataStore.get().test {
            assertThat(awaitItem()).isNull()
            dataStore.save(user1)
            assertThat(awaitItem()).isEqualTo(user1)
            dataStore.save(user2)
            assertThat(awaitItem()).isEqualTo(user2)
            dataStore.delete()
            assertThat(awaitItem()).isNull()
        }
    }
}