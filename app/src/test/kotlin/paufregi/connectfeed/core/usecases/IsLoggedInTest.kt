package paufregi.connectfeed.core.usecases

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.api.models.OAuth1
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.oauth1
import paufregi.connectfeed.user

class IsLoggedInTest{
    private val authRepo = mockk<AuthRepository>()
    private lateinit var useCase: IsLoggedIn

    @Before
    fun setup(){
        useCase = IsLoggedIn(authRepo)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Logged In`() = runTest {
        coEvery { authRepo.getUser() } returns flowOf(user)
        val res = useCase()

        res.test {
            assertThat(awaitItem()).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
        coVerify {
            authRepo.getUser()
        }
        confirmVerified(authRepo)
    }

    @Test
    fun `Not logged In - no user`() = runTest {
        coEvery { authRepo.getUser() } returns flowOf(null)
        val res = useCase()

        res.test {
            assertThat(awaitItem()).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
        coVerify {
            authRepo.getUser()
        }
        confirmVerified(authRepo)
    }
}