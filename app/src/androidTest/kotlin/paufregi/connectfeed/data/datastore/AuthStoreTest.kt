package paufregi.connectfeed.data.datastore

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.data.api.garmin.models.AuthToken as GarminAuthToken
import paufregi.connectfeed.data.api.strava.models.AuthToken as StravaAuthToken
import javax.inject.Inject
import kotlin.time.Instant

@HiltAndroidTest
@ExperimentalCoroutinesApi
class AuthStoreTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var dataStore: AuthStore

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun tearDown(): Unit = runBlocking {
        dataStore.clear()
    }

    @Test
    fun `Save retrieve and clear Garmin token`() = runTest {
        val token1 = GarminAuthToken("GARMIN_ACCESS_1", "GARMIN_REFRESH_1")
        val token2 = GarminAuthToken("GARMIN_ACCESS_2", "GARMIN_REFRESH_2")

        dataStore.garminToken.test {
            assertThat(awaitItem()).isNull()
            dataStore.saveGarminToken(token1)
            assertThat(awaitItem()).isEqualTo(token1)
            dataStore.saveGarminToken(token2)
            assertThat(awaitItem()).isEqualTo(token2)
            dataStore.clear()
            assertThat(awaitItem()).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Save retrieve and clear Strava token`() = runTest {
        val token1 = StravaAuthToken(
            accessToken = "STRAVA_ACCESS_1",
            refreshToken = "STRAVA_REFRESH_1",
            expiresAt = Instant.parse("2025-01-01T01:00:00Z")
        )
        val token2 = StravaAuthToken(
            accessToken = "STRAVA_ACCESS_2",
            refreshToken = "STRAVA_REFRESH_2",
            expiresAt = Instant.parse("2025-01-02T01:00:00Z")
        )

        dataStore.stravaToken.test {
            assertThat(awaitItem()).isNull()
            dataStore.saveStravaToken(token1)
            assertThat(awaitItem()).isEqualTo(token1)
            dataStore.saveStravaToken(token2)
            assertThat(awaitItem()).isEqualTo(token2)
            dataStore.clear()
            assertThat(awaitItem()).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Saving one token does not overwrite the other`() = runTest {
        val garminToken = GarminAuthToken("GARMIN_ACCESS", "GARMIN_REFRESH")
        val stravaToken = StravaAuthToken(
            accessToken = "STRAVA_ACCESS",
            refreshToken = "STRAVA_REFRESH",
            expiresAt = Instant.parse("2025-01-03T01:00:00Z")
        )

        dataStore.saveGarminToken(garminToken)
        dataStore.saveStravaToken(stravaToken)

        assertThat(dataStore.garminToken.first()).isEqualTo(garminToken)
        assertThat(dataStore.stravaToken.first()).isEqualTo(stravaToken)
    }
}