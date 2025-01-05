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
import paufregi.connectfeed.data.repository.GarminRepository

class RefreshUserTest{
    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: RefreshUser

    @Before
    fun setup(){
        useCase = RefreshUser(repo)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Refresh user - success`() = runTest {
        val user = User("user", "profileImage")

        coEvery { repo.fetchUser() } returns Result.Success(user)
        coEvery { repo.saveUser(any()) } returns Unit

        val res = useCase()

        assertThat(res).isInstanceOf(Result.Success<Unit>(Unit).javaClass)

        coVerify {
            repo.fetchUser()
            repo.saveUser(user)
        }
        confirmVerified(repo)
    }

    @Test
    fun `Refresh user - failure`() = runTest {
        coEvery { repo.fetchUser() } returns Result.Failure("error")

        val res = useCase()

        assertThat(res).isInstanceOf(Result.Failure<Unit>("error").javaClass)
        coVerify { repo.fetchUser() }
        confirmVerified(repo)
    }
}