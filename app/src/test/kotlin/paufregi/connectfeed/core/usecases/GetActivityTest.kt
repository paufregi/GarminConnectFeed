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
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.api.garmin.models.ActivityDto
import paufregi.connectfeed.data.api.garmin.models.ActivityTypeDto
import paufregi.connectfeed.data.repository.GarminRepository
import java.time.LocalDateTime

class GetActivityTest {
    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: GetActivity

    private val garminActivity = ActivityDto(
        id = 1,
        name = "Morning Run",
        type = ActivityTypeDto(1, "running", ActivityType.Running),
        eventType = EventType.Training,
        distance = 10_234.0,
        trainingEffectLabel = "Base",
        startDateTime = LocalDateTime.of(2024, 6, 1, 8, 0, 0),
        workoutId = 101,
    )

    @Before
    fun setup() {
        useCase = GetActivity(repo)
    }

    @After
    fun tearDown() {
        confirmVerified(repo)
        clearAllMocks()
    }

    @Test
    fun `Get activity`() = runTest {
        coEvery { repo.getActivity(1) } returns Result.success(garminActivity)

        val res = useCase(1, 12)

        assertThat(res.isSuccess).isTrue()
        val activity = res.getOrThrow()
        assertThat(activity.id).isEqualTo(1)
        assertThat(activity.name).isEqualTo("Morning Run")
        assertThat(activity.type).isEqualTo(ActivityType.Running)
        assertThat(activity.eventType).isEqualTo(EventType.Training)
        assertThat(activity.distance).isEqualTo(10_234.0)
        assertThat(activity.trainingEffect).isEqualTo("Base")
        assertThat(activity.date).isEqualTo(LocalDateTime.of(2024, 6, 1, 8, 0, 0))
        assertThat(activity.workoutId).isEqualTo(101)
        assertThat(activity.stravaId).isEqualTo(12)

        coVerify { repo.getActivity(1) }
    }

    @Test
    fun `Get activity - no distance`() = runTest {
        coEvery { repo.getActivity(1) } returns Result.success(garminActivity.copy(distance = 0.0))

        val res = useCase(1, 12)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrThrow().distance).isNull()

        coVerify { repo.getActivity(1) }
    }

    @Test
    fun `Get activity - no strava id`() = runTest {
        coEvery { repo.getActivity(1) } returns Result.success(garminActivity)

        val res = useCase(1, null)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrThrow().stravaId).isNull()

        coVerify { repo.getActivity(1) }
    }

    @Test
    fun `Get activity - failure`() = runTest {
        coEvery { repo.getActivity(1) } returns Result.failure("not found")

        val res = useCase(1, 12)

        assertThat(res.isFailure).isTrue()
        assertThat(res.exceptionOrNull()).hasMessageThat().isEqualTo("not found")

        coVerify { repo.getActivity(1) }
    }
}

