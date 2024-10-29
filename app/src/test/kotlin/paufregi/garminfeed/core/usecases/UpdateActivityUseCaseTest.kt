package paufregi.garminfeed.core.usecases

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
import paufregi.garminfeed.core.models.Activity
import paufregi.garminfeed.core.models.ActivityType
import paufregi.garminfeed.core.models.Course
import paufregi.garminfeed.core.models.Credential
import paufregi.garminfeed.core.models.EventType
import paufregi.garminfeed.core.models.Profile
import paufregi.garminfeed.core.models.Result
import paufregi.garminfeed.data.repository.GarminRepository

class UpdateActivityUseCaseTest{
    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: UpdateActivityUseCase

    val activity = Activity(id = 1, name = "name", type = ActivityType.Running)
    val profile = Profile(
        activityName = "newName",
        eventType = EventType.transportation,
        activityType = ActivityType.Running,
        course = Course.home,
        water = 500
    )

    @Before
    fun setup(){
        useCase = UpdateActivityUseCase(repo)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Update activity use-case`() = runTest{
        coEvery { repo.updateActivity(any(), any()) } returns Result.Success(Unit)
        val res = useCase(activity, profile)

        assertThat(res).isInstanceOf(Result.Success(Unit).javaClass)
        coVerify { repo.updateActivity(activity, profile) }
        confirmVerified(repo)
    }

    @Test
    fun `Invalid - no activity`() = runTest{
        val res = useCase(null, profile)

        assertThat(res).isInstanceOf(Result.Failure<Unit>("Validation error").javaClass)
    }

    @Test
    fun `Invalid - no profile`() = runTest{
        val res = useCase(activity, null)

        assertThat(res).isInstanceOf(Result.Failure<Unit>("Validation error").javaClass)
    }

    @Test
    fun `Invalid - both null`() = runTest{
        val res = useCase(null, null)

        assertThat(res).isInstanceOf(Result.Failure<Unit>("Validation error").javaClass)
    }
}