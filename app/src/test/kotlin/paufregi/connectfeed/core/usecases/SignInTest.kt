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
import paufregi.connectfeed.authToken
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.user

class SignInTest{
    private val authRepo = mockk<AuthRepository>()
    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: SignIn

    private val clientId = "clientId"
    private val ticket = "ST-XYZ"
    private val username = "user"
    private val password = "pass"

    @Before
    fun setup(){
        useCase = SignIn(authRepo, repo, clientId)
    }

    @After
    fun tearDown(){
        confirmVerified(authRepo, repo)
        clearAllMocks()
    }

    @Test
    fun `Sign in`() = runTest {
        coEvery { authRepo.garminLogin(any(), any()) } returns Result.success(ticket)
        coEvery { authRepo.exchangeGarminToken(any(), any()) } returns Result.success(authToken)
        coEvery { authRepo.saveGarminToken(any()) } returns Unit
        coEvery { repo.getUserProfile() } returns Result.success(user)
        coEvery { authRepo.saveUser(any()) } returns Unit

        val res = useCase(username, password)
        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(user)

        coVerify {
            authRepo.garminLogin(username, password)
            authRepo.exchangeGarminToken(ticket, clientId)
            authRepo.saveGarminToken(authToken)
            repo.getUserProfile()
            authRepo.saveUser(user)
        }
    }

    @Test
    fun `Validation fail - no user`() = runTest {
        val res = useCase("", password)
        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Validation error")
    }

    @Test
    fun `Validation fail - no pass`() = runTest {
        val res = useCase(username, "")
        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Validation error")
    }

    @Test
    fun `Validation fail - no user and pass`() = runTest {
        val res = useCase("", "")
        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Validation error")
    }

    @Test
    fun `Failure - failed login`() = runTest {
        coEvery { authRepo.garminLogin(any(), any()) } returns Result.failure("Couldn't login")
        coEvery { authRepo.clear() } returns Unit

        val res = useCase(username, password)
        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Couldn't login")

        coVerify {
            authRepo.garminLogin(username, password)
            authRepo.clear()
        }
    }

    @Test
    fun `Failure - failed exchange token`() = runTest {
        coEvery { authRepo.garminLogin(any(), any()) } returns Result.success(ticket)
        coEvery { authRepo.exchangeGarminToken(any(), any()) } returns Result.failure("Couldn't authorize")
        coEvery { authRepo.clear() } returns Unit

        val res = useCase(username, password)
        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Couldn't authorize")

        coVerify {
            authRepo.garminLogin(username, password)
            authRepo.exchangeGarminToken(ticket, clientId)
            authRepo.clear()
        }
    }

    @Test
    fun `Failure - get user profile`() = runTest {
        coEvery { authRepo.garminLogin(any(), any()) } returns Result.success(ticket)
        coEvery { authRepo.exchangeGarminToken(any(), any()) } returns Result.success(authToken)
        coEvery { authRepo.saveGarminToken(any()) } returns Unit
        coEvery { repo.getUserProfile() } returns Result.failure("Couldn't fetch user")
        coEvery { authRepo.clear() } returns Unit

        val res = useCase(username, password)
        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Couldn't fetch user")

        coVerify {
            authRepo.garminLogin(username, password)
            authRepo.exchangeGarminToken(ticket, clientId)
            authRepo.saveGarminToken(authToken)
            repo.getUserProfile()
            authRepo.clear()
        }
    }


}