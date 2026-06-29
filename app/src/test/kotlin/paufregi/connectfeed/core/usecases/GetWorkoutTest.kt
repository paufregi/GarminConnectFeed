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
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.core.models.Workout
import paufregi.connectfeed.data.repository.GarminRepository

class GetWorkoutTest {

    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: GetWorkout

    @Before
    fun setup(){
        useCase = GetWorkout(repo)
    }

    @After
    fun tearDown(){
        confirmVerified(repo)
        clearAllMocks()
    }

    @Test
    fun `Get workout`() = runTest {
        val workout = Workout(1, "workout")
        coEvery { repo.getWorkout(any(), any()) } returns Result.success(workout)
        val res = useCase(1, true)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(workout)
        coVerify { repo.getWorkout(1, true) }
    }

    @Test
    fun `Get courses - failure`() = runTest {
        coEvery { repo.getWorkout(any(), any()) } returns Result.failure("Failed")
        val res = useCase(1)

        assertThat(res.isSuccess).isFalse()
        coVerify { repo.getWorkout(1, false) }
    }
}