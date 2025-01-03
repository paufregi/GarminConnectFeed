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
import paufregi.connectfeed.core.usecases.RefreshTokens
import paufregi.connectfeed.core.usecases.SignOut
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule

@ExperimentalCoroutinesApi
class AccountViewModelTest {

    private val getUser = mockk<GetUser>()
    private val refreshTokens = mockk<RefreshTokens>()
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
        viewModel = AccountViewModel(getUser, refreshTokens, signOut)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.user).isEqualTo(user)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getUser() }
        confirmVerified(getUser, refreshTokens, signOut)
    }

    @Test
    fun `Refresh tokens - success`() = runTest {
        coEvery { refreshTokens() } returns Result.Success(Unit)
        viewModel = AccountViewModel(getUser, refreshTokens, signOut)
        viewModel.onEvent(AccountEvent.RefreshTokens)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Success("Tokens refreshed"))
            assertThat(state.user).isEqualTo(user)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getUser()
            refreshTokens()
        }
        confirmVerified(getUser, refreshTokens, signOut)
    }

    @Test
    fun `Refresh tokens - failure`() = runTest {
        coEvery { refreshTokens() } returns Result.Failure<Unit>("error")
        viewModel = AccountViewModel(getUser, refreshTokens, signOut)
        viewModel.onEvent(AccountEvent.RefreshTokens)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("error"))
            assertThat(state.user).isEqualTo(user)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getUser()
            refreshTokens()
        }
        confirmVerified(getUser, refreshTokens, signOut)
    }

    @Test
    fun `Sign out`() = runTest {
        coEvery { signOut() } returns Unit
        viewModel = AccountViewModel(getUser, refreshTokens, signOut)
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
        confirmVerified(getUser, refreshTokens, signOut)
    }
}