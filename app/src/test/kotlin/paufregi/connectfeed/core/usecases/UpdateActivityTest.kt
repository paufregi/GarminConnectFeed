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
    val name = "newName"
    val eventType = EventType.Training
    val course = Course(id = 1, name = "courseName", distance = 1.0, type = ActivityType.Running)
    val water = 10
    val feel = 50f
    val effort = 90f

    @Before
    fun setup(){
        useCase = UpdateActivity(repo)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Update activity`() = runTest {
        coEvery { repo.updateActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val res = useCase(activity, name, eventType, course, water, feel, effort)

        assertThat(res.isSuccess).isTrue()
        coVerify { repo.updateActivity(activity, name, eventType, course, water, feel, effort) }
        confirmVerified(repo)
    }

    @Test
    fun `Invalid - no activity`() = runTest {
        val res = useCase(null, name, eventType, course, water, feel, effort)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Validation error")

        confirmVerified(repo)
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

        val res = useCase(activity, name, eventType, course, water, feel, effort)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Validation error")

        confirmVerified(repo)
    }
}