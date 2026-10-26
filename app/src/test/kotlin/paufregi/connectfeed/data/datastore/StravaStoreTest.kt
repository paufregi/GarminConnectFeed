package paufregi.connectfeed.data.datastore

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import paufregi.connectfeed.FakeAndroidKeyStore
import paufregi.connectfeed.data.api.strava.models.AuthToken
import paufregi.connectfeed.today
import paufregi.connectfeed.tomorrow

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class StravaStoreTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var dataStore: StravaStore

    @Before
    fun setup() {
        FakeAndroidKeyStore.install()
        dataStore = StravaStore(
            PreferenceDataStoreFactory.create(scope = scope) {
                tempFolder.newFile("stravaStore.preferences_pb")
            }
        )
    }

    @After
    fun tearDown() {
        scope.cancel()
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