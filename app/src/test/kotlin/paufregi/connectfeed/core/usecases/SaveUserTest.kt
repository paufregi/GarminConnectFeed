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
import paufregi.connectfeed.user

class SaveUserTest {
    private val authRepo = mockk<AuthRepository>()
    private lateinit var useCase: SaveUser

    @Before
    fun setup() {
        useCase = SaveUser(authRepo)
    }

    @After
    fun tearDown() {
        confirmVerified(authRepo)
        clearAllMocks()
    }

    @Test
    fun `Save user`() = runTest {
        coEvery { authRepo.saveUser(user) } returns Unit

        useCase(user)

        coVerify { authRepo.saveUser(user) }
    }
}

