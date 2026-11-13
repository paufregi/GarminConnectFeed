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
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.core.models.Workout
import paufregi.connectfeed.data.repository.StravaRepository

class UpdateStravaActivityTest{
    private val repo = mockk<StravaRepository>()
    private lateinit var useCase: UpdateStravaActivity

    val activity = Activity(
        id = 1,
        name = "strava name",
        type = ActivityType.Running,
        distance = 10234.00,
    )
    val gear = Gear(
        id = "gear-id",
        stravaId = "strava-gear-id",
        name = "gear-name",
        type = GearType.Shoe
    )
    val workout = Workout(1, "VO2 max")
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
        coEvery { repo.updateActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val expectedDescription = "$description\n\nWorkout: VO₂ max\nBenefit: $trainingEffect"
        val res = useCase(activity, name, description, eventType, trainingEffect, trainingEffectFlag, workout, gear)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateActivity(activity, name, expectedDescription, true, gear) }
    }

    @Test
    fun `Update activity - no training effect`() = runTest {
        coEvery { repo.updateActivity(any(), any(), any(), any(), any(),) } returns Result.success(Unit)

        val expectedDescription = "$description\n\nWorkout: VO₂ max"
        val res = useCase(activity, name, description, eventType, null, trainingEffectFlag, workout, gear)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateActivity(activity, name, expectedDescription, true, gear) }
    }

    @Test
    fun `Update activity - training effect flag false`() = runTest {
        coEvery { repo.updateActivity(any(), any(), any(), any(), any(),) } returns Result.success(Unit)

        val expectedDescription = "$description\n\nWorkout: VO₂ max"
        val res = useCase(activity, name, description, eventType, trainingEffect, false, workout, gear)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateActivity(activity, name, expectedDescription, true, gear) }
    }

    @Test
    fun `Update activity - no workout`() = runTest {
        coEvery { repo.updateActivity(any(), any(), any(), any(), any(),) } returns Result.success(Unit)

        val expectedDescription = "$description\n\nBenefit: $trainingEffect"
        val res = useCase(activity, name, description, eventType, trainingEffect, trainingEffectFlag, null, gear)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateActivity(activity, name, expectedDescription, true, gear) }
    }

    @Test
    fun `Update activity - no gear`() = runTest {
        coEvery { repo.updateActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val expectedDescription = "$description\n\nWorkout: VO₂ max\nBenefit: $trainingEffect"
        val res = useCase(activity, name, description, eventType, trainingEffect, trainingEffectFlag, workout, null)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateActivity(activity, name, expectedDescription, true, null) }
    }


    @Test
    fun `Invalid - no strava activity`() = runTest {
        val res = useCase(null, name, description, eventType, trainingEffect, trainingEffectFlag, workout, gear)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Validation error")
    }

    @Test
    fun `Invalid - no name`() = runTest {
        val res = useCase(activity, null, description, eventType, trainingEffect, trainingEffectFlag, workout, gear)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Validation error")
    }
}