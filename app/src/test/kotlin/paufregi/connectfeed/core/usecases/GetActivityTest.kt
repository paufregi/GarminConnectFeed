package paufregi.connectfeed.core.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.data.repository.GarminRepository
import java.time.LocalDateTime
import paufregi.connectfeed.data.api.garmin.models.Activity as GarminActivity
import paufregi.connectfeed.data.api.garmin.models.ActivityType as GarminActivityType

class GetActivityTest {
    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: GetActivity

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
    fun `Get activity`() {
        val garminActivity = GarminActivity(
            id = 1,
            name = "Morning Run",
            type = GarminActivityType(1, "running", ActivityType.Running),
            eventType = EventType.Training,
            distance = 10_234.0,
            trainingEffectLabel = "Base",
            startDateTime = LocalDateTime.of(2024, 6, 1, 8, 0, 0),
            workoutId = 101,
        )

        every { repo.getActivity(1) } returns garminActivity

        val res = useCase(1, 12)

        assertThat(res).isNotNull()
        assertThat(res?.id).isEqualTo(1)
        assertThat(res?.name).isEqualTo("Morning Run")
        assertThat(res?.type).isEqualTo(ActivityType.Running)
        assertThat(res?.eventType).isEqualTo(EventType.Training)
        assertThat(res?.distance).isEqualTo(10_234.0)
        assertThat(res?.trainingEffect).isEqualTo("Base")
        assertThat(res?.date).isEqualTo(LocalDateTime.of(2024, 6, 1, 8, 0, 0))
        assertThat(res?.workoutId).isEqualTo(101)
        assertThat(res?.stravaId).isEqualTo(12)

        verify { repo.getActivity(1) }
    }

    @Test
    fun `Get activity - no distance`() {
        val garminActivity = GarminActivity(
            id = 1,
            name = "Morning Run",
            type = GarminActivityType(1, "running", ActivityType.Running),
            eventType = EventType.Training,
            distance = 0.0,
            trainingEffectLabel = "Base",
            startDateTime = LocalDateTime.of(2024, 6, 1, 8, 0, 0),
            workoutId = 101,
        )

        every { repo.getActivity(1) } returns garminActivity

        val res = useCase(1, 12)

        assertThat(res).isNotNull()
        assertThat(res?.id).isEqualTo(1)
        assertThat(res?.name).isEqualTo("Morning Run")
        assertThat(res?.type).isEqualTo(ActivityType.Running)
        assertThat(res?.eventType).isEqualTo(EventType.Training)
        assertThat(res?.distance).isEqualTo(10_234.0)
        assertThat(res?.trainingEffect).isEqualTo("Base")
        assertThat(res?.date).isEqualTo(LocalDateTime.of(2024, 6, 1, 8, 0, 0))
        assertThat(res?.workoutId).isEqualTo(101)
        assertThat(res?.stravaId).isEqualTo(12)

        verify { repo.getActivity(1) }
    }

    @Test
    fun `Get activity - no activity`() {
        every { repo.getActivity(1) } returns null

        val res = useCase(1, 12)

        assertThat(res).isNull()

        verify { repo.getActivity(1) }
    }
}

