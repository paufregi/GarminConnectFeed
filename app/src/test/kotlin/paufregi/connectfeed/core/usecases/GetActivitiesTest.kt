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
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.api.strava.models.SportType
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.data.repository.StravaRepository
import java.time.LocalDateTime
import paufregi.connectfeed.data.api.garmin.models.Activity as GarminActivity
import paufregi.connectfeed.data.api.garmin.models.ActivityType as GarminActivityType
import paufregi.connectfeed.data.api.strava.models.Activity as StravaActivity

class GetActivitiesTest{
    private val isStravaConnected = mockk<IsStravaConnected>()
    private val garminRepo = mockk<GarminRepository>()
    private val stravaRepo = mockk<StravaRepository>()
    private lateinit var useCase: GetActivities

    private val garminActivities = listOf(
        GarminActivity(
            id = 1,
            name = "Morning Run",
            type = GarminActivityType(1, "running", ActivityType.Running),
            eventType = EventType.Training,
            distance = 10_234.0,
            trainingEffectLabel = "Base",
            startDateTime = LocalDateTime.of(2024, 6, 1, 8, 0, 0),
            workoutId = 101,
        ),
        GarminActivity(
            id = 2,
            name = "Evening Ride",
            type = GarminActivityType(10, "road_biking", ActivityType.RoadBiking),
            eventType = EventType.Recreation,
            distance = 24_120.0,
            trainingEffectLabel = null,
            startDateTime = LocalDateTime.of(2024, 6, 1, 18, 30, 0),
            workoutId = null,
        ),
    )
    private val stravaActivities = listOf(
        StravaActivity(
            id = 11,
            name = "Lunch Ride on Strava",
            sportType = SportType.Ride,
            distance = 41_020.0,
            startDateTime = LocalDateTime.of(2024, 6, 2, 12, 0, 30),
        ),
        StravaActivity(
            id = 12,
            name = "Morning Run",
            sportType = SportType.Run,
            distance = 10_234.0,
            startDateTime = LocalDateTime.of(2024, 6, 1, 8, 0, 1),
        ),
    )

    @Before
    fun setup(){
        useCase = GetActivities(isStravaConnected, garminRepo, stravaRepo)
    }

    @After
    fun tearDown(){
        confirmVerified(isStravaConnected, garminRepo, stravaRepo)
        clearAllMocks()
    }

    @Test
    fun `Get activities`() = runTest {
        val expected = listOf(
            Activity(
                id = 1,
                name = "Morning Run",
                type = ActivityType.Running,
                eventType = EventType.Training,
                distance = 10_234.0,
                trainingEffect = "Base",
                date = LocalDateTime.of(2024, 6, 1, 8, 0, 0),
                workoutId = 101,
                stravaId = null,
            ),
            Activity(
                id = 2,
                name = "Evening Ride",
                type = ActivityType.RoadBiking,
                eventType = EventType.Recreation,
                distance = 24_120.0,
                trainingEffect = null,
                date = LocalDateTime.of(2024, 6, 1, 18, 30, 0),
                workoutId = null,
                stravaId = null,
            ),
        )
        every { isStravaConnected() } returns flowOf(false)
        coEvery { garminRepo.getActivities(10) } returns Result.success(garminActivities)

        val res = useCase()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)

        verify { isStravaConnected() }
        coVerify { garminRepo.getActivities(10) }
    }

    @Test
    fun `Get activities with strava`() = runTest {
        val expected = listOf(
            Activity(
                id = 1,
                name = "Morning Run",
                type = ActivityType.Running,
                eventType = EventType.Training,
                distance = 10_234.0,
                trainingEffect = "Base",
                date = LocalDateTime.of(2024, 6, 1, 8, 0, 0),
                workoutId = 101,
                stravaId = 12,
            ),
            Activity(
                id = 2,
                name = "Evening Ride",
                type = ActivityType.RoadBiking,
                eventType = EventType.Recreation,
                distance = 24_120.0,
                trainingEffect = null,
                date = LocalDateTime.of(2024, 6, 1, 18, 30, 0),
                workoutId = null,
                stravaId = null,
            ),
        )

        every { isStravaConnected() } returns flowOf(true)
        coEvery { garminRepo.getActivities(10) } returns Result.success(garminActivities)
        coEvery { stravaRepo.getActivities(15) } returns Result.success(stravaActivities)

        val res = useCase()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)

        verify { isStravaConnected() }
        coVerify { garminRepo.getActivities(10) }
        coVerify { stravaRepo.getActivities(15) }

    }

    @Test
    fun `Get activities - garmin failure`() = runTest {
        every { isStravaConnected() } returns flowOf(true)
        coEvery { garminRepo.getActivities(10) } returns Result.failure("Failed to load Garmin")
        coEvery { stravaRepo.getActivities(15) } returns Result.success(emptyList())

        val res = useCase()

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Failed to load Garmin")

        verify { isStravaConnected() }
        coVerify { garminRepo.getActivities(10) }
        coVerify { stravaRepo.getActivities(15) }

    }

    @Test
    fun `Get activities - strava failure`() = runTest {
        val expected = listOf(
            Activity(
                id = 1,
                name = "Morning Run",
                type = ActivityType.Running,
                eventType = EventType.Training,
                distance = 10_234.0,
                trainingEffect = "Base",
                date = LocalDateTime.of(2024, 6, 1, 8, 0, 0),
                workoutId = 101,
                stravaId = null,
            ),
            Activity(
                id = 2,
                name = "Evening Ride",
                type = ActivityType.RoadBiking,
                eventType = EventType.Recreation,
                distance = 24_120.0,
                trainingEffect = null,
                date = LocalDateTime.of(2024, 6, 1, 18, 30, 0),
                workoutId = null,
                stravaId = null,
            ),
        )
        every { isStravaConnected() } returns flowOf(true)
        coEvery { garminRepo.getActivities(10) } returns Result.success(garminActivities)
        coEvery { stravaRepo.getActivities(15) } returns Result.failure("Strava unavailable")

        val res = useCase()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)

        verify { isStravaConnected() }
        coVerify { garminRepo.getActivities(10) }
        coVerify { stravaRepo.getActivities(15) }

    }
}