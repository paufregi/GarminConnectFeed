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
        confirmVerified(repo)
        clearAllMocks()
    }

    @Test
    fun `Sync activity`() = runTest {
        coEvery { repo.updateStravaActivity(any(), any(), any(), any()) } returns Result.success(Unit)

        val expectedDescription = "$description\n\nTraining: recovery"
        val res = useCase(activity, stravaActivity, description, true)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateStravaActivity(stravaActivity, activity.name, expectedDescription, true) }
    }

    @Test
    fun `Update activity - no training effect`() = runTest {
        coEvery { repo.updateStravaActivity(any(), any(), any(), any()) } returns Result.success(Unit)

        val res = useCase(activity, stravaActivity, description, false)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateStravaActivity(stravaActivity, activity.name, description, true) }
    }

    @Test
    fun `Update activity - no commute`() = runTest {
        coEvery { repo.updateStravaActivity(any(), any(), any(), any()) } returns Result.success(Unit)

        val activity = Activity(
            id = 1,
            name = "name",
            type = ActivityType.Running,
            eventType = EventType.Training,
            distance = 10234.00,
            trainingEffect = "recovery"
        )

        val res = useCase(activity, stravaActivity, description, false)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateStravaActivity(stravaActivity, activity.name, description, false) }
    }

    @Test
    fun `Invalid - no strava activity`() = runTest {
        val res = useCase(activity, null, description, true)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Validation error")
    }

    @Test
    fun `Invalid - no activity`() = runTest {
        val res = useCase(null, stravaActivity, description, true)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Validation error")
    }

    @Test
    fun `Invalid - all nulls`() = runTest {
        val res = useCase(null, null, description, true)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Validation error")
    }
}