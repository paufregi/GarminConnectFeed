package paufregi.connectfeed.core.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.api.garmin.models.GearDto
import paufregi.connectfeed.data.api.strava.models.Athlete
import paufregi.connectfeed.data.api.strava.models.SummaryGear
import paufregi.connectfeed.data.repository.AppRepository
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.data.repository.StravaRepository
import paufregi.connectfeed.user

class SyncGearTest {

    private val isStravaConnected = mockk<IsStravaConnected>()
    private val authRepo = mockk<AuthRepository>()
    private val garminRepo = mockk<GarminRepository>()
    private val stravaRepo = mockk<StravaRepository>()
    private val appRepo = mockk<AppRepository>()
    private lateinit var useCase: SyncGear

    private val garminGears = listOf(
        GearDto("id-1", null, null, "bike-1", GearType.Bike, 10000.0),
        GearDto("id-2", "brand", "shoe", null, GearType.Shoe, 123.0),
        GearDto("id-3", null, null, "other shoe", GearType.Shoe, 456.0)
    )

    private val stravaAthlete = Athlete(
        id = 1,
        bikes = listOf(SummaryGear("strava-id-1", "bike-1")),
        shoes = listOf(SummaryGear("strava-id-2", "brand shoe"))
    )

    private val unmatchedGears = listOf(
        Gear("id-1", "bike-1", GearType.Bike, 10000, null),
        Gear("id-2", "brand shoe", GearType.Shoe, 123, null),
        Gear("id-3", "other shoe", GearType.Shoe, 456, null),
    )

    @Before
    fun setup() {
        useCase = SyncGear(authRepo, isStravaConnected, garminRepo, stravaRepo, appRepo)
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
        coEvery { garminRepo.getGears() } returns Result.success(garminGears)
        coEvery { appRepo.replaceGears(any(), any()) } returns Unit

        val res = useCase()

        assertThat(res.isSuccess).isTrue()

        verify { authRepo.getUser() }
        verify { isStravaConnected() }
        coVerify { garminRepo.getGears() }
        coVerify { appRepo.replaceGears(user, unmatchedGears) }
    }

    @Test
    fun `Sync gears with strava`() = runTest {
        every { authRepo.getUser() } returns flowOf(user)
        every { isStravaConnected() } returns flowOf(true)
        coEvery { garminRepo.getGears() } returns Result.success(garminGears)
        coEvery { stravaRepo.getAthlete() } returns Result.success(stravaAthlete)
        coEvery { appRepo.replaceGears(any(), any()) } returns Unit

        val res = useCase()

        assertThat(res.isSuccess).isTrue()

        verify { authRepo.getUser() }
        verify { isStravaConnected() }
        coVerify { garminRepo.getGears() }
        coVerify { stravaRepo.getAthlete() }
        coVerify {
            appRepo.replaceGears(
                user,
                listOf(
                    Gear("id-1", "bike-1", GearType.Bike, 10000, "strava-id-1"),
                    Gear("id-2", "brand shoe", GearType.Shoe, 123, "strava-id-2"),
                    Gear("id-3", "other shoe", GearType.Shoe, 456, null),
                )
            )
        }
    }

    @Test
    fun `Sync gears - strava empty list`() = runTest {
        every { authRepo.getUser() } returns flowOf(user)
        every { isStravaConnected() } returns flowOf(true)
        coEvery { garminRepo.getGears() } returns Result.success(garminGears)
        coEvery { stravaRepo.getAthlete() } returns Result.success(Athlete(1, emptyList(), emptyList()))
        coEvery { appRepo.replaceGears(any(), any()) } returns Unit

        val res = useCase()

        assertThat(res.isSuccess).isTrue()

        verify { authRepo.getUser() }
        verify { isStravaConnected() }
        coVerify { garminRepo.getGears() }
        coVerify { stravaRepo.getAthlete() }
        coVerify { appRepo.replaceGears(user, unmatchedGears) }
    }

    @Test
    fun `Sync gears - garmin empty list`() = runTest {
        every { authRepo.getUser() } returns flowOf(user)
        every { isStravaConnected() } returns flowOf(true)
        coEvery { garminRepo.getGears() } returns Result.success(emptyList())
        coEvery { stravaRepo.getAthlete() } returns Result.success(stravaAthlete)
        coEvery { appRepo.replaceGears(any(), any()) } returns Unit

        val res = useCase()

        assertThat(res.isSuccess).isTrue()

        verify { authRepo.getUser() }
        verify { isStravaConnected() }
        coVerify { garminRepo.getGears() }
        coVerify { stravaRepo.getAthlete() }
        coVerify { appRepo.replaceGears(user, emptyList()) }
    }

    @Test
    fun `Sync gears - garmin failure`() = runTest {
        every { authRepo.getUser() } returns flowOf(user)
        every { isStravaConnected() } returns flowOf(true)
        coEvery { garminRepo.getGears() } returns Result.failure("Failed to load Garmin")
        coEvery { stravaRepo.getAthlete() } returns Result.success(stravaAthlete)

        val res = useCase()

        assertThat(res.isFailure).isTrue()
        assertThat(res.exceptionOrNull()).hasMessageThat().isEqualTo("Failed to load Garmin")

        verify { authRepo.getUser() }
        verify { isStravaConnected() }
        coVerify { garminRepo.getGears() }
        coVerify { stravaRepo.getAthlete() }
        coVerify(exactly = 0) { appRepo.replaceGears(any(), any()) }
    }

    @Test
    fun `Sync gears - strava failure`() = runTest {
        every { authRepo.getUser() } returns flowOf(user)
        every { isStravaConnected() } returns flowOf(true)
        coEvery { garminRepo.getGears() } returns Result.success(garminGears)
        coEvery { stravaRepo.getAthlete() } returns Result.failure("Failed to load Strava")
        coEvery { appRepo.replaceGears(any(), any()) } returns Unit

        val res = useCase()

        assertThat(res.isSuccess).isTrue()

        verify { authRepo.getUser() }
        verify { isStravaConnected() }
        coVerify { garminRepo.getGears() }
        coVerify { stravaRepo.getAthlete() }
        coVerify { appRepo.replaceGears(user, unmatchedGears) }
    }

    @Test
    fun `Sync gears - database failure`() = runTest {
        every { authRepo.getUser() } returns flowOf(user)
        every { isStravaConnected() } returns flowOf(false)
        coEvery { garminRepo.getGears() } returns Result.success(garminGears)
        coEvery { appRepo.replaceGears(any(), any()) } throws IllegalStateException("disk full")

        val res = useCase()

        assertThat(res.isFailure).isTrue()
        assertThat(res.exceptionOrNull()).hasMessageThat().isEqualTo("disk full")

        verify { authRepo.getUser() }
        verify { isStravaConnected() }
        coVerify { garminRepo.getGears() }
        coVerify { appRepo.replaceGears(user, unmatchedGears) }
    }

    @Test
    fun `Sync gears - cancellation is propagated`() = runTest {
        every { authRepo.getUser() } returns flowOf(user)
        every { isStravaConnected() } returns flowOf(false)
        coEvery { garminRepo.getGears() } returns Result.success(garminGears)
        coEvery { appRepo.replaceGears(any(), any()) } throws CancellationException("cancelled")

        val thrown = runCatching { useCase() }.exceptionOrNull()

        assertThat(thrown).isInstanceOf(CancellationException::class.java)

        verify { authRepo.getUser() }
        verify { isStravaConnected() }
        coVerify { garminRepo.getGears() }
        coVerify { appRepo.replaceGears(user, unmatchedGears) }
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

