package paufregi.connectfeed.core.usecases

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.data.repository.AuthRepository

class SignOutTest{
    private val authRepo = mockk<AuthRepository>()
    private lateinit var useCase: SignOut

    @Before
    fun setup(){
        useCase = SignOut(authRepo)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Sign out`() = runTest {
        coEvery { authRepo.clear() } returns Unit
        useCase()

        coVerify { authRepo.clear() }
        confirmVerified(authRepo)
    }
}