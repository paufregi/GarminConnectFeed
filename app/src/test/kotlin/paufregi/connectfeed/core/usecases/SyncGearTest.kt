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
import paufregi.connectfeed.data.api.garmin.models.GearDto
import paufregi.connectfeed.data.api.strava.models.AthleteDto
import paufregi.connectfeed.data.api.strava.models.SummaryGearDto
import paufregi.connectfeed.data.repository.AppRepository
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.data.repository.StravaRepository
import paufregi.connectfeed.user

class SyncGearTest {

    private val auth = mockk<AuthRepository>()
    private val appRepo = mockk<AppRepository>()
    private val garminRepo = mockk<GarminRepository>()
    private val stravaRepo = mockk<StravaRepository>()
    private val isStravaConnected = mockk<IsStravaConnected>()
    private lateinit var useCase: SyncGear

    @Before
    fun setup() {
        useCase = SyncGear(auth, appRepo, garminRepo, stravaRepo, isStravaConnected)
    }

    @After
    fun tearDown() {
        confirmVerified(auth, appRepo, garminRepo, stravaRepo, isStravaConnected)
        clearAllMocks()
    }

    @Test
    fun `Sync gears`() = runTest {
        val apiGears = listOf(
            GearDto(
                id = "gear-1",
                brand = "Brand",
                model = "Model 1",
                name = "gear 1",
                type = GearType.Shoe,
                distance = 1000.0
            ),
            GearDto(
                id = "gear-2",
                brand = "Brand",
                model = "Model 2",
                name = "gear 2",
                type = GearType.Bike,
                distance = 2000.0
            )
        )
        val expectedGears = listOf(
            Gear(id = "gear-1", name = "gear 1", type = GearType.Shoe, distance = 1000),
            Gear(id = "gear-2", name = "gear 2", type = GearType.Bike, distance = 2000)
        )
        val dbGears = listOf(
            Gear(
                id = "gear-1",
                name = "gear 1 old",
                type = GearType.Unknown,
                distance = null
            ),
            Gear(
                id = "gear-3",
                name = "gear 3",
                type = GearType.Shoe,
                distance = 3000
            )
        )

        every { auth.getUser() } returns flowOf(user)
        every { isStravaConnected.invoke() } returns flowOf(false)
        coEvery { garminRepo.getGears() } returns Result.success(apiGears)
        every { appRepo.getAllGears(user) } returns flowOf(dbGears)
        coEvery { appRepo.saveGear(any(), any()) } returns Unit
        coEvery { appRepo.deleteGear(any(), any()) } returns Unit

        val res = useCase()

        assertThat(res.isSuccess).isTrue()

        verify { auth.getUser() }
        coVerify { garminRepo.getGears() }
        verify { isStravaConnected.invoke() }
        verify { appRepo.getAllGears(user) }
        coVerify {
            appRepo.deleteGear(user, dbGears[1])
            appRepo.saveGear(user, expectedGears[0])
            appRepo.saveGear(user, expectedGears[1])
        }
    }

    @Test
    fun `Sync gears with strava`() = runTest {
        val apiGears = listOf(
            GearDto(
                id = "gear-1",
                brand = "Brand",
                model = "Shoe",
                name = "Daily Shoe",
                type = GearType.Shoe,
                distance = 1000.0
            ),
            GearDto(
                id = "gear-2",
                brand = "Brand",
                model = "Bike",
                name = "Road Bike",
                type = GearType.Bike,
                distance = 2000.0
            )
        )
        val stravaAthlete = AthleteDto(
            id = 1,
            bikes = listOf(SummaryGearDto(id = "b123", name = "brand bike")),
            shoes = listOf(SummaryGearDto(id = "s123", name = "DAILY SHOE"))
        )
        val expectedGears = listOf(
            Gear(id = "gear-1", name = "Daily Shoe", type = GearType.Shoe, distance = 1000, stravaId = "s123"),
            Gear(id = "gear-2", name = "Road Bike", type = GearType.Bike, distance = 2000, stravaId = "b123")
        )

        every { auth.getUser() } returns flowOf(user)
        every { isStravaConnected.invoke() } returns flowOf(true)
        coEvery { garminRepo.getGears() } returns Result.success(apiGears)
        coEvery { stravaRepo.getAthlete() } returns Result.success(stravaAthlete)
        every { appRepo.getAllGears(user) } returns flowOf(emptyList())
        coEvery { appRepo.saveGear(any(), any()) } returns Unit
        coEvery { appRepo.deleteGear(any(), any()) } returns Unit

        val res = useCase()

        assertThat(res.isSuccess).isTrue()

        verify { auth.getUser() }
        verify { isStravaConnected.invoke() }
        coVerify { garminRepo.getGears() }
        coVerify { stravaRepo.getAthlete() }
        verify { appRepo.getAllGears(user) }
        coVerify {
            appRepo.saveGear(user, expectedGears[0])
            appRepo.saveGear(user, expectedGears[1])
        }
    }

    @Test
    fun `Sync gears - garmin empty list`() = runTest {
        val dbGears = listOf(
            Gear(id = "gear-1", name = "gear 1", type = GearType.Shoe, distance = 1000),
            Gear(id = "gear-2", name = "gear 2", type = GearType.Bike, distance = 2000)
        )

        every { auth.getUser() } returns flowOf(user)
        every { isStravaConnected.invoke() } returns flowOf(false)
        coEvery { garminRepo.getGears() } returns Result.success(emptyList())
        every { appRepo.getAllGears(user) } returns flowOf(dbGears)
        coEvery { appRepo.saveGear(any(), any()) } returns Unit
        coEvery { appRepo.deleteGear(any(), any()) } returns Unit

        val res = useCase()

        assertThat(res.isSuccess).isTrue()

        verify { auth.getUser() }
        verify { isStravaConnected.invoke() }
        coVerify { garminRepo.getGears() }
        coVerify(exactly = 2) { appRepo.deleteGear(user, any()) }
        verify { appRepo.getAllGears(user) }
    }

    @Test
    fun `Sync gears - strava empty list`() = runTest {
        val apiGear = GearDto(
            id = "gear-1",
            brand = "Brand",
            model = "Shoe",
            name = "Daily Shoe",
            type = GearType.Shoe,
            distance = 1000.0
        )
        val expectedGear = Gear(
            id = "gear-1",
            name = "Daily Shoe",
            type = GearType.Shoe,
            distance = 1000
        )

        every { auth.getUser() } returns flowOf(user)
        every { isStravaConnected.invoke() } returns flowOf(true)
        coEvery { garminRepo.getGears() } returns Result.success(listOf(apiGear))
        coEvery { stravaRepo.getAthlete() } returns Result.success(AthleteDto(id = 1))
        every { appRepo.getAllGears(user) } returns flowOf(emptyList())
        coEvery { appRepo.saveGear(any(), any()) } returns Unit

        val res = useCase()

        assertThat(res.isSuccess).isTrue()

        verify { auth.getUser() }
        verify { isStravaConnected.invoke() }
        coVerify { garminRepo.getGears() }
        coVerify { stravaRepo.getAthlete() }
        verify { appRepo.getAllGears(user) }
        coVerify { appRepo.saveGear(user, expectedGear) }
    }

    @Test
    fun `Sync gears - no user`() = runTest {
        every { auth.getUser() } returns flowOf(null)

        val res = useCase()

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("User must be logged in")

        verify { auth.getUser() }
    }

    @Test
    fun `Sync gears - garmin failure`() = runTest {
        every { auth.getUser() } returns flowOf(user)
        every { isStravaConnected.invoke() } returns flowOf(false)
        coEvery { garminRepo.getGears() } returns Result.failure(Exception("error"))

        val res = useCase()

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("error")

        verify { auth.getUser() }
        verify { isStravaConnected.invoke() }
        coVerify { garminRepo.getGears() }
    }

    @Test
    fun `Sync gears - strava failure`() = runTest {
        val apiGear = GearDto(
            id = "gear-1",
            brand = "Brand",
            model = "Shoe",
            name = "Daily Shoe",
            type = GearType.Shoe,
            distance = 1000.0
        )
        val expectedGear = Gear(
            id = "gear-1",
            name = "Daily Shoe",
            type = GearType.Shoe,
            distance = 1000
        )

        every { auth.getUser() } returns flowOf(user)
        every { isStravaConnected.invoke() } returns flowOf(true)
        coEvery { garminRepo.getGears() } returns Result.success(listOf(apiGear))
        coEvery { stravaRepo.getAthlete() } returns Result.failure(Exception("error"))
        every { appRepo.getAllGears(user) } returns flowOf(emptyList())
        coEvery { appRepo.saveGear(any(), any()) } returns Unit

        val res = useCase()

        assertThat(res.isSuccess).isTrue()

        verify { auth.getUser() }
        verify { isStravaConnected.invoke() }
        coVerify { garminRepo.getGears() }
        coVerify { stravaRepo.getAthlete() }
        verify { appRepo.getAllGears(user) }
        coVerify { appRepo.saveGear(user, expectedGear) }
    }
}
