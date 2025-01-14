package paufregi.connectfeed.presentation.account

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
import paufregi.connectfeed.core.usecases.GetUser
import paufregi.connectfeed.core.usecases.RefreshUser
import paufregi.connectfeed.core.usecases.SignOut
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule

@ExperimentalCoroutinesApi
class AccountViewModelTest {

    private val getUser = mockk<GetUser>()
    private val refreshUser = mockk<RefreshUser>()
    private val clearTokens = mockk<ClearTokens>()
    private val signOut = mockk<SignOut>()

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
        viewModel = AccountViewModel(getUser, refreshUser, clearTokens, signOut)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.user).isEqualTo(user)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getUser() }
        confirmVerified(getUser, clearTokens, signOut)
    }

    @Test
    fun `Refresh user - success`() = runTest {
        coEvery { refreshUser() } returns Result.Success(Unit)
        viewModel = AccountViewModel(getUser, refreshUser, clearTokens, signOut)
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
        confirmVerified(getUser, clearTokens, signOut)
    }

    @Test
    fun `Refresh user - failure`() = runTest {
        coEvery { refreshUser() } returns Result.Failure("error")
        viewModel = AccountViewModel(getUser, refreshUser, clearTokens, signOut)
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
        confirmVerified(getUser, clearTokens, signOut)
    }

    @Test
    fun `Clear tokens`() = runTest {
        coEvery { clearTokens() } returns Unit
        viewModel = AccountViewModel(getUser, refreshUser, clearTokens, signOut)
        viewModel.onEvent(AccountEvent.ClearTokens)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Success("Tokens cleared"))
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getUser()
            clearTokens()
        }
        confirmVerified(getUser, clearTokens, signOut)
    }

    @Test
    fun `Sign out`() = runTest {
        coEvery { signOut() } returns Unit
        viewModel = AccountViewModel(getUser, refreshUser, clearTokens, signOut)
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
        confirmVerified(getUser, clearTokens, signOut)
    }
}