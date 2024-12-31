package paufregi.connectfeed.presentation.login

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.core.models.Credential
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.usecases.SignIn
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private val signIn = mockk<SignIn>()

    private lateinit var viewModel: LoginViewModel

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
        viewModel = LoginViewModel(signIn)

        viewModel.state.test {
            var state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.credential.username).isEmpty()
            assertThat(state.credential.password).isEmpty()
            assertThat(state.user).isNull()
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        confirmVerified(signIn)
    }

    @Test
    fun `Set username`() = runTest {
        viewModel = LoginViewModel(signIn)
        viewModel.onEvent(LoginEvent.SetUsername("user"))

        viewModel.state.test {
            var state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.credential.username).isEqualTo("user")
            assertThat(state.credential.password).isEmpty()
            assertThat(state.user).isNull()
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        confirmVerified(signIn)
    }

    @Test
    fun `Set password`() = runTest {
        viewModel = LoginViewModel(signIn)
        viewModel.onEvent(LoginEvent.SetPassword("pass"))

        viewModel.state.test {
            var state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.credential.username).isEmpty()
            assertThat(state.credential.password).isEqualTo("pass")
            assertThat(state.user).isNull()
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        confirmVerified(signIn)
    }

    @Test
    fun `Show password`() = runTest {
        viewModel = LoginViewModel(signIn)
        viewModel.onEvent(LoginEvent.ShowPassword(true))

        viewModel.state.test {
            var state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.credential.username).isEmpty()
            assertThat(state.credential.password).isEmpty()
            assertThat(state.user).isNull()
            assertThat(state.showPassword).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        confirmVerified(signIn)
    }

    @Test
    fun `Reset state`() = runTest {
        viewModel = LoginViewModel(signIn)
        viewModel.onEvent(LoginEvent.SetUsername("user"))
        viewModel.onEvent(LoginEvent.SetPassword("pass"))
        viewModel.onEvent(LoginEvent.ShowPassword(true))
        viewModel.onEvent(LoginEvent.Reset)

        viewModel.state.test {
            var state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.credential.username).isEmpty()
            assertThat(state.credential.password).isEmpty()
            assertThat(state.user).isNull()
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        confirmVerified(signIn)
    }

    @Test
    fun `Sign in - success`() = runTest {
        val user = User("user", "avatar")
        coEvery { signIn(any()) } returns Result.Success(user)
        viewModel = LoginViewModel(signIn)
        viewModel.onEvent(LoginEvent.SetUsername("user"))
        viewModel.onEvent(LoginEvent.SetPassword("pass"))

        viewModel.state.test {
            assertThat(awaitItem().process).isEqualTo(ProcessState.Idle)
            viewModel.onEvent(LoginEvent.SignIn)
            var state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Success("user"))
            assertThat(state.credential.username).isEqualTo("user")
            assertThat(state.credential.password).isEqualTo("pass")
            assertThat(state.user).isEqualTo(user)
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { signIn(Credential("user", "pass")) }
        confirmVerified(signIn)
    }

    @Test
    fun `Sign in - failed`() = runTest {
        coEvery { signIn(any()) } returns Result.Failure("error")
        viewModel = LoginViewModel(signIn)
        viewModel.onEvent(LoginEvent.SetUsername("user"))
        viewModel.onEvent(LoginEvent.SetPassword("pass"))

        viewModel.state.test {
            assertThat(awaitItem().process).isEqualTo(ProcessState.Idle)
            viewModel.onEvent(LoginEvent.SignIn)
            var state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("error"))
            assertThat(state.credential.username).isEqualTo("user")
            assertThat(state.credential.password).isEqualTo("pass")
            assertThat(state.user).isNull()
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { signIn(Credential("user", "pass")) }
        confirmVerified(signIn)
    }
}