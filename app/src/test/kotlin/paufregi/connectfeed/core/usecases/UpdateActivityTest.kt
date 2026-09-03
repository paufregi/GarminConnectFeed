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
import paufregi.connectfeed.data.repository.GarminRepository

class UpdateActivityTest{
    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: UpdateActivity

    val activity = Activity(
        id = 1,
        name = "name",
        type = ActivityType.Running,
        distance = 10234.00,
        trainingEffect = "recovery"
    )
    val workout = Workout(1, "VO2 max")
    val name = "newName"
    val eventType = EventType.Training
    val course = Course(id = 1, name = "courseName", distance = 1.0, type = ActivityType.Running)
    val water = 10
    val feel = 50f
    val effort = 90f
    val gear = Gear(id = "shoe-1", name = "Shoe 1", type = GearType.Shoe)

    @Before
    fun setup(){
        useCase = UpdateActivity(repo)
    }

    @After
    fun tearDown(){
        confirmVerified(repo)
        clearAllMocks()
    }

    @Test
    fun `Update activity`() = runTest {
        coEvery { repo.updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val description = "Workout: VO₂ max"
        val res = useCase(activity, name, eventType, course, water, feel, effort, workout, gear)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateActivity(activity, name, description, eventType, course, water, feel, effort, gear) }
    }

    @Test
    fun `Update activity - no workout`() = runTest {
        coEvery { repo.updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val res = useCase(activity, name, eventType, course, water, feel, effort, null, null)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateActivity(activity, name, null, eventType, course, water, feel, effort, null) }
    }

    @Test
    fun `Update activity - no gears`() = runTest {
        coEvery { repo.updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val description = "Workout: VO₂ max"
        val res = useCase(activity, name, eventType, course, water, feel, effort, workout, null)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateActivity(activity, name, description, eventType, course, water, feel, effort, null) }
    }

    @Test
    fun `Invalid - no activity`() = runTest {
        val res = useCase(null, name, eventType, course, water, feel, effort, workout, null)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Validation error")
    }

    @Test
    fun `Invalid - course not allowed`() = runTest {
        val activity = Activity(
            id = 1,
            name = "name",
            type = ActivityType.Swimming,
            distance = 1034.00,
            trainingEffect = "recovery"
        )
        val course = Course(id = 1, name = "courseName", distance = 1.0, type = ActivityType.Running)

        val res = useCase(activity, name, eventType, course, water, feel, effort, workout, null)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Validation error")
    }
}