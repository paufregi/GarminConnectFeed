package paufregi.connectfeed.presentation.app.login

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
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

    private val username = "test-username"
    private val password = "test-password"
    private val user = User(1, "Test User", "https://example.com/avatar.png")

    private lateinit var viewModel: LoginViewModel

    fun createViewModel() = LoginViewModel(signIn, saveUser)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {}

    @After
    fun tearDown() {
        confirmVerified(signIn, saveUser)
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        viewModel = createViewModel()

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
        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(LoginState())
            viewModel.onAction(LoginAction.SetUsername(username))

            val state = awaitItem()
            assertThat(state.status).isNull()
            assertThat(state.username).isEqualTo(username)
            assertThat(state.password).isEmpty()
            assertThat(state.user).isNull()
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set password`() = runTest {
        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(LoginState())
            viewModel.onAction(LoginAction.SetPassword(password))

            val state = awaitItem()
            assertThat(state.status).isNull()
            assertThat(state.username).isEmpty()
            assertThat(state.password).isEqualTo(password)
            assertThat(state.user).isNull()
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Show password`() = runTest {
        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(LoginState())
            viewModel.onAction(LoginAction.ShowPassword(true))

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
    fun `Show password - hide`() = runTest {
        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(LoginState())
            viewModel.injectState(LoginState(username = username, password = password, showPassword = true))
            assertThat(awaitItem().showPassword).isTrue()

            viewModel.onAction(LoginAction.ShowPassword(false))

            val state = awaitItem()
            assertThat(state.status).isNull()
            assertThat(state.username).isEqualTo(username)
            assertThat(state.password).isEqualTo(password)
            assertThat(state.user).isNull()
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Reset`() = runTest {
        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(LoginState())
            viewModel.injectState(
                LoginState(
                    status = Status.Failure("error"),
                    username = username,
                    password = password,
                    user = user,
                    showPassword = true,
                )
            )
            assertThat(awaitItem().status).isEqualTo(Status.Failure("error"))

            viewModel.onAction(LoginAction.Reset)

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
        val signInResult = CompletableDeferred<Result<User>>()
        coEvery { signIn(any(), any()) } coAnswers { signInResult.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(LoginState())
            viewModel.injectState(LoginState(username = username, password = password))
            assertThat(awaitItem().status).isNull()

            viewModel.onAction(LoginAction.SignIn)

            assertThat(awaitItem().status).isEqualTo(Status.Loading)
            signInResult.complete(Result.success(user))
            val state = awaitItem()
            assertThat(state.status).isEqualTo(Status.Success)
            assertThat(state.username).isEqualTo(username)
            assertThat(state.password).isEqualTo(password)
            assertThat(state.user).isEqualTo(user)
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { signIn(username, password) }
    }

    @Test
    fun `Sign in - failure`() = runTest {
        val signInResult = CompletableDeferred<Result<User>>()
        coEvery { signIn(any(), any()) } coAnswers { signInResult.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(LoginState())
            viewModel.injectState(LoginState(username = username, password = password))
            assertThat(awaitItem().status).isNull()

            viewModel.onAction(LoginAction.SignIn)

            assertThat(awaitItem().status).isEqualTo(Status.Loading)
            signInResult.complete(Result.failure("error"))
            val state = awaitItem()
            assertThat(state.status).isEqualTo(Status.Failure("error"))
            assertThat(state.username).isEqualTo(username)
            assertThat(state.password).isEqualTo(password)
            assertThat(state.user).isNull()
            assertThat(state.showPassword).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { signIn(username, password) }
    }

    @Test
    fun `Sign in - failure without message`() = runTest {
        val signInResult = CompletableDeferred<Result<User>>()
        coEvery { signIn(any(), any()) } coAnswers { signInResult.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(LoginState())
            viewModel.injectState(LoginState(username = username, password = password))
            assertThat(awaitItem().status).isNull()

            viewModel.onAction(LoginAction.SignIn)

            assertThat(awaitItem().status).isEqualTo(Status.Loading)
            signInResult.complete(Result.failure(Exception()))
            val state = awaitItem()
            assertThat(state.status).isEqualTo(Status.Failure("Something went wrong: unknown error"))
            assertThat(state.user).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { signIn(username, password) }
    }

    @Test
    fun `Save user`() = runTest {
        coEvery { saveUser(any()) } returns Unit

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(LoginState())
            viewModel.injectState(LoginState(status = Status.Success, username = username, password = password, user = user))
            assertThat(awaitItem().user).isEqualTo(user)

            viewModel.onAction(LoginAction.SaveUser)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { saveUser(user) }
    }

    @Test
    fun `Save user - no user`() = runTest {
        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(LoginState())
            viewModel.onAction(LoginAction.SaveUser)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }
}
