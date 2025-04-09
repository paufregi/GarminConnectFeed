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
            assertThat(state.username).isEmpty()
            assertThat(state.password).isEmpty()
            assertThat(state.user).isNull()
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        confirmVerified(signIn)
    }

    @Test
    fun `Set username`() = runTest {
        viewModel = LoginViewModel(signIn)

        viewModel.state.test {
            viewModel.onAction(LoginAction.SetUsername("user"))
            skipItems(1)
            var state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.username).isEqualTo("user")
            assertThat(state.password).isEmpty()
            assertThat(state.user).isNull()
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        confirmVerified(signIn)
    }

    @Test
    fun `Set password`() = runTest {
        viewModel = LoginViewModel(signIn)

        viewModel.state.test {
            viewModel.onAction(LoginAction.SetPassword("pass"))
            skipItems(1)
            var state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.username).isEmpty()
            assertThat(state.password).isEqualTo("pass")
            assertThat(state.user).isNull()
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        confirmVerified(signIn)
    }

    @Test
    fun `Show password`() = runTest {
        viewModel = LoginViewModel(signIn)

        viewModel.state.test {
            viewModel.onAction(LoginAction.ShowPassword(true))
            skipItems(1)
            var state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.username).isEmpty()
            assertThat(state.password).isEmpty()
            assertThat(state.user).isNull()
            assertThat(state.showPassword).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        confirmVerified(signIn)
    }

    @Test
    fun `Reset state`() = runTest {
        viewModel = LoginViewModel(signIn)

        viewModel.state.test {
            viewModel.onAction(LoginAction.SetUsername("user"))
            viewModel.onAction(LoginAction.SetPassword("pass"))
            viewModel.onAction(LoginAction.ShowPassword(true))
            viewModel.onAction(LoginAction.Reset)
            skipItems(4)
            var state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.username).isEmpty()
            assertThat(state.password).isEmpty()
            assertThat(state.user).isNull()
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        confirmVerified(signIn)
    }

    @Test
    fun `Sign in - success`() = runTest {
        val user = User("user", "avatar")
        coEvery { signIn(any(), any()) } returns Result.Success(user)

        viewModel = LoginViewModel(signIn)

        viewModel.state.test {
            viewModel.onAction(LoginAction.SetUsername("user"))
            viewModel.onAction(LoginAction.SetPassword("pass"))
            viewModel.onAction(LoginAction.SignIn)
            skipItems(3)
            var state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Success())
            assertThat(state.username).isEqualTo("user")
            assertThat(state.password).isEqualTo("pass")
            assertThat(state.user).isEqualTo(user)
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { signIn("user", "pass") }
        confirmVerified(signIn)
    }

    @Test
    fun `Sign in - failed`() = runTest {
        coEvery { signIn(any(), any()) } returns Result.Failure("error")

        viewModel = LoginViewModel(signIn)

        viewModel.state.test {
            viewModel.onAction(LoginAction.SetUsername("user"))
            viewModel.onAction(LoginAction.SetPassword("pass"))
            viewModel.onAction(LoginAction.SignIn)
            skipItems(3)
            var state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("error"))
            assertThat(state.username).isEqualTo("user")
            assertThat(state.password).isEqualTo("pass")
            assertThat(state.user).isNull()
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { signIn("user", "pass") }
        confirmVerified(signIn)
    }
}