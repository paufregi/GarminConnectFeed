package paufregi.connectfeed.data.database

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.data.database.entities.GearEntity
import paufregi.connectfeed.data.database.entities.ProfileEntity
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(application = HiltTestApplication::class)
class GarminDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: GarminDatabase

    @Inject
    lateinit var dao: GarminDao

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `Save delete and retrieve profiles`() = runTest {
        val profile = ProfileEntity(
            id = 1,
            userId = 1,
            name = "profile1",
            eventType = EventType.Training,
            type = ActivityType.Running,
            course = Course(id = 1, name = "course1", distance = 1000.50, type = ActivityType.Running),
            water = 100,
            rename = true,
            customWater = true,
             gear = true,
            feelAndEffort = true,
            trainingEffect = true
        )
        val profile2 = ProfileEntity(
            id = 2,
            userId = 2,
            name = "profile2",
            eventType = EventType.Training,
            type = ActivityType.Running,
            course = Course(id = 1, name = "course2", distance = 2000.50, type = ActivityType.Running),
            water = 200,
            rename = true,
            customWater = true,
            gear = true,
            feelAndEffort = true,
            trainingEffect = true
        )

        dao.saveProfile(profile)
        assertThat(dao.getProfile(profile.id)).isEqualTo(profile)

        dao.saveProfile(profile2)
        assertThat(dao.getProfile(profile2.id)).isEqualTo(profile2)

        dao.deleteProfile(profile)
        assertThat(dao.getProfile(profile.id)).isNull()

        dao.getAllProfiles(1).test {
            assertThat(awaitItem()).isEmpty()
            dao.saveProfile(profile)
            assertThat(awaitItem()).containsExactly(profile)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Save delete and retrieve gears`() = runTest {
        val gear = GearEntity(
            id = "gear-1",
            userId = 1,
            name = "gear1",
            type = GearType.Shoe,
            distance = 1000,
            stravaId = "strava-gear-1"
        )
        val gear2 = GearEntity(
            id = "gear-2",
            userId = 2,
            name = "gear2",
            type = GearType.Bike,
            distance = 2000,
            stravaId = "strava-gear-2"
        )

        dao.saveGear(gear)
        dao.saveGear(gear2)

        dao.getAllGears(gear.userId).test {
            assertThat(awaitItem()).containsExactly(gear)
            dao.deleteGear(gear)
            assertThat(awaitItem()).isEmpty()

            cancelAndIgnoreRemainingEvents()
        }

        dao.getAllGears(gear2.userId).test {
            assertThat(awaitItem()).containsExactly(gear2)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Replace gears - only affects the given user`() = runTest {
        val kept = GearEntity(id = "gear-1", userId = 1, name = "gear1", type = GearType.Shoe, distance = 1000)
        val removed = GearEntity(id = "gear-2", userId = 1, name = "gear2", type = GearType.Bike, distance = 2000)
        val otherUser = GearEntity(id = "gear-3", userId = 2, name = "gear3", type = GearType.Bike, distance = 3000)
        val updated = kept.copy(distance = 1500, stravaId = "strava-1")
        val added = GearEntity(id = "gear-4", userId = 1, name = "gear4", type = GearType.Shoe, distance = 10)

        dao.saveGear(kept)
        dao.saveGear(removed)
        dao.saveGear(otherUser)

        dao.getAllGears(1).test {
            assertThat(awaitItem()).containsExactly(removed, kept)

            dao.replaceGears(1, listOf(updated, added))
            assertThat(awaitItem()).containsExactly(updated, added)

            cancelAndIgnoreRemainingEvents()
        }

        dao.getAllGears(2).test {
            assertThat(awaitItem()).containsExactly(otherUser)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Replace gears - empty list clears the user`() = runTest {
        val gear = GearEntity(id = "gear-1", userId = 1, name = "gear1", type = GearType.Shoe, distance = 1000)

        dao.saveGear(gear)

        dao.getAllGears(1).test {
            assertThat(awaitItem()).containsExactly(gear)

            dao.replaceGears(1, emptyList())
            assertThat(awaitItem()).isEmpty()

            cancelAndIgnoreRemainingEvents()
        }
    }
}