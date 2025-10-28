package paufregi.connectfeed.core.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.core.models.ActivityCategory

class GetActivityCategoriesTest {

    private lateinit var useCase: GetActivityCategories

    @Before
    fun setup(){
        useCase = GetActivityCategories()
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Get activity categories`() = runTest {
        val res = useCase()

        assertThat(res).containsExactly(
            ActivityCategory.Any,
            ActivityCategory.Running,
            ActivityCategory.Cycling,
            ActivityCategory.Swimming,
            ActivityCategory.Strength,
            ActivityCategory.Fitness,
        )
    }
}