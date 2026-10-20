package paufregi.connectfeed.presentation.strava

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.usecases.ConnectStrava
import paufregi.connectfeed.core.usecases.GetUser
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.presentation.ui.models.ProcState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule
import paufregi.connectfeed.user

@ExperimentalCoroutinesApi
class StravaViewModelTest {

    private val getUser = mockk<GetUser>()
    private val enableStrava = mockk<ConnectStrava>()

    private val code = "test-code"

    private lateinit var viewModel: StravaViewModel

    fun createViewModel() = StravaViewModel(getUser, enableStrava)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {}

    @After
    fun tearDown() {
        confirmVerified(getUser, enableStrava)
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        every { getUser() } returns flowOf(user)

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(ProcState.Running)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
    }

    @Test
    fun `Initial user`() = runTest {
        every { getUser() } returns flowOf(user)

        viewModel = createViewModel()

        viewModel.user.test {
            assertThat(awaitItem()).isEqualTo(user)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
    }

    @Test
    fun `Initial user - no user`() = runTest {
        every { getUser() } returns flowOf(null)

        viewModel = createViewModel()

        viewModel.user.test {
            assertThat(awaitItem()).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
    }

    @Test
    fun `Initial user - user updated`() = runTest {
        val userFlow = MutableStateFlow<User?>(null)
        every { getUser() } returns userFlow

        viewModel = createViewModel()

        viewModel.user.test {
            assertThat(awaitItem()).isNull()

            userFlow.value = user

            assertThat(awaitItem()).isEqualTo(user)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
    }

    @Test
    fun `Exchange token - success`() = runTest {
        val connectResult = CompletableDeferred<Result<Unit>>()
        every { getUser() } returns flowOf(user)
        coEvery { enableStrava(any()) } coAnswers { connectResult.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(ProcState.Running)
            viewModel.injectState(ProcState.Failure("previous"))
            assertThat(awaitItem()).isEqualTo(ProcState.Failure("previous"))

            viewModel.exchangeToken(code)

            assertThat(awaitItem()).isEqualTo(ProcState.Running)
            connectResult.complete(Result.success(Unit))
            assertThat(awaitItem()).isEqualTo(ProcState.Success("Strava linked"))
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
        coVerify { enableStrava(code) }
    }

    @Test
    fun `Exchange token - failure`() = runTest {
        val connectResult = CompletableDeferred<Result<Unit>>()
        every { getUser() } returns flowOf(user)
        coEvery { enableStrava(any()) } coAnswers { connectResult.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(ProcState.Running)
            viewModel.injectState(ProcState.Failure("previous"))
            assertThat(awaitItem()).isEqualTo(ProcState.Failure("previous"))

            viewModel.exchangeToken(code)

            assertThat(awaitItem()).isEqualTo(ProcState.Running)
            connectResult.complete(Result.failure("error"))
            assertThat(awaitItem()).isEqualTo(ProcState.Failure("error"))
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
        coVerify { enableStrava(code) }
    }

    @Test
    fun `Exchange token - failure without message`() = runTest {
        val connectResult = CompletableDeferred<Result<Unit>>()
        every { getUser() } returns flowOf(user)
        coEvery { enableStrava(any()) } coAnswers { connectResult.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(ProcState.Running)
            viewModel.injectState(ProcState.Failure("previous"))
            assertThat(awaitItem()).isEqualTo(ProcState.Failure("previous"))

            viewModel.exchangeToken(code)

            assertThat(awaitItem()).isEqualTo(ProcState.Running)
            connectResult.complete(Result.failure(Exception()))
            assertThat(awaitItem()).isEqualTo(ProcState.Failure("unknown error"))
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
        coVerify { enableStrava(code) }
    }
}
