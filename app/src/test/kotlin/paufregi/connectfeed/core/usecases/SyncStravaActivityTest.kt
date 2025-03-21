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
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.data.repository.GarminRepository

class SyncStravaActivityTest{
    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: SyncStravaActivity

    val activity = Activity(
        id = 1,
        name = "name",
        type = ActivityType.Running,
        eventType = EventType.Transportation,
        distance = 10234.00,
        trainingEffect = "recovery"
    )
    val stravaActivity = Activity(
        id = 1,
        name = "strava name",
        type = ActivityType.Running,
        distance = 10234.00
    )
    val description: String = "description"

    @Before
    fun setup(){
        useCase = SyncStravaActivity(repo)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Sync activity`() = runTest {
        coEvery { repo.updateStravaActivity(any(), any(), any(), any()) } returns Result.Success(Unit)

        val expectedDescription = "$description\n\nTraining: recovery"
        val res = useCase(activity, stravaActivity, description, true)

        assertThat(res.isSuccessful).isTrue()
        coVerify { repo.updateStravaActivity(stravaActivity, activity.name, expectedDescription, true) }
        confirmVerified(repo)
    }

    @Test
    fun `Update activity - no training effect`() = runTest {
        coEvery { repo.updateStravaActivity(any(), any(), any(), any()) } returns Result.Success(Unit)

        val res = useCase(activity, stravaActivity, description, false)

        assertThat(res.isSuccessful).isTrue()
        coVerify { repo.updateStravaActivity(stravaActivity, activity.name, description, true) }
        confirmVerified(repo)
    }

    @Test
    fun `Update activity - no commute`() = runTest {
        coEvery { repo.updateStravaActivity(any(), any(), any(), any()) } returns Result.Success(Unit)

        val activity = Activity(
            id = 1,
            name = "name",
            type = ActivityType.Running,
            eventType = EventType.Training,
            distance = 10234.00,
            trainingEffect = "recovery"
        )

        val res = useCase(activity, stravaActivity, description, false)

        assertThat(res.isSuccessful).isTrue()
        coVerify { repo.updateStravaActivity(stravaActivity, activity.name, description, false) }
        confirmVerified(repo)
    }

    @Test
    fun `Invalid - no strava activity`() = runTest {
        val res = useCase(activity, null, description, true)

        assertThat(res.isSuccessful).isFalse()
        res as Result.Failure
        assertThat(res.reason).isEqualTo("Validation error")

        confirmVerified(repo)
    }

    @Test
    fun `Invalid - no activity`() = runTest {
        val res = useCase(null, stravaActivity, description, true)

        assertThat(res.isSuccessful).isFalse()
        res as Result.Failure
        assertThat(res.reason).isEqualTo("Validation error")

        confirmVerified(repo)
    }

    @Test
    fun `Invalid - all nulls`() = runTest {
        val res = useCase(null, null, description, true)

        assertThat(res.isSuccessful).isFalse()
        res as Result.Failure
        assertThat(res.reason).isEqualTo("Validation error")

        confirmVerified(repo)
    }
}