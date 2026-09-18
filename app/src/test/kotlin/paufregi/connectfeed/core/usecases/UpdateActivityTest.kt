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
import paufregi.connectfeed.core.models.Workout
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.data.repository.StravaRepository
import kotlin.time.Instant

class UpdateActivityTest{
    private val garminRepo = mockk<GarminRepository>()
    private val stravaRepo = mockk<StravaRepository>()
    private lateinit var useCase: UpdateActivity

    val activity = Activity(
        id = 1,
        name = "name",
        type = ActivityType.Running,
        eventType = EventType.Training,
        distance = 10234.00,
        trainingEffect = "recovery",
        date = Instant.parse("2024-06-01T08:00:00Z"),
        workoutId = 1L,
        stravaId = 2L
    )
    val workout = Workout(1, "VO2 max")
    val name = "newName"
    val description = "description"
    val eventType = EventType.Training
    val course = Course(id = 1, name = "courseName", distance = 1.0, type = ActivityType.Running)
    val water = 10
    val feel = 50f
    val effort = 90f
    val trainingEffect = "VO2MAX"
    val gear = Gear(id = "shoe-1", name = "Shoe 1", type = GearType.Shoe)

    @Before
    fun setup(){
        useCase = UpdateActivity(garminRepo, stravaRepo)
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

        val res = useCase(activityNoStrava, name, null, eventType, course, water, feel, effort, workout, gear, null, false)

        assertThat(res.isSuccess).isTrue()
        coVerify { garminRepo.updateActivity(activityNoStrava, name, garminDescription, eventType, course, water, feel, effort, gear) }
    }

    @Test
    fun `Update activity - with Strava`() = runTest {
        val garminDescription = "Workout: VO₂ max"
        val stravaDescription = "description\n\nWorkout: VO₂ max\nBenefit: VO₂ max"

        coEvery { garminRepo.updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { stravaRepo.updateActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val res = useCase(activity, name, description, eventType, course, water, feel, effort, workout, gear, trainingEffect, true)

        assertThat(res.isSuccess).isTrue()
        coVerify {
            garminRepo.updateActivity(activity, name, garminDescription, eventType, course, water, feel, effort, gear)
            stravaRepo.updateActivity(activity, name, stravaDescription, eventType.commute, gear)
        }
    }

    @Test
    fun `Update activity - no description`() = runTest {
        val garminDescription = "Workout: VO₂ max"
        val stravaDescription = "Workout: VO₂ max\nBenefit: VO₂ max"
        coEvery { garminRepo.updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { stravaRepo.updateActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val res = useCase(activity, name, null, eventType, course, water, feel, effort, workout, gear, trainingEffect, true)

        assertThat(res.isSuccess).isTrue()
        coVerify {
            garminRepo.updateActivity(activity, name, garminDescription, eventType, course, water, feel, effort, gear)
            stravaRepo.updateActivity(activity, name, stravaDescription, eventType.commute, gear)
        }
    }

    @Test
    fun `Update activity - no workout`() = runTest {
        val stravaDescription = "descriptionWorkout: VO₂ max\n\nBenefit: VO₂ max"
        coEvery { garminRepo.updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { stravaRepo.updateActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val res = useCase(activity, name, description, eventType, course, water, feel, effort, null, gear, trainingEffect, true)

        assertThat(res.isSuccess).isTrue()
        coVerify {
            garminRepo.updateActivity(activity, name, null, eventType, course, water, feel, effort, gear)
            stravaRepo.updateActivity(activity, name, stravaDescription, eventType.commute, gear)
        }
    }

    @Test
    fun `Update activity - no training effect`() = runTest {
        val garminDescription = "Workout: VO₂ max"
        val stravaDescription = "description\n\nWorkout: VO₂ max"

        coEvery { garminRepo.updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { stravaRepo.updateActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val res = useCase(activity, name, description, eventType, course, water, feel, effort, workout, gear, null, true)

        assertThat(res.isSuccess).isTrue()
        coVerify {
            garminRepo.updateActivity(activity, name, garminDescription, eventType, course, water, feel, effort, gear)
            stravaRepo.updateActivity(activity, name, stravaDescription, eventType.commute, gear)
        }
    }

    @Test
    fun `Update activity - training effect flag false`() = runTest {
        val garminDescription = "Workout: VO₂ max"
        val stravaDescription = "description\n\nWorkout: VO₂ max"

        coEvery { garminRepo.updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { stravaRepo.updateActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val res = useCase(activity, name, description, eventType, course, water, feel, effort, workout, gear, trainingEffect, false)

        assertThat(res.isSuccess).isTrue()
        coVerify {
            garminRepo.updateActivity(activity, name, garminDescription, eventType, course, water, feel, effort, gear)
            stravaRepo.updateActivity(activity, name, stravaDescription, eventType.commute, gear)
        }
    }

    @Test
    fun `Invalid - no name`() = runTest {
        val res = useCase(activity, null, description, eventType, course, water, feel, effort, workout, gear, trainingEffect, true)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Validation error")
    }

    @Test
    fun `Invalid - course not allowed`() = runTest {
        val invalidActivity = activity.copy(type = ActivityType.Swimming)

        val res = useCase(invalidActivity, name, description, eventType, course, water, feel, effort, workout, gear, trainingEffect, true)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Validation error")
    }

    @Test
    fun `Update activity - garmin failed`() = runTest {
        val garminDescription = "Workout: VO₂ max"
        val stravaDescription = "description\n\nWorkout: VO₂ max\nBenefit: VO₂ max"

        coEvery { garminRepo.updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.failure("error")
        coEvery { stravaRepo.updateActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val res = useCase(activity, name, description, eventType, course, water, feel, effort, workout, gear, trainingEffect, true)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Couldn't update Garmin activity")
        coVerify {
            garminRepo.updateActivity(activity, name, garminDescription, eventType, course, water, feel, effort, gear)
            stravaRepo.updateActivity(activity, name, stravaDescription, eventType.commute, gear)
        }
    }

    @Test
    fun `Update activity - strava failed`() = runTest {
        val garminDescription = "Workout: VO₂ max"
        val stravaDescription = "description\n\nWorkout: VO₂ max\nBenefit: VO₂ max"

        coEvery { garminRepo.updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { stravaRepo.updateActivity(any(), any(), any(), any(), any()) } returns Result.failure("error")

        val res = useCase(activity, name, description, eventType, course, water, feel, effort, workout, gear, trainingEffect, true)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Couldn't update Strava activity")
        coVerify {
            garminRepo.updateActivity(activity, name, garminDescription, eventType, course, water, feel, effort, gear)
            stravaRepo.updateActivity(activity, name, stravaDescription, eventType.commute, gear)
        }
    }

    @Test
    fun `Update activity - both failed`() = runTest {
        val garminDescription = "Workout: VO₂ max"
        val stravaDescription = "description\n\nWorkout: VO₂ max\nBenefit: VO₂ max"

        coEvery { garminRepo.updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.failure("error")
        coEvery { stravaRepo.updateActivity(any(), any(), any(), any(), any()) } returns Result.failure("error")

        val res = useCase(activity, name, description, eventType, course, water, feel, effort, workout, gear, trainingEffect, true)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Couldn't update Garmin & Strava activities")
        coVerify {
            garminRepo.updateActivity(activity, name, garminDescription, eventType, course, water, feel, effort, gear)
            stravaRepo.updateActivity(activity, name, stravaDescription, eventType.commute, gear)
        }
    }
}