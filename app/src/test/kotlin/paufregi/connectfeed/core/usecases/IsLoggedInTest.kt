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
import paufregi.connectfeed.core.models.Credential
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.repository.GarminRepository

class IsLoggedInTest{
    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: IsLoggedIn

    @Before
    fun setup(){
        useCase = IsLoggedIn(repo)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Logged In`() = runTest {
        val user = User("user", "avatar")
        val cred = Credential("user", "pass")
        coEvery { repo.getUser() } returns flowOf(user)
        coEvery { repo.getCredential() } returns flowOf(cred)
        val res = useCase()

        res.test {
            assertThat(awaitItem()).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
        coVerify {
            repo.getUser()
            repo.getCredential()
        }
        confirmVerified(repo)
    }

    @Test
    fun `Not logged In - no user`() = runTest {
        val cred = Credential("user", "pass")
        coEvery { repo.getUser() } returns flowOf(null)
        coEvery { repo.getCredential() } returns flowOf(cred)
        val res = useCase()

        res.test {
            assertThat(awaitItem()).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
        coVerify {
            repo.getUser()
            repo.getCredential()
        }
        confirmVerified(repo)
    }

    @Test
    fun `Not logged In - no credential`() = runTest {
        val user = User("user", "avatar")
        coEvery { repo.getUser() } returns flowOf(user)
        coEvery { repo.getCredential() } returns flowOf(null)
        val res = useCase()

        res.test {
            assertThat(awaitItem()).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
        coVerify {
            repo.getUser()
            repo.getCredential()
        }
        confirmVerified(repo)
    }

    @Test
    fun `Not logged In - blank user in credential`() = runTest {
        val user = User("user", "avatar")
        val cred = Credential("", "pass")
        coEvery { repo.getUser() } returns flowOf(user)
        coEvery { repo.getCredential() } returns flowOf(cred)
        val res = useCase()

        res.test {
            assertThat(awaitItem()).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
        coVerify {
            repo.getUser()
            repo.getCredential()
        }
        confirmVerified(repo)
    }

    @Test
    fun `Not logged In - blank password in credential`() = runTest {
        val user = User("user", "avatar")
        val cred = Credential("user", "")
        coEvery { repo.getUser() } returns flowOf(user)
        coEvery { repo.getCredential() } returns flowOf(cred)
        val res = useCase()

        res.test {
            assertThat(awaitItem()).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
        coVerify {
            repo.getUser()
            repo.getCredential()
        }
        confirmVerified(repo)
    }
}