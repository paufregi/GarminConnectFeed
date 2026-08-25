package paufregi.connectfeed.data.database

import android.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.data.database.entities.GearEntity
import paufregi.connectfeed.data.database.entities.ProfileEntity
import paufregi.connectfeed.data.database.entities.StravaGearEntity
import paufregi.connectfeed.data.database.relations.GearWithStravaGear
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
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
            name = "Mizuno Neo Vista",
            type = GearType.Shoe,
            distance = 1234,
        )
        val otherUserGear = GearEntity(
            id = "gear-2",
            userId = 2,
            name = "Giant Contend",
            type = GearType.Bike,
            distance = 5678,
        )

        dao.saveGear(gear)
        assertThat(dao.getGear(gear.id)).isEqualTo(gear)

        dao.saveGear(otherUserGear)
        assertThat(dao.getGear(otherUserGear.id)).isEqualTo(otherUserGear)

        dao.deleteGear(gear)
        assertThat(dao.getGear(gear.id)).isNull()

        dao.getAllGears(1).test {
            assertThat(awaitItem()).isEmpty()
            dao.saveGear(gear)
            assertThat(awaitItem()).containsExactly(gear)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Save delete and retrieve strava gears`() = runTest {
        val stravaGear = StravaGearEntity(
            id = "strava-1",
            stravaAthleteId = 10,
            name = "Mizuno Neo Vista",
            type = GearType.Shoe,
        )
        val otherAthleteGear = StravaGearEntity(
            id = "strava-2",
            stravaAthleteId = 20,
            name = "Giant Contend",
            type = GearType.Bike,
        )

        dao.saveStravaGear(stravaGear)
        assertThat(dao.getStravaGear(stravaGear.id)).isEqualTo(stravaGear)

        dao.saveStravaGear(otherAthleteGear)
        assertThat(dao.getStravaGear(otherAthleteGear.id)).isEqualTo(otherAthleteGear)

        dao.deleteStravaGear(stravaGear)
        assertThat(dao.getStravaGear(stravaGear.id)).isNull()

        dao.getAllStravaGears(10).test {
            assertThat(awaitItem()).isEmpty()
            dao.saveStravaGear(stravaGear)
            assertThat(awaitItem()).containsExactly(stravaGear)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Retrieve gear with linked strava gear`() = runTest {
        val stravaGear = StravaGearEntity(
            id = "strava-1",
            stravaAthleteId = 10,
            name = "Mizuno Neo Vista",
            type = GearType.Shoe,
        )
        val gear = GearEntity(
            id = "gear-1",
            userId = 1,
            name = "Daily Trainer",
            type = GearType.Shoe,
            distance = 1234,
            stravaGearId = stravaGear.id,
        )

        dao.saveStravaGear(stravaGear)
        dao.saveGear(gear)

        assertThat(dao.getGearWithStravaGear(gear.id)).isEqualTo(
            GearWithStravaGear(
                gear = gear,
                stravaGear = stravaGear,
            )
        )
    }

    @Test
    fun `Retrieve gear without linked strava gear`() = runTest {
        val gear = GearEntity(
            id = "gear-1",
            userId = 1,
            name = "Daily Trainer",
            type = GearType.Shoe,
            distance = 1234,
        )

        dao.saveGear(gear)

        assertThat(dao.getGearWithStravaGear(gear.id)).isEqualTo(
            GearWithStravaGear(
                gear = gear,
                stravaGear = null,
            )
        )
    }

    @Test
    fun `Deleting strava gear clears linked garmin gear foreign key`() = runTest {
        val stravaGear = StravaGearEntity(
            id = "strava-1",
            stravaAthleteId = 10,
            name = "Mizuno Neo Vista",
            type = GearType.Shoe,
        )
        val gear = GearEntity(
            id = "gear-1",
            userId = 1,
            name = "Daily Trainer",
            type = GearType.Shoe,
            distance = 1234,
            stravaGearId = stravaGear.id,
        )

        dao.saveStravaGear(stravaGear)
        dao.saveGear(gear)
        dao.deleteStravaGear(stravaGear)

        assertThat(dao.getGear(gear.id)).isEqualTo(gear.copy(stravaGearId = null))
        assertThat(dao.getGearWithStravaGear(gear.id)).isEqualTo(
            GearWithStravaGear(
                gear = gear.copy(stravaGearId = null),
                stravaGear = null,
            )
        )
    }
}