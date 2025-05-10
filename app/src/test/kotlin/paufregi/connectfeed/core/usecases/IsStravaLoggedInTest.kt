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
import paufregi.connectfeed.data.repository.StravaAuthRepository
import paufregi.connectfeed.stravaAuthToken

class IsStravaLoggedInTest{
    private val repo = mockk<StravaAuthRepository>()
    private lateinit var useCase: IsStravaLoggedIn

    @Before
    fun setup(){
        useCase = IsStravaLoggedIn(repo)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Logged In`() = runTest {
        every { repo.getToken() } returns flowOf(stravaAuthToken)
        val res = useCase()

        res.test {
            assertThat(awaitItem()).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
        verify { repo.getToken() }
        confirmVerified(repo)
    }

    @Test
    fun `Not logged In - no code`() = runTest {
        every { repo.getToken() } returns flowOf(null)
        val res = useCase()

        res.test {
            assertThat(awaitItem()).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
        verify { repo.getToken() }
        confirmVerified(repo)
    }
}