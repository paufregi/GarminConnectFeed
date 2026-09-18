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
import paufregi.connectfeed.athlete
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.api.strava.models.Athlete
import paufregi.connectfeed.data.api.strava.models.Bike
import paufregi.connectfeed.data.api.strava.models.Shoe
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.data.repository.StravaRepository
import paufregi.connectfeed.data.api.garmin.models.Gear as GarminGear

class SyncGearTest {

    private val isStravaConnected = mockk<IsStravaConnected>()
    private val garminRepo = mockk<GarminRepository>()
    private val stravaRepo = mockk<StravaRepository>()
    private lateinit var useCase: SyncGear

    private val garminGears = listOf(
        GarminGear("id-1", null, null, "bike-1", GearType.Bike, 10000.0),
        GarminGear("id-2", "brand", "shoe", null, GearType.Shoe, 123.0),
        GarminGear("id-3", null, null, "other shoe", GearType.Shoe, 456.0)
    )

    private val stravaAthlete = Athlete(
        id = 1,
        bikes = listOf(
            Bike("strava-id-1", "bike-1")
        ),
        shoes = listOf(
            Shoe("strava-id-2", "brand shoe"),
        )
    )

    @Before
    fun setup() {
        useCase = SyncGear(isStravaConnected, garminRepo, stravaRepo)
    }

    @After
    fun tearDown() {
        confirmVerified(isStravaConnected, garminRepo, stravaRepo)
        clearAllMocks()
    }

    @Test
    fun `Sync gears`() = runTest {
        val expected = listOf(
            Gear("id-1", "bike-1", GearType.Bike, 1000),
            Gear("id-2", "brand shoe", GearType.Shoe, 123),
            Gear("id-3",  "other shoe", GearType.Shoe, 456)
        )

        every { isStravaConnected() } returns flowOf(false)
        coEvery { garminRepo.getGears() } returns Result.success(garminGears)

        val res = useCase()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)

        verify { isStravaConnected() }
        coVerify { garminRepo.getGears() }
    }

    @Test
    fun `Sync gears with strava`() = runTest {
        val expected = listOf(
            Gear("id-1", "bike-1", GearType.Bike, 1000, "strava-id-1"),
            Gear("id-2", "brand shoe", GearType.Shoe, 123, "strava-id-2"),
            Gear("id-3",  "other shoe", GearType.Shoe, 456)
        )

        every { isStravaConnected() } returns flowOf(false)
        coEvery { garminRepo.getGears() } returns Result.success(garminGears)
        coEvery { stravaRepo.getAthlete() } returns Result.success(stravaAthlete)

        val res = useCase()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)

        verify { isStravaConnected() }
        coVerify { garminRepo.getGears() }
        coVerify { stravaRepo.getAthlete() }
    }

    @Test
    fun `Sync gears - garmin failure`() = runTest {
        every { isStravaConnected() } returns flowOf(true)
        coEvery { garminRepo.getGears() } returns Result.failure("Failed to load Garmin")
        coEvery { stravaRepo.getAthlete() } returns Result.success(athlete)

        val res = useCase()

        assertThat(res.isSuccess).isFalse()
        assertThat(res.getOrNull()).isEqualTo(emptyList<Gear>())

        verify { isStravaConnected() }
        coVerify { garminRepo.getGears() }
        coVerify { stravaRepo.getAthlete() }
    }

    @Test
    fun `Sync gears - strava failure`() = runTest {
        val expected = listOf(
            Gear("id-1", "bike-1", GearType.Bike, 1000),
            Gear("id-2", "brand shoe", GearType.Shoe, 123),
            Gear("id-3",  "other shoe", GearType.Shoe, 456)
        )

        every { isStravaConnected() } returns flowOf(false)
        coEvery { garminRepo.getGears() } returns Result.success(garminGears)
        coEvery { stravaRepo.getAthlete() } returns Result.failure("Failed to load Strava")

        val res = useCase()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)

        verify { isStravaConnected() }
        coVerify { garminRepo.getGears() }
        coVerify { stravaRepo.getAthlete() }
    }
}
