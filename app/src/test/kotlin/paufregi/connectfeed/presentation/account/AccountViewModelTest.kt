package paufregi.connectfeed.presentation.account

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
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
        every { isStravaLoggedIn() } returns flowOf(true)
        viewModel = AccountViewModel(getUser, refreshUser, signOut, isStravaLoggedIn, disconnectStrava)
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.user).isEqualTo(user)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getUser() }
        confirmVerified(getUser, signOut)
    }

    @Test
    fun `Refresh user - success`() = runTest {
        coEvery { refreshUser() } returns Result.Success(Unit)
        every { isStravaLoggedIn() } returns flowOf(true)
        viewModel = AccountViewModel(getUser, refreshUser, signOut, isStravaLoggedIn, disconnectStrava)
        viewModel.onEvent(AccountEvent.RefreshUser)

        viewModel.state.test {
            var state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Success("User data refreshed"))
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getUser()
            refreshUser()
        }
        confirmVerified(getUser, signOut)
    }

    @Test
    fun `Refresh user - failure`() = runTest {
        coEvery { refreshUser() } returns Result.Failure("error")
        every { isStravaLoggedIn() } returns flowOf(true)
        viewModel = AccountViewModel(getUser, refreshUser, signOut, isStravaLoggedIn, disconnectStrava)
        viewModel.onEvent(AccountEvent.RefreshUser)

        viewModel.state.test {
            var state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("error"))
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getUser()
            refreshUser()
        }
        confirmVerified(getUser, signOut)
    }

    @Test
    fun `Sign out`() = runTest {
        coEvery { signOut() } returns Unit
        every { isStravaLoggedIn() } returns flowOf(true)
        viewModel = AccountViewModel(getUser, refreshUser, signOut, isStravaLoggedIn, disconnectStrava)
        viewModel.onEvent(AccountEvent.SignOut)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Processing)
            assertThat(state.user).isEqualTo(user)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getUser()
            signOut()
        }
        confirmVerified(getUser, signOut)
    }
}