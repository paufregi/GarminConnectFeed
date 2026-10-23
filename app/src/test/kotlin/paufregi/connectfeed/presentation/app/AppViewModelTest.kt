package paufregi.connectfeed.presentation.app

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.usecases.GetUser
import paufregi.connectfeed.presentation.utils.MainDispatcherRule

@ExperimentalCoroutinesApi
class AppViewModelTest {

    private val getUser = mockk<GetUser>()

    private val user = User(1, "user", "avatar")

    private lateinit var viewModel: AppViewModel

    private fun createViewModel() = AppViewModel(getUser)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @After
    fun tearDown() {
        confirmVerified(getUser)
        clearAllMocks()
    }

    @Test
    fun `Initial state - authenticated`() = runTest {
        every { getUser() } returns flowOf(user)

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(AppState.Authenticated(user))
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
    }

    @Test
    fun `Initial state - not authenticated`() = runTest {
        every { getUser() } returns flowOf(null)

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(AppState.NotAuthenticated)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
    }
}
