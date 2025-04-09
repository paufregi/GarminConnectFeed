package paufregi.connectfeed.presentation.account

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.usecases.DisconnectStrava
import paufregi.connectfeed.core.usecases.GetUser
import paufregi.connectfeed.core.usecases.IsStravaLoggedIn
import paufregi.connectfeed.core.usecases.RefreshUser
import paufregi.connectfeed.core.usecases.SignOut
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule

@ExperimentalCoroutinesApi
class AccountViewModelTest {

    private val getUser = mockk<GetUser>()
    private val refreshUser = mockk<RefreshUser>()
    private val signOut = mockk<SignOut>()
    private val isStravaLoggedIn = mockk<IsStravaLoggedIn>()
    private val disconnectStrava = mockk<DisconnectStrava>()

    private lateinit var viewModel: AccountViewModel

    private val user = User("user", "url")

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup(){
        coEvery { getUser() } returns flowOf(user)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        every { getUser() } returns flowOf(user)
        every { isStravaLoggedIn() } returns flowOf(true)

        viewModel = AccountViewModel(getUser, isStravaLoggedIn, refreshUser, signOut, disconnectStrava)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.user).isEqualTo(user)
            cancelAndIgnoreRemainingEvents()
        }

        verify {
            getUser()
            isStravaLoggedIn()
        }
        confirmVerified(getUser, isStravaLoggedIn, refreshUser, signOut, disconnectStrava)
    }

    @Test
    fun `Refresh user - success`() = runTest {
        coEvery { refreshUser() } returns Result.Success(Unit)
        every { getUser() } returns flowOf(user)
        every { isStravaLoggedIn() } returns flowOf(true)

        viewModel = AccountViewModel(getUser, isStravaLoggedIn, refreshUser, signOut, disconnectStrava)

        viewModel.state.test {
            viewModel.onAction(AccountAction.RefreshUser)
            skipItems(1)
            var state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Success("User data refreshed"))
            cancelAndIgnoreRemainingEvents()
        }

        verify {
            getUser()
            isStravaLoggedIn()
        }
        coVerify { refreshUser() }
        confirmVerified(getUser, isStravaLoggedIn, refreshUser, signOut, disconnectStrava)
    }

    @Test
    fun `Refresh user - failure`() = runTest {
        coEvery { refreshUser() } returns Result.Failure("error")
        every { getUser() } returns flowOf(user)
        every { isStravaLoggedIn() } returns flowOf(true)

        viewModel = AccountViewModel(getUser, isStravaLoggedIn, refreshUser, signOut, disconnectStrava)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(AccountAction.RefreshUser)
            var state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("error"))
            cancelAndIgnoreRemainingEvents()
        }

        verify {
            getUser()
            isStravaLoggedIn()
        }
        coVerify { refreshUser() }
        confirmVerified(getUser, isStravaLoggedIn, refreshUser, signOut, disconnectStrava)
    }

    @Test
    fun `Sign out`() = runTest {
        coEvery { signOut() } returns Unit
        every { getUser() } returns flowOf(user)
        every { isStravaLoggedIn() } returns flowOf(true)

        viewModel = AccountViewModel(getUser, isStravaLoggedIn, refreshUser, signOut, disconnectStrava)

        viewModel.state.test {
            viewModel.onAction(AccountAction.SignOut)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.user).isEqualTo(user)
            cancelAndIgnoreRemainingEvents()
        }

        verify {
            getUser()
            isStravaLoggedIn()
        }
        coVerify { signOut() }
        confirmVerified(getUser, isStravaLoggedIn, refreshUser, signOut, disconnectStrava)
    }

    @Test
    fun `Sign out strava`() = runTest {
        coEvery { disconnectStrava() } returns Unit
        every { getUser() } returns flowOf(user)
        every { isStravaLoggedIn() } returns flowOf(true)

        viewModel = AccountViewModel(getUser, isStravaLoggedIn, refreshUser, signOut, disconnectStrava)

        viewModel.state.test {
            viewModel.onAction(AccountAction.StravaDisconnect)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            cancelAndIgnoreRemainingEvents()
        }

        verify {
            getUser()
            isStravaLoggedIn()
        }
        coVerify { disconnectStrava() }
        confirmVerified(getUser, isStravaLoggedIn, refreshUser, signOut, disconnectStrava)
    }
}