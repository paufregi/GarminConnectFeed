package paufregi.connectfeed.presentation.app.login

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
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.usecases.SaveUser
import paufregi.connectfeed.core.usecases.SignIn
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.presentation.utils.MainDispatcherRule

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private val signIn = mockk<SignIn>()
    private val saveUser = mockk<SaveUser>()

    private lateinit var viewModel: LoginViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
    }

    @After
    fun tearDown() {
        confirmVerified(signIn, saveUser)
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        viewModel = LoginViewModel(signIn, saveUser)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.status).isNull()
            assertThat(state.username).isEmpty()
            assertThat(state.password).isEmpty()
            assertThat(state.user).isNull()
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set username`() = runTest {
        viewModel = LoginViewModel(signIn, saveUser)

        viewModel.state.test {
            viewModel.onAction(LoginAction.SetUsername("user"))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.status).isNull()
            assertThat(state.username).isEqualTo("user")
            assertThat(state.password).isEmpty()
            assertThat(state.user).isNull()
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set password`() = runTest {
        viewModel = LoginViewModel(signIn, saveUser)

        viewModel.state.test {
            viewModel.onAction(LoginAction.SetPassword("pass"))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.status).isNull()
            assertThat(state.username).isEmpty()
            assertThat(state.password).isEqualTo("pass")
            assertThat(state.user).isNull()
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Show password`() = runTest {
        viewModel = LoginViewModel(signIn, saveUser)

        viewModel.state.test {
            viewModel.onAction(LoginAction.ShowPassword(true))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.status).isNull()
            assertThat(state.username).isEmpty()
            assertThat(state.password).isEmpty()
            assertThat(state.user).isNull()
            assertThat(state.showPassword).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Reset state`() = runTest {
        viewModel = LoginViewModel(signIn, saveUser)

        viewModel.state.test {
            viewModel.onAction(LoginAction.SetUsername("user"))
            viewModel.onAction(LoginAction.SetPassword("pass"))
            viewModel.onAction(LoginAction.ShowPassword(true))
            viewModel.onAction(LoginAction.Reset)
            skipItems(4)
            val state = awaitItem()
            assertThat(state.status).isNull()
            assertThat(state.username).isEmpty()
            assertThat(state.password).isEmpty()
            assertThat(state.user).isNull()
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Sign in - success`() = runTest {
        val user = User(1, "user", "avatar")
        coEvery { signIn(any(), any()) } returns Result.success(user)

        viewModel = LoginViewModel(signIn, saveUser)

        viewModel.state.test {
            viewModel.onAction(LoginAction.SetUsername("user"))
            viewModel.onAction(LoginAction.SetPassword("pass"))
            viewModel.onAction(LoginAction.SignIn)
            skipItems(3)
            val state = awaitItem()
            assertThat(state.status).isEqualTo(Status.Success)
            assertThat(state.username).isEqualTo("user")
            assertThat(state.password).isEqualTo("pass")
            assertThat(state.user).isEqualTo(user)
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { signIn("user", "pass") }
    }

    @Test
    fun `Sign in - failed`() = runTest {
        coEvery { signIn(any(), any()) } returns Result.failure("error")

        viewModel = LoginViewModel(signIn, saveUser)

        viewModel.state.test {
            viewModel.onAction(LoginAction.SetUsername("user"))
            viewModel.onAction(LoginAction.SetPassword("pass"))
            viewModel.onAction(LoginAction.SignIn)
            skipItems(3)
            val state = awaitItem()
            assertThat(state.status).isEqualTo(Status.Failure("error"))
            assertThat(state.username).isEqualTo("user")
            assertThat(state.password).isEqualTo("pass")
            assertThat(state.user).isNull()
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { signIn("user", "pass") }
    }

    @Test
    fun `Save user`() = runTest {
        val user = User(1, "user", "avatar")
        coEvery { signIn(any(), any()) } returns Result.success(user)
        coEvery { saveUser(any()) } returns Unit

        viewModel = LoginViewModel(signIn, saveUser)

        viewModel.state.test {
            viewModel.onAction(LoginAction.SetUsername("user"))
            viewModel.onAction(LoginAction.SetPassword("pass"))
            skipItems(2)
            viewModel.onAction(LoginAction.SignIn)
            skipItems(2)
            viewModel.onAction(LoginAction.SaveUser)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { signIn("user", "pass") }
        coVerify { saveUser(user) }
    }

    @Test
    fun `Save user - no user`() = runTest {
        val user = User(1, "user", "avatar")
        coEvery { saveUser(any()) } returns Unit

        viewModel = LoginViewModel(signIn, saveUser)

        viewModel.onAction(LoginAction.SaveUser)
    }
}