package paufregi.connectfeed.core.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.core.models.ActivityType

class GetActivityTypesForProfileTest {

    private lateinit var useCase: GetActivityTypesForProfile

    @Before
    fun setup(){
        useCase = GetActivityTypesForProfile()
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Get activity types`() = runTest {
        val res = useCase()

        assertThat(res).containsExactly(
            ActivityType.Any,
           ActivityType.Running,
           ActivityType.Cycling,
           ActivityType.Swimming,
           ActivityType.StrengthTraining,
           ActivityType.Fitness,
           ActivityType.Other
        )
    }
}