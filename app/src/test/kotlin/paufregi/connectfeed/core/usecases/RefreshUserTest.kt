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
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.user

class RefreshUserTest{
    private val garminRepo = mockk<GarminRepository>()
    private val authRepo = mockk<AuthRepository>()
    private lateinit var useCase: RefreshUser

    @Before
    fun setup(){
        useCase = RefreshUser(garminRepo, authRepo)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Refresh user - success`() = runTest {
        coEvery { garminRepo.fetchUser() } returns Result.Success(user)
        coEvery { authRepo.saveUser(any()) } returns Unit

        val res = useCase()

        assertThat(res.isSuccessful).isTrue()

        coVerify {
            garminRepo.fetchUser()
            authRepo.saveUser(user)
        }
        confirmVerified(garminRepo, authRepo)
    }

    @Test
    fun `Refresh user - failure`() = runTest {
        coEvery { garminRepo.fetchUser() } returns Result.Failure("error")

        val res = useCase()

        assertThat(res.isSuccessful).isFalse()

        coVerify { garminRepo.fetchUser() }
        confirmVerified(garminRepo, authRepo)
    }
}