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
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.data.repository.GarminRepository

class QuickUpdateStravaActivityTest{
    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: QuickUpdateStravaActivity

    val activity = Activity(
        id = 1,
        name = "name",
        type = ActivityType.Running,
        distance = 10234.00,
        trainingEffect = "recovery"
    )
    val stravaActivity = Activity(
        id = 1,
        name = "strava name",
        type = ActivityType.Running,
        distance = 10234.00
    )
    val profile = Profile(
        name = "newName",
        type = ActivityType.Running,
        eventType = EventType.Transportation,
        course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Running),
        rename = true,
        trainingEffect = true
    )
    val description: String = "description"

    @Before
    fun setup(){
        useCase = QuickUpdateStravaActivity(repo)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Update activity`() = runTest {
        coEvery { repo.updateStravaActivity(any(), any(), any(), any()) } returns Result.success(Unit)

        val expectedDescription = "$description\n\nTraining: recovery"
        val res = useCase(activity, stravaActivity, profile, description)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateStravaActivity(stravaActivity, profile.name, expectedDescription, true) }
        confirmVerified(repo)
    }

    @Test
    fun `Update activity - no rename`() = runTest {
        coEvery { repo.updateStravaActivity(any(), any(), any(), any()) } returns Result.success(Unit)

        val profile = Profile(
            name = "newName",
            type = ActivityType.Running,
            eventType = EventType.Transportation,
            course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Running),
            rename = false,
            trainingEffect = true
        )

        val expectedDescription = "$description\n\nTraining: recovery"
        val res = useCase(activity, stravaActivity, profile, description)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateStravaActivity(stravaActivity, activity.name, expectedDescription, true) }
        confirmVerified(repo)
    }

    @Test
    fun `Update activity - no training effect`() = runTest {
        coEvery { repo.updateStravaActivity(any(), any(), any(), any()) } returns Result.success(Unit)

        val profile = Profile(
            name = "newName",
            type = ActivityType.Running,
            eventType = EventType.Transportation,
            course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Running),
            rename = true,
            trainingEffect = false
        )

        val res = useCase(activity, stravaActivity, profile, description)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateStravaActivity(stravaActivity, profile.name, description, true) }
        confirmVerified(repo)
    }

    @Test
    fun `Update activity - no commute`() = runTest {
        coEvery { repo.updateStravaActivity(any(), any(), any(), any()) } returns Result.success(Unit)

        val profile = Profile(
            name = "newName",
            type = ActivityType.Running,
            eventType = EventType.Training,
            course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Running),
            rename = true,
            trainingEffect = false
        )

        val res = useCase(activity, stravaActivity, profile, description)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateStravaActivity(stravaActivity, profile.name, description, false) }
        confirmVerified(repo)
    }

    @Test
    fun `Invalid - no strava activity`() = runTest {
        val res = useCase(activity, null, profile, description)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Validation error")

        confirmVerified(repo)
    }

    @Test
    fun `Invalid - no profile`() = runTest {
        val res = useCase(activity, null, null, null)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Validation error")

        confirmVerified(repo)
    }

    @Test
    fun `Invalid - all nulls`() = runTest {
        val res = useCase(null, null, null, null)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Validation error")

        confirmVerified(repo)
    }
}