package paufregi.connectfeed.presentation.password

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.usecases.SignIn
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule

@ExperimentalCoroutinesApi
class PasswordViewModelTest {

    private val signIn = mockk<SignIn>()
    private val getCredential = mockk<GetCredential>()

    private lateinit var viewModel: PasswordViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup(){}

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        coEvery { getCredential() } returns flowOf(Credential("user", "pass"))
        viewModel = PasswordViewModel(getCredential, signIn)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.credential.username).isEqualTo("user")
            assertThat(state.credential.password).isEqualTo("pass")
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getCredential() }
        confirmVerified(getCredential, signIn)
    }

    @Test
    fun `Set password`() = runTest {
        coEvery { getCredential() } returns flowOf(Credential("user", "pass"))
        viewModel = PasswordViewModel(getCredential, signIn)

        viewModel.state.test {
            awaitItem() // Skip initial state
            viewModel.onEvent(PasswordEvent.SetPassword("newPass"))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.credential.username).isEqualTo("user")
            assertThat(state.credential.password).isEqualTo("newPass")
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getCredential() }
        confirmVerified(getCredential, signIn)
    }

    @Test
    fun `Show password`() = runTest {
        coEvery { getCredential() } returns flowOf(Credential("user", "pass"))
        viewModel = PasswordViewModel(getCredential, signIn)

        viewModel.state.test {
            awaitItem() // Skip initial state
            viewModel.onEvent(PasswordEvent.ShowPassword(true))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.credential.username).isEqualTo("user")
            assertThat(state.credential.password).isEqualTo("pass")
            assertThat(state.showPassword).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getCredential() }
        confirmVerified(getCredential, signIn)
    }

    @Test
    fun `Sign in - success`() = runTest {
        coEvery { getCredential() } returns flowOf(Credential("user", "pass"))
        coEvery { signIn(any()) } returns Result.Success(User("user", "avatar"))
        viewModel = PasswordViewModel(getCredential, signIn)

        viewModel.state.test {
            awaitItem() // Skip initial state
            viewModel.onEvent(PasswordEvent.SignIn)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Success("user"))
            assertThat(state.credential.username).isEqualTo("user")
            assertThat(state.credential.password).isEqualTo("pass")
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getCredential()
            signIn(Credential("user", "pass"))
        }
        confirmVerified(getCredential, signIn)
    }

    @Test
    fun `Sign in - failed`() = runTest {
        coEvery { getCredential() } returns flowOf(Credential("user", "pass"))
        coEvery { signIn(any()) } returns Result.Failure("error")
        viewModel = PasswordViewModel(getCredential, signIn)

        viewModel.state.test {
            awaitItem() // Skip initial state
            viewModel.onEvent(PasswordEvent.SignIn)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("error"))
            assertThat(state.credential.username).isEqualTo("user")
            assertThat(state.credential.password).isEqualTo("pass")
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getCredential()
            signIn(Credential("user", "pass"))
        }
        confirmVerified(signIn)
    }
}