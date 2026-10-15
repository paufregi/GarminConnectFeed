package paufregi.connectfeed.presentation.app

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
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

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `Maps user changes to authentication state`() = runTest {
        val user = User(1, "user", "avatar")
        val userFlow = MutableStateFlow<User?>(user)
        every { getUser() } returns userFlow

        val viewModel = AppViewModel(getUser)

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(AppState.Authenticated(user))
            userFlow.value = null
            assertThat(awaitItem()).isEqualTo(AppState.NotAuthenticated)
            userFlow.value = user
            assertThat(awaitItem()).isEqualTo(AppState.Authenticated(user))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Starts in loading state`() = runTest {
        every { getUser() } returns emptyFlow()

        val viewModel = AppViewModel(getUser)

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(AppState.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
