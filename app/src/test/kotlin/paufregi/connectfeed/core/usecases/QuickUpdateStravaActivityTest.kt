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
import paufregi.connectfeed.data.repository.StravaRepository

class QuickUpdateStravaActivityTest{
    private val repo = mockk<StravaRepository>()
    private lateinit var useCase: QuickUpdateStravaActivity

    val activity = Activity(
        id = 1,
        name = "name",
        type = ActivityType.Running,
        distance = 10234.00,
        trainingEffect = "recovery",
        workoutId = 1
    )
    val stravaActivity = Activity(
        id = 1,
        name = "strava name",
        type = ActivityType.Running,
        distance = 10234.00
    )
    val gear = Gear(
        id = "gear-id",
        stravaId = "strava-gear-id",
        name = "gear-name",
        type = GearType.Shoe
    )
    val profile = Profile(
        name = "newName",
        type = ActivityType.Running,
        eventType = EventType.Transportation,
        course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Running),
        gear = true,
        rename = true,
        trainingEffect = true,
    )
    val workout = Workout(1, "VO2 max")
    val description: String = "description"

    @Before
    fun setup(){
        useCase = QuickUpdateStravaActivity(repo)
    }

    @After
    fun tearDown(){
        confirmVerified(repo)
        clearAllMocks()
    }

    @Test
    fun `Update activity`() = runTest {
        coEvery { repo.updateActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val expectedDescription = "$description\n\nWorkout: VO₂ max\nBenefit: ${activity.trainingEffect}"
        val res = useCase(activity, stravaActivity, profile, description, workout, gear)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateActivity(stravaActivity, profile.name, expectedDescription, true, gear) }
    }

    @Test
    fun `Update activity - no rename`() = runTest {
        coEvery { repo.updateActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val profile = Profile(
            name = "newName",
            type = ActivityType.Running,
            eventType = EventType.Transportation,
            course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Running),
            gear = true,
            rename = false,
            trainingEffect = true,
        )

        val expectedDescription = "$description\n\nWorkout: VO₂ max\nBenefit: ${activity.trainingEffect}"
        val res = useCase(activity, stravaActivity, profile, description, workout, gear)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateActivity(stravaActivity, activity.name, expectedDescription, true, gear) }
    }

    @Test
    fun `Update activity - no training effect`() = runTest {
        coEvery { repo.updateActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val activity = Activity(
            id = 1,
            name = "name",
            type = ActivityType.Running,
            distance = 10234.00,
            trainingEffect = null,
            workoutId = 1
        )

        val expectedDescription = "$description\n\nWorkout: VO₂ max"
        val res = useCase(activity, stravaActivity, profile, description, workout, gear)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateActivity(stravaActivity, profile.name, expectedDescription, true, gear) }
    }

    @Test
    fun `Update activity - training effect flag false`() = runTest {
        coEvery { repo.updateActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val profile = Profile(
            name = "newName",
            type = ActivityType.Running,
            eventType = EventType.Transportation,
            course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Running),
            rename = true,
            gear = true,
            trainingEffect = false,
        )

        val expectedDescription = "$description\n\nWorkout: VO₂ max"
        val res = useCase(activity, stravaActivity, profile, description, workout, gear)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateActivity(stravaActivity, profile.name, expectedDescription, true, gear) }
    }

    @Test
    fun `Update activity - no workout`() = runTest {
        coEvery { repo.updateActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val activity = Activity(
            id = 1,
            name = "name",
            type = ActivityType.Running,
            distance = 10234.00,
            trainingEffect = "recovery",
        )
        val expectedDescription = "$description\n\nBenefit: ${activity.trainingEffect}"
        val res = useCase(activity, stravaActivity, profile, description, null, gear)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateActivity(stravaActivity, profile.name, expectedDescription, true, gear) }
    }

    @Test
    fun `Update activity - gear flag false`() = runTest {
        coEvery { repo.updateActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val profile = Profile(
            name = "newName",
            type = ActivityType.Running,
            eventType = EventType.Transportation,
            course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Running),
            rename = true,
            gear = false,
            trainingEffect = true,
        )

        val expectedDescription = "$description\n\nWorkout: VO₂ max\nBenefit: ${activity.trainingEffect}"
        val res = useCase(activity, stravaActivity, profile, description, workout, gear)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateActivity(stravaActivity, profile.name, expectedDescription, true, null) }
    }

    @Test
    fun `Update activity - no gear`() = runTest {
        coEvery { repo.updateActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val expectedDescription = "$description\n\nWorkout: VO₂ max\nBenefit: ${activity.trainingEffect}"
        val res = useCase(activity, stravaActivity, profile, description, workout, null)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateActivity(stravaActivity, profile.name, expectedDescription, true, null) }
    }

    @Test
    fun `Update activity - no commute`() = runTest {
        coEvery { repo.updateActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val profile = Profile(
            name = "newName",
            type = ActivityType.Running,
            eventType = EventType.Training,
            course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Running),
            rename = true,
            gear = true,
            trainingEffect = true
        )

        val expectedDescription = "$description\n\nWorkout: VO₂ max\nBenefit: ${activity.trainingEffect}"
        val res = useCase(activity, stravaActivity, profile, description, workout, gear)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateActivity(stravaActivity, profile.name, expectedDescription, false, gear) }
    }

    @Test
    fun `Invalid - no strava activity`() = runTest {
        val res = useCase(activity, null, profile, description, workout, gear)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Validation error")
    }

    @Test
    fun `Invalid - no profile`() = runTest {
        val res = useCase(activity, null, null, null, null, null)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Validation error")
    }

    @Test
    fun `Invalid - all nulls`() = runTest {
        val res = useCase(null, null, null, null, null, null)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Validation error")
    }
}