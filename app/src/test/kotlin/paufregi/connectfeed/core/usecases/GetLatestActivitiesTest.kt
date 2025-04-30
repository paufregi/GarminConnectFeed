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
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.repository.GarminRepository

class GetLatestActivitiesTest{
    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: GetLatestActivities

    @Before
    fun setup(){
        useCase = GetLatestActivities(repo)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Get latest activities`() = runTest {
        val activities = listOf(
            Activity(id = 1, name = "name", distance = 10234.00, type = ActivityType.Running)
        )
        coEvery { repo.getLatestActivities(any()) } returns Result.success(activities)
        val res = useCase()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(activities)
        coVerify { repo.getLatestActivities(5) }
        confirmVerified(repo)
    }

    @Test
    fun `Get latest activities - failed`() = runTest {
        coEvery { repo.getLatestActivities(any()) } returns Result.failure("Failed")
        val res = useCase()

        assertThat(res.isSuccess).isFalse()
        coVerify { repo.getLatestActivities(5) }
        confirmVerified(repo)
    }
}