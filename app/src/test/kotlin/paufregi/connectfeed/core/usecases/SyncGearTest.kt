package paufregi.connectfeed.core.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.api.strava.models.Athlete
import paufregi.connectfeed.data.api.strava.models.Bike
import paufregi.connectfeed.data.api.strava.models.Shoe
import paufregi.connectfeed.data.repository.AppRepository
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.data.repository.StravaRepository
import paufregi.connectfeed.user
import paufregi.connectfeed.data.api.garmin.models.Gear as GarminGear

class SyncGearTest {

    private val isStravaConnected = mockk<IsStravaConnected>()
    private val authRepo = mockk<AuthRepository>()
    private val garminRepo = mockk<GarminRepository>()
    private val stravaRepo = mockk<StravaRepository>()
    private val appRepo = mockk<AppRepository>()
    private lateinit var useCase: SyncGear

    private val garminGears = listOf(
        GarminGear("id-1", null, null, "bike-1", GearType.Bike, 10000.0),
        GarminGear("id-2", "brand", "shoe", null, GearType.Shoe, 123.0),
        GarminGear("id-3", null, null, "other shoe", GearType.Shoe, 456.0)
    )

    private val stravaAthlete = Athlete(
        id = 1,
        bikes = listOf(Bike("strava-id-1", "bike-1")),
        shoes = listOf(Shoe("strava-id-2", "brand shoe"))
    )

    private val dbGears = listOf(
        Gear("id-1", "bike-1", GearType.Bike, 1000, "strava-id-1"),
        Gear("id-10", "shoes-10", GearType.Shoe, 100)
    )

    @Before
    fun setup() {
        useCase = SyncGear(authRepo, isStravaConnected, garminRepo, stravaRepo, appRepo)
        coEvery { appRepo.saveGear(any(), any()) } returns Unit
        coEvery { appRepo.deleteGear(any(), any()) } returns Unit
    }

    @After
    fun tearDown() {
        confirmVerified(isStravaConnected, garminRepo, stravaRepo, authRepo, appRepo)
        clearAllMocks()
    }

    @Test
    fun `Sync gears`() = runTest {
        every { authRepo.getUser() } returns flowOf(user)
        every { isStravaConnected() } returns flowOf(false)
        every { appRepo.getAllGears(user) } returns flowOf(dbGears)
        coEvery { garminRepo.getGears() } returns Result.success(garminGears)
        coEvery { stravaRepo.getAthlete() } returns Result.success(Athlete(0))

        val res = useCase()

        assertThat(res.isSuccess).isTrue()

        verify { authRepo.getUser() }
        verify { isStravaConnected() }
        coVerify { garminRepo.getGears() }
        coVerify { stravaRepo.getAthlete() }
        verify { appRepo.getAllGears(user) }
        coVerify { appRepo.saveGear(user, match { it.id == "id-1" }) }
        coVerify { appRepo.saveGear(user, match { it.id == "id-2" }) }
        coVerify { appRepo.saveGear(user, match { it.id == "id-3" }) }
        coVerify { appRepo.deleteGear(user, match { it.id == "id-10" }) }
    }

    @Test
    fun `Sync gears with strava`() = runTest {
        every { authRepo.getUser() } returns flowOf(user)
        every { isStravaConnected() } returns flowOf(true)
        every { appRepo.getAllGears(user) } returns flowOf(dbGears)
        coEvery { garminRepo.getGears() } returns Result.success(garminGears)
        coEvery { stravaRepo.getAthlete() } returns Result.success(stravaAthlete)

        val res = useCase()

        assertThat(res.isSuccess).isTrue()

        verify { authRepo.getUser() }
        verify { isStravaConnected() }
        coVerify { garminRepo.getGears() }
        coVerify { stravaRepo.getAthlete() }
        verify { appRepo.getAllGears(user) }
        coVerify { appRepo.saveGear(user, match { it.id == "id-1" && it.stravaId == "strava-id-1" }) }
        coVerify { appRepo.saveGear(user, match { it.id == "id-2" && it.stravaId == null }) }
        coVerify { appRepo.saveGear(user, match { it.id == "id-3" && it.stravaId == null }) }
        coVerify { appRepo.deleteGear(user, match { it.id == "id-10" }) }
    }

    @Test
    fun `Sync gears - empty DB`() = runTest {
        every { authRepo.getUser() } returns flowOf(user)
        every { isStravaConnected() } returns flowOf(true)
        every { appRepo.getAllGears(user) } returns flowOf(emptyList())
        coEvery { garminRepo.getGears() } returns Result.success(garminGears)
        coEvery { stravaRepo.getAthlete() } returns Result.success(stravaAthlete)

        val res = useCase()

        assertThat(res.isSuccess).isTrue()

        verify { authRepo.getUser() }
        verify { isStravaConnected() }
        coVerify { garminRepo.getGears() }
        coVerify { stravaRepo.getAthlete() }
        verify { appRepo.getAllGears(user) }
        coVerify { appRepo.saveGear(user, match { it.id == "id-1" && it.stravaId == "strava-id-1" }) }
        coVerify { appRepo.saveGear(user, match { it.id == "id-2" && it.stravaId == null }) }
        coVerify { appRepo.saveGear(user, match { it.id == "id-3" && it.stravaId == null }) }
    }

    @Test
    fun `Sync gears - strava empty list`() = runTest {
        every { authRepo.getUser() } returns flowOf(user)
        every { isStravaConnected() } returns flowOf(true)
        every { appRepo.getAllGears(user) } returns flowOf(dbGears)
        coEvery { garminRepo.getGears() } returns Result.success(garminGears)
        coEvery { stravaRepo.getAthlete() } returns Result.success(Athlete(1, emptyList(), emptyList()))

        val res = useCase()

        assertThat(res.isSuccess).isTrue()

        verify { authRepo.getUser() }
        verify { isStravaConnected() }
        coVerify { garminRepo.getGears() }
        coVerify { stravaRepo.getAthlete() }
        verify { appRepo.getAllGears(user) }
        coVerify { appRepo.saveGear(user, match { it.id == "id-1" && it.stravaId == null }) }
        coVerify { appRepo.saveGear(user, match { it.id == "id-2" && it.stravaId == null }) }
        coVerify { appRepo.saveGear(user, match { it.id == "id-3" && it.stravaId == null }) }
        coVerify { appRepo.deleteGear(user, match { it.id == "id-10" }) }
    }

    @Test
    fun `Sync gears - garmin empty list`() = runTest {
        every { authRepo.getUser() } returns flowOf(user)
        every { isStravaConnected() } returns flowOf(true)
        every { appRepo.getAllGears(user) } returns flowOf(dbGears)
        coEvery { garminRepo.getGears() } returns Result.success(emptyList())
        coEvery { stravaRepo.getAthlete() } returns Result.success(stravaAthlete)

        val res = useCase()

        assertThat(res.isSuccess).isTrue()

        verify { authRepo.getUser() }
        verify { isStravaConnected() }
        coVerify { garminRepo.getGears() }
        coVerify { stravaRepo.getAthlete() }
        verify { appRepo.getAllGears(user) }
        coVerify { appRepo.deleteGear(user, match { it.id == "id-1" }) }
        coVerify { appRepo.deleteGear(user, match { it.id == "id-10" }) }
    }

    @Test
    fun `Sync gears - garmin failure`() = runTest {
        every { authRepo.getUser() } returns flowOf(user)
        every { isStravaConnected() } returns flowOf(true)
        every { appRepo.getAllGears(user) } returns flowOf(dbGears)
        coEvery { garminRepo.getGears() } returns Result.failure("Failed to load Garmin")
        coEvery { stravaRepo.getAthlete() } returns Result.success(stravaAthlete)

        val res = useCase()

        assertThat(res.isSuccess).isFalse()

        verify { authRepo.getUser() }
        verify { isStravaConnected() }
        coVerify { garminRepo.getGears() }
        coVerify { stravaRepo.getAthlete() }
    }

    @Test
    fun `Sync gears - strava failure`() = runTest {
        every { authRepo.getUser() } returns flowOf(user)
        every { isStravaConnected() } returns flowOf(true)
        every { appRepo.getAllGears(user) } returns flowOf(dbGears)
        coEvery { garminRepo.getGears() } returns Result.success(garminGears)
        coEvery { stravaRepo.getAthlete() } returns Result.failure("Failed to load Strava")

        val res = useCase()

        assertThat(res.isSuccess).isTrue()

        verify { authRepo.getUser() }
        verify { isStravaConnected() }
        coVerify { garminRepo.getGears() }
        coVerify { stravaRepo.getAthlete() }
        verify { appRepo.getAllGears(user) }
        coVerify { appRepo.saveGear(user, match { it.id == "id-1" && it.stravaId == null }) }
        coVerify { appRepo.saveGear(user, match { it.id == "id-2" && it.stravaId == null }) }
        coVerify { appRepo.saveGear(user, match { it.id == "id-3" && it.stravaId == null }) }
        coVerify { appRepo.deleteGear(user, match { it.id == "id-10" }) }
    }

    @Test
    fun `Sync gears - no user`() = runTest {
        every { authRepo.getUser() } returns flowOf(null)
        every { isStravaConnected() } returns flowOf(true)
        coEvery { garminRepo.getGears() } returns Result.success(garminGears)
        coEvery { stravaRepo.getAthlete() } returns Result.success(stravaAthlete)

        val res = useCase()

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()).hasMessageThat().contains("User must be logged in")

        verify { authRepo.getUser() }
    }
}
