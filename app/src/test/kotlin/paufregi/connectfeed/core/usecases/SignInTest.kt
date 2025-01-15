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
import paufregi.connectfeed.consumer
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.oauth1
import paufregi.connectfeed.user

class SignInTest{
    private val garminRepo = mockk<GarminRepository>()
    private val authRepo = mockk<AuthRepository>()
    private lateinit var useCase: SignIn

    @Before
    fun setup(){
        useCase = SignIn(authRepo, garminRepo)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `SignIn - success`() = runTest {
        coEvery { authRepo.getOrFetchConsumer() } returns consumer
        coEvery { authRepo.authorize(any(), any(), any()) } returns Result.Success(oauth1)
        coEvery { authRepo.saveOAuth1(any()) } returns Unit
        coEvery { garminRepo.fetchUser() } returns Result.Success(user)
        coEvery { garminRepo.saveUser(any()) } returns Unit

        val res = useCase("user", "pass")
        assertThat(res.isSuccessful).isTrue()
        res as Result.Success
        assertThat(res.data).isEqualTo(user)

        coVerify {
            authRepo.getOrFetchConsumer()
            authRepo.authorize("user", "pass", consumer)
            authRepo.saveOAuth1(oauth1)
            garminRepo.fetchUser()
            garminRepo.saveUser(user)
        }
        confirmVerified(authRepo, garminRepo)
    }

    @Test
    fun `Validation fail - no user`() = runTest {
        val res = useCase("", "pass")
        assertThat(res.isSuccessful).isFalse()
        res as Result.Failure
        assertThat(res.reason).isEqualTo("Validation error")

        confirmVerified(authRepo, garminRepo)
    }

    @Test
    fun `Validation fail - no pass`() = runTest {
        val res = useCase("user", "")
        assertThat(res.isSuccessful).isFalse()
        res as Result.Failure
        assertThat(res.reason).isEqualTo("Validation error")

        confirmVerified(authRepo, garminRepo)
    }

    @Test
    fun `Validation fail - no user and pass`() = runTest {
        val res = useCase("", "")
        assertThat(res.isSuccessful).isFalse()
        res as Result.Failure
        assertThat(res.reason).isEqualTo("Validation error")

        confirmVerified(authRepo, garminRepo)
    }

    @Test
    fun `Failure - no consumer`() = runTest {
        coEvery { authRepo.getOrFetchConsumer() } returns null

        val res = useCase("user", "pass")
        assertThat(res.isSuccessful).isFalse()
        res as Result.Failure
        assertThat(res.reason).isEqualTo("Couldn't get OAuth Consumer")

        coVerify {
            authRepo.getOrFetchConsumer()
        }
        confirmVerified(authRepo, garminRepo)
    }

    @Test
    fun `Failure - authorize`() = runTest {
        coEvery { authRepo.getOrFetchConsumer() } returns consumer
        coEvery { authRepo.authorize(any(), any(), any()) } returns Result.Failure("Couldn't authorize")
        coEvery { authRepo.clear() } returns Unit

        val res = useCase("user", "pass")
        assertThat(res.isSuccessful).isFalse()
        res as Result.Failure
        assertThat(res.reason).isEqualTo("Couldn't authorize")

        coVerify {
            authRepo.getOrFetchConsumer()
            authRepo.authorize("user", "pass", consumer)
            authRepo.clear()
        }
        confirmVerified(authRepo, garminRepo)
    }

    @Test
    fun `Failure - fetch user`() = runTest {
        coEvery { authRepo.getOrFetchConsumer() } returns consumer
        coEvery { authRepo.authorize(any(), any(), any()) } returns Result.Success(oauth1)
        coEvery { authRepo.saveOAuth1(any()) } returns Unit
        coEvery { garminRepo.fetchUser() } returns Result.Failure("Couldn't fetch user")
        coEvery { authRepo.clear() } returns Unit

        val res = useCase("user", "pass")
        assertThat(res.isSuccessful).isFalse()
        res as Result.Failure
        assertThat(res.reason).isEqualTo("Couldn't fetch user")

        coVerify {
            authRepo.getOrFetchConsumer()
            authRepo.authorize("user", "pass", consumer)
            authRepo.saveOAuth1(oauth1)
            garminRepo.fetchUser()
            authRepo.clear()
        }
        confirmVerified(authRepo, garminRepo)
    }


}