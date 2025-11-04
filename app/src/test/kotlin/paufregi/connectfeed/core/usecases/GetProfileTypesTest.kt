package paufregi.connectfeed.core.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.core.models.ProfileType

class GetProfileTypesTest {

    private lateinit var useCase: GetProfileTypes

    @Before
    fun setup(){
        useCase = GetProfileTypes()
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Get activity types`() = runTest {
        val res = useCase()

        assertThat(res).containsExactly(
            ProfileType.Any,
            ProfileType.Running,
            ProfileType.Cycling,
            ProfileType.Swimming,
            ProfileType.Strength,
            ProfileType.Fitness,
            ProfileType.Other
        )
    }
}