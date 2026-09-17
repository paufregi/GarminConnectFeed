package paufregi.connectfeed.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.data.database.GarminDao
import paufregi.connectfeed.data.database.entities.GearEntity
import paufregi.connectfeed.data.database.entities.ProfileEntity
import paufregi.connectfeed.user

class AppRepositoryTest {

    private lateinit var repo: AppRepository
    private val dao = mockk<GarminDao>()

    @Before
    fun setup(){
        repo = AppRepository(dao)
    }

    @After
    fun tearDown(){
        confirmVerified(dao)
        clearAllMocks()
    }

    @Test
    fun `Get all profiles`() = runTest {
        val profiles = listOf(
            Profile(
                id = 1,
                name = "profile 1",
                eventType = EventType.Training,
                type = ActivityType.Cycling,
                course = Course(1, "course 1", 10234.00, ActivityType.Cycling),
                water = 2
            ),
            Profile(
                id = 2,
                name = "profile 2",
                eventType = EventType.Recreation,
                type = ActivityType.Running,
                course = Course(2, "course 2", 15007.00, ActivityType.Running),
            )
        )
        val profileEntities = listOf(
            ProfileEntity(
                id = 1,
                userId = user.id,
                name = "profile 1",
                eventType = EventType.Training,
                type = ActivityType.Cycling,
                course = Course(1, "course 1", 10234.00, ActivityType.Cycling),
                water = 2
            ),
            ProfileEntity(
                id = 2,
                userId = user.id,
                name = "profile 2",
                eventType = EventType.Recreation,
                type = ActivityType.Running,
                course = Course(2, "course 2", 15007.00, ActivityType.Running),
            )
        )

        coEvery { dao.getAllProfiles(any()) } returns flowOf(profileEntities)

        val res = repo.getAllProfiles(user)

        res.test {
            assertThat(awaitItem()).isEqualTo(profiles)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { dao.getAllProfiles(user.id) }
    }

    @Test
    fun `Get all profile - no result`() = runTest {
        coEvery { dao.getProfile(any()) } returns null

        val res = repo.getProfile(1)

        assertThat(res).isNull()

        coVerify { dao.getProfile(1) }
    }

    @Test
    fun `Get all profiles - empty list`() = runTest {
        coEvery { dao.getAllProfiles(any()) } returns flowOf(emptyList<ProfileEntity>())

        val res = repo.getAllProfiles(user)

        res.test {
            assertThat(awaitItem()).isEqualTo(emptyList<Profile>())
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { dao.getAllProfiles(user.id) }
    }

    @Test
    fun `Get profile`() = runTest {
        val profile = Profile(
            id = 1,
            name = "profile",
            eventType = EventType.Training,
            type = ActivityType.Cycling,
            course = Course(1, "course", 10234.00, ActivityType.Cycling),
            water = 2
        )
        val profileEntity = ProfileEntity(
            id = 1,
            userId = user.id,
            name = "profile",
            eventType = EventType.Training,
            type = ActivityType.Cycling,
            course = Course(1, "course", 10234.00, ActivityType.Cycling),
            water = 2
        )

        coEvery { dao.getProfile(any()) } returns profileEntity

        val res = repo.getProfile(1)

        assertThat(res).isEqualTo(profile)

        coVerify { dao.getProfile(1) }
    }

    @Test
    fun `Save profile`() = runTest {
        val profile = Profile(
            name = "profile",
            eventType = EventType.Training,
            type = ActivityType.Cycling,
            course = Course(1, "course", 10234.00, ActivityType.Cycling),
            water = 2
        )

        val profileEntity  = ProfileEntity(
            name = "profile",
            userId = user.id,
            eventType = EventType.Training,
            type = ActivityType.Cycling,
            course = Course(1, "course", 10234.00, ActivityType.Cycling),
            water = 2
        )

        coEvery { dao.saveProfile(any()) } returns Unit

        repo.saveProfile(user, profile)

        coVerify { dao.saveProfile(profileEntity) }
    }

    @Test
    fun `Delete profile`() = runTest {
        val profile = Profile(
            id = 1,
            name = "profile",
            eventType = EventType.Training,
            type = ActivityType.Cycling,
            course = Course(1, "course", 10234.00, ActivityType.Cycling),
        )

        val profileEntity  = ProfileEntity(
            id = 1,
            userId = user.id,
            name = "profile",
            eventType = EventType.Training,
            type = ActivityType.Cycling,
            course = Course(1, "course", 10234.00, ActivityType.Cycling),
        )

        coEvery { dao.deleteProfile(any()) } returns Unit

        repo.deleteProfile(user, profile)

        coVerify { dao.deleteProfile(profileEntity) }
    }

    @Test
    fun `Get all gears`() = runTest {
        val gears = listOf(
            Gear(
                id = "gear-1",
                name = "gear 1",
                type = GearType.Shoe,
                distance = 1000,
                stravaId = null
            ),
            Gear(
                id = "gear-2",
                name = "gear 2",
                type = GearType.Bike,
                distance = 2000,
                stravaId = null
            )
        )
        val gearEntities = listOf(
            GearEntity(
                id = "gear-1",
                userId = user.id,
                name = "gear 1",
                type = GearType.Shoe,
                distance = 1000
            ),
            GearEntity(
                id = "gear-2",
                userId = user.id,
                name = "gear 2",
                type = GearType.Bike,
                distance = 2000
            )
        )

        coEvery { dao.getAllGears(any()) } returns flowOf(gearEntities)

        val res = repo.getAllGears(user)

        res.test {
            assertThat(awaitItem()).isEqualTo(gears)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { dao.getAllGears(user.id) }
    }

    @Test
    fun `Get gear`() = runTest {
        val gear = Gear(
            id = "gear-1",
            name = "gear",
            type = GearType.Shoe,
            distance = 1000,
            stravaId = null
        )
        val gearEntity = GearEntity(
            id = "gear-1",
            userId = user.id,
            name = "gear",
            type = GearType.Shoe,
            distance = 1000,
        )

        coEvery { dao.getGear(any()) } returns gearEntity

        val res = repo.getGear("gear-1")

        assertThat(res).isEqualTo(gear)

        coVerify { dao.getGear("gear-1") }
    }

    @Test
    fun `Get gear - no result`() = runTest {
        coEvery { dao.getGear(any()) } returns null

        val res = repo.getGear("gear-1")

        assertThat(res).isNull()

        coVerify { dao.getGear("gear-1") }
    }

    @Test
    fun `Save gear`() = runTest {
        val gear = Gear(
            id = "gear-1",
            name = "gear",
            type = GearType.Shoe,
            distance = 1000,
            stravaId = null
        )
        val gearEntity = GearEntity(
            id = "gear-1",
            userId = user.id,
            name = "gear",
            type = GearType.Shoe,
            distance = 1000
        )

        coEvery { dao.saveGear(any()) } returns Unit

        repo.saveGear(user, gear)

        coVerify { dao.saveGear(gearEntity) }
    }

    @Test
    fun `Delete gear`() = runTest {
        val gear = Gear(
            id = "gear-1",
            name = "gear",
            type = GearType.Shoe,
            distance = 1000,
            stravaId = null
        )
        val gearEntity = GearEntity(
            id = "gear-1",
            userId = user.id,
            name = "gear",
            type = GearType.Shoe,
            distance = 1000
        )

        coEvery { dao.deleteGear(any()) } returns Unit

        repo.deleteGear(user, gear)

        coVerify { dao.deleteGear(gearEntity) }
    }
}