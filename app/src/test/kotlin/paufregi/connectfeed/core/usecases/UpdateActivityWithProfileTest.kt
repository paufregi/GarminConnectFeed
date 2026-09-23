package paufregi.connectfeed.core.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.models.Workout
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.data.repository.StravaRepository
import java.time.LocalDateTime

class UpdateActivityWithProfileTest{
    private val garminRepo = mockk<GarminRepository>()
    private val stravaRepo = mockk<StravaRepository>()
    private lateinit var useCase: UpdateActivityWithProfile

    val activity = Activity(
        id = 1,
        name = "name",
        type = ActivityType.Running,
        eventType = EventType.Training,
        distance = 10234.00,
        trainingEffect = "recovery",
        date = LocalDateTime.of(2024, 6, 1, 8, 0, 0),
        workoutId = 1L,
        stravaId = 2L
    )
    val profile = Profile(
        name = "newName",
        type = ActivityType.Running,
        eventType = EventType.Training,
        course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Running),
        water = 500,
        rename = true,
        customWater = true,
        feelAndEffort = true,
        trainingEffect = true
    )
    val description = "description"
    val workout = Workout(1, "VO2 max")
    val water = 10
    val feel = 50f
    val effort = 90f
    val trainingEffect = "VO2MAX"
    val gear = Gear(id = "shoe-1", name = "Shoe 1", type = GearType.Shoe)

    @Before
    fun setup(){
        useCase = UpdateActivityWithProfile(garminRepo, stravaRepo)
    }

    @After
    fun tearDown(){
        confirmVerified(garminRepo, stravaRepo)
        clearAllMocks()
    }

    @Test
    fun `Update activity`() = runTest {
        val activityNoStrava = activity.copy(stravaId = null)
        val garminDescription = "Workout: VO₂ max"

        coEvery { garminRepo.updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val res = useCase(activityNoStrava, profile, null, water, feel, effort, workout, gear, trainingEffect)

        assertThat(res.isSuccess).isTrue()
        coVerify { garminRepo.updateActivity(activityNoStrava, profile.name, garminDescription, profile.eventType, profile.course, water, feel, effort, gear) }
    }

    @Test
    fun `Update activity - with Strava`() = runTest {
        val garminDescription = "Workout: VO₂ max"
        val stravaDescription = "description\n\nWorkout: VO₂ max\nBenefit: VO₂ max"

        coEvery { garminRepo.updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { stravaRepo.updateActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val res = useCase(activity, profile, description, water, feel, effort, workout, gear, trainingEffect)

        assertThat(res.isSuccess).isTrue()
        coVerify {
            garminRepo.updateActivity(activity, profile.name, garminDescription, profile.eventType, profile.course, water, feel, effort, gear)
            stravaRepo.updateActivity(activity, profile.name, stravaDescription, profile.eventType?.commute, gear)
        }
    }

    @Test
    fun `Update activity - no description`() = runTest {
        val garminDescription = "Workout: VO₂ max"
        val stravaDescription = "Workout: VO₂ max\nBenefit: VO₂ max"

        coEvery { garminRepo.updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { stravaRepo.updateActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val res = useCase(activity, profile, null, water, feel, effort, workout, gear, trainingEffect)

        assertThat(res.isSuccess).isTrue()
        coVerify {
            garminRepo.updateActivity(activity, profile.name, garminDescription, profile.eventType, profile.course, water, feel, effort, gear)
            stravaRepo.updateActivity(activity, profile.name, stravaDescription, profile.eventType?.commute, gear)
        }
    }

    @Test
    fun `Update activity - no workout`() = runTest {
        val stravaDescription = "description\n\nBenefit: VO₂ max"

        coEvery { garminRepo.updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { stravaRepo.updateActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val res = useCase(activity, profile, description, water, feel, effort, null, gear, trainingEffect)

        assertThat(res.isSuccess).isTrue()
        coVerify {
            garminRepo.updateActivity(activity, profile.name, null, profile.eventType, profile.course, water, feel, effort, gear)
            stravaRepo.updateActivity(activity, profile.name, stravaDescription, profile.eventType?.commute, gear)
        }
    }

    @Test
    fun `Update activity - no training effect`() = runTest {
        val garminDescription = "Workout: VO₂ max"
        val stravaDescription = "description\n\nWorkout: VO₂ max"

        coEvery { garminRepo.updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { stravaRepo.updateActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val res = useCase(activity, profile, description, water, feel, effort, workout, gear, null)

        assertThat(res.isSuccess).isTrue()
        coVerify {
            garminRepo.updateActivity(activity, profile.name, garminDescription, profile.eventType, profile.course, water, feel, effort, gear)
            stravaRepo.updateActivity(activity, profile.name, stravaDescription, profile.eventType?.commute, gear)
        }
    }

    @Test
    fun `Update activity - training effect flag false`() = runTest {
        val garminDescription = "Workout: VO₂ max"
        val stravaDescription = "description\n\nWorkout: VO₂ max"

        coEvery { garminRepo.updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { stravaRepo.updateActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val res = useCase(activity, profile.copy(trainingEffect = false), description, water, feel, effort, workout, gear, trainingEffect)

        assertThat(res.isSuccess).isTrue()
        coVerify {
            garminRepo.updateActivity(activity, profile.name, garminDescription, profile.eventType, profile.course, water, feel, effort, gear)
            stravaRepo.updateActivity(activity, profile.name, stravaDescription, profile.eventType?.commute, gear)
        }
    }

    @Test
    fun `Invalid - no profile`() = runTest {
        val res = useCase(activity, null, description, water, feel, effort, workout, gear, trainingEffect)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Validation error")
    }

    @Test
    fun `Update activity - garmin failed`() = runTest {
        val garminDescription = "Workout: VO₂ max"
        val stravaDescription = "description\n\nWorkout: VO₂ max\nBenefit: VO₂ max"

        coEvery { garminRepo.updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.failure("error")
        coEvery { stravaRepo.updateActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val res = useCase(activity, profile, description, water, feel, effort, workout, gear, trainingEffect)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Couldn't update Garmin activity")
        coVerify {
            garminRepo.updateActivity(activity, profile.name, garminDescription, profile.eventType, profile.course, water, feel, effort, gear)
            stravaRepo.updateActivity(activity, profile.name, stravaDescription, profile.eventType?.commute, gear)
        }
    }

    @Test
    fun `Update activity - strava failed`() = runTest {
        val garminDescription = "Workout: VO₂ max"
        val stravaDescription = "description\n\nWorkout: VO₂ max\nBenefit: VO₂ max"

        coEvery { garminRepo.updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { stravaRepo.updateActivity(any(), any(), any(), any(), any()) } returns Result.failure("error")

        val res = useCase(activity, profile, description, water, feel, effort, workout, gear, trainingEffect)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Couldn't update Strava activity")
        coVerify {
            garminRepo.updateActivity(activity, profile.name, garminDescription, profile.eventType, profile.course, water, feel, effort, gear)
            stravaRepo.updateActivity(activity, profile.name, stravaDescription, profile.eventType?.commute, gear)
        }
    }

    @Test
    fun `Update activity - both failed`() = runTest {
        val garminDescription = "Workout: VO₂ max"
        val stravaDescription = "description\n\nWorkout: VO₂ max\nBenefit: VO₂ max"

        coEvery { garminRepo.updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.failure("error")
        coEvery { stravaRepo.updateActivity(any(), any(), any(), any(), any()) } returns Result.failure("error")

        val res = useCase(activity, profile, description, water, feel, effort, workout, gear, trainingEffect)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Couldn't update Garmin and Strava activities")
        coVerify {
            garminRepo.updateActivity(activity, profile.name, garminDescription, profile.eventType, profile.course, water, feel, effort, gear)
            stravaRepo.updateActivity(activity, profile.name, stravaDescription, profile.eventType?.commute, gear)
        }
    }
}