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

class GetActivitiesTest{
    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: GetActivities

    @Before
    fun setup(){
        useCase = GetActivities(repo)
    }

    @After
    fun tearDown(){
        confirmVerified(repo)
        clearAllMocks()
    }

    @Test
    fun `Get activities`() = runTest {
        val activities = listOf(
            Activity(id = 1, name = "name", distance = 10234.00, type = ActivityType.Running)
        )
        coEvery { repo.getActivities(any(), any()) } returns Result.success(activities)
        val res = useCase(true)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(activities)
        coVerify { repo.getActivities(5, true) }
    }

    @Test
    fun `Get activities - failed`() = runTest {
        coEvery { repo.getActivities(any(), any()) } returns Result.failure("Failed")
        val res = useCase()

        assertThat(res.isSuccess).isFalse()
        coVerify { repo.getActivities(5, false) }
    }
}