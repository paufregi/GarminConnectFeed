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
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Workout
import paufregi.connectfeed.data.repository.GarminRepository

class UpdateStravaActivityTest{
    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: UpdateStravaActivity

    val activity = Activity(
        id = 1,
        name = "strava name",
        type = ActivityType.Running,
        distance = 10234.00,
    )
    val workout = Workout(1, "workout")
    val name = "newName"
    val description = "description"
    val eventType = EventType.Transportation
    val trainingEffect = "recovery"
    val trainingEffectFlag = true

    @Before
    fun setup(){
        useCase = UpdateStravaActivity(repo)
    }

    @After
    fun tearDown(){
        confirmVerified(repo)
        clearAllMocks()
    }

    @Test
    fun `Update activity`() = runTest {
        coEvery { repo.getWorkout(any()) } returns Result.success(workout)
        coEvery { repo.updateStravaActivity(any(), any(), any(), any(),) } returns Result.success(Unit)

        val expectedDescription = "$description\n\nWorkout: ${workout.name}\nBenefit: $trainingEffect"
        val res = useCase(activity, name, description, eventType, trainingEffect, trainingEffectFlag, workout.id)

        assertThat(res.isSuccess).isTrue()
        coVerify {
            repo.getWorkout(workout.id)
            repo.updateStravaActivity(activity, name, expectedDescription, true)
        }
    }

    @Test
    fun `Update activity - no training effect`() = runTest {
        coEvery { repo.getWorkout(any()) } returns Result.success(workout)
        coEvery { repo.updateStravaActivity(any(), any(), any(), any(),) } returns Result.success(Unit)

        val expectedDescription = "$description\n\nWorkout: ${workout.name}"
        val res = useCase(activity, name, description, eventType, null, trainingEffectFlag, workout.id)

        assertThat(res.isSuccess).isTrue()
        coVerify {
            repo.getWorkout(workout.id)
            repo.updateStravaActivity(activity, name, expectedDescription, true)
        }
    }

    @Test
    fun `Update activity - training effect flag false`() = runTest {
        coEvery { repo.getWorkout(any()) } returns Result.success(workout)
        coEvery { repo.updateStravaActivity(any(), any(), any(), any(),) } returns Result.success(Unit)

        val expectedDescription = "$description\n\nWorkout: ${workout.name}"
        val res = useCase(activity, name, description, eventType, trainingEffect, false, workout.id)

        assertThat(res.isSuccess).isTrue()
        coVerify {
            repo.getWorkout(workout.id)
            repo.updateStravaActivity(activity, name, expectedDescription, true)
        }
    }

    @Test
    fun `Update activity - no workout`() = runTest {
        coEvery { repo.updateStravaActivity(any(), any(), any(), any(),) } returns Result.success(Unit)

        val expectedDescription = "$description\n\nBenefit: $trainingEffect"
        val res = useCase(activity, name, description, eventType, trainingEffect, trainingEffectFlag, null)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateStravaActivity(activity, name, expectedDescription, true) }
    }


    @Test
    fun `Invalid - no strava activity`() = runTest {
        val res = useCase(null, name, description, eventType, trainingEffect, trainingEffectFlag, workout.id)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Validation error")
    }

    @Test
    fun `Invalid - no name`() = runTest {
        val res = useCase(activity, null, description, eventType, trainingEffect, trainingEffectFlag, workout.id)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Validation error")
    }
}