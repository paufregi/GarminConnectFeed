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
    val profile = Profile(
        name = "newName",
        activityType = ActivityType.Running,
        eventType = EventType(id = 1, name = "event 1"),
        course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Running),
        water = 500,
        rename = true,
        customWater = true,
        feelAndEffort = true,
        trainingEffect = true
    )

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
        coEvery { repo.updateActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.Success(Unit)
        val res = useCase(activity, profile, 50f, 90f)

        assertThat(res.isSuccessful).isTrue()
        coVerify { repo.updateActivity(activity, profile.name, profile.eventType, profile.course, profile.water, 50f, 90f) }
        confirmVerified(repo)
    }

    @Test
    fun `Invalid - no activity`() = runTest {
        val res = useCase(null, profile, null, null)

        assertThat(res.isSuccessful).isFalse()
        res as Result.Failure
        assertThat(res.reason).isEqualTo("Validation error")

        confirmVerified(repo)
    }

    @Test
    fun `Invalid - no profile`() = runTest {
        val res = useCase(activity, null, null, null)

        assertThat(res.isSuccessful).isFalse()
        res as Result.Failure
        assertThat(res.reason).isEqualTo("Validation error")

        confirmVerified(repo)
    }

    @Test
    fun `Invalid - both null`() = runTest {
        val res = useCase(null, null, null, null)

        assertThat(res.isSuccessful).isFalse()
        res as Result.Failure
        assertThat(res.reason).isEqualTo("Validation error")

        confirmVerified(repo)
    }
}