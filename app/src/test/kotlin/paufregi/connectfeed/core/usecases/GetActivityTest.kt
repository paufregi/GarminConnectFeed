package paufregi.connectfeed.core.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.data.api.garmin.models.ActivityDto
import paufregi.connectfeed.data.api.garmin.models.ActivityTypeDto
import paufregi.connectfeed.data.repository.GarminRepository
import java.time.LocalDateTime

class GetActivityTest {

    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: GetActivity

    @Before
    fun setup(){
        useCase = GetActivity(repo)
    }

    @After
    fun tearDown(){
        confirmVerified(repo)
        clearAllMocks()
    }

    @Test
    fun `Get activity`() = runTest {
        val date = LocalDateTime.of(2025, 1, 2, 3, 4)
        val garminActivity = ActivityDto(
            id = 1,
            name = "Morning run",
            type = ActivityTypeDto(id = 1, key = "running", type = ActivityType.Running),
            eventType = EventType.Training,
            distance = 1234.5,
            trainingEffectLabel = "Base",
            beginTimestamp = date,
            workoutId = 42,
        )
        val expected = Activity(
            id = 1,
            name = "Morning run",
            type = ActivityType.Running,
            eventType = EventType.Training,
            distance = 1234.5,
            trainingEffect = "Base",
            date = date,
            workoutId = 42,
            stravaId = 99,
        )

        every { repo.getActivity(1) } returns garminActivity

        val result = useCase(1, 99)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(expected)
        verify { repo.getActivity(1) }
    }

    @Test
    fun `Get activity - not found`() = runTest {
        every { repo.getActivity(1) } returns null

        val result = useCase(1, null)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Activity not found")
        verify { repo.getActivity(1) }
    }
}
