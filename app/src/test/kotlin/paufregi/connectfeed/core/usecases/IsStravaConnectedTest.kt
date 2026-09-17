package paufregi.connectfeed.core.usecases

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.stravaAuthToken

class IsStravaConnectedTest{
    private val repo = mockk<AuthRepository>()
    private lateinit var useCase: IsStravaConnected

    @Before
    fun setup(){
        useCase = IsStravaConnected(repo)
    }

    @After
    fun tearDown(){
        confirmVerified(repo)
        clearAllMocks()
    }

    @Test
    fun `Logged In`() = runTest {
        every { repo.getStravaToken() } returns flowOf(stravaAuthToken)
        val res = useCase()

        res.test {
            assertThat(awaitItem()).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
        verify { repo.getStravaToken() }
    }

    @Test
    fun `Not logged In - no code`() = runTest {
        every { repo.getStravaToken() } returns flowOf(null)
        val res = useCase()

        res.test {
            assertThat(awaitItem()).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
        verify { repo.getStravaToken() }
    }
}