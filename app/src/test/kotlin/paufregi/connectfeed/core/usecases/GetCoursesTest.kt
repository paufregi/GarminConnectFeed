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
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.repository.GarminRepository

class GetCoursesTest {

    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: GetCourses

    @Before
    fun setup(){
        useCase = GetCourses(repo)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Get courses`() = runTest {
        val courses = listOf(
            Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Running),
            Course(id = 2, name = "course 2", distance = 15007.00, type = ActivityType.Cycling),
        )
        coEvery { repo.getCourses(any()) } returns Result.success(courses)
        val res = useCase(true)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(courses)
        coVerify { repo.getCourses(true) }
        confirmVerified(repo)
    }

    @Test
    fun `Get courses - failure`() = runTest {
        coEvery { repo.getCourses(any()) } returns Result.failure("Failed")
        val res = useCase()

        assertThat(res.isSuccess).isFalse()
        coVerify { repo.getCourses(false) }
        confirmVerified(repo)
    }
}