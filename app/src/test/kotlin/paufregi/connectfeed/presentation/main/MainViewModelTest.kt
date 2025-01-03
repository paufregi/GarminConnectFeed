package paufregi.connectfeed.presentation.main

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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.core.usecases.IsLoggedIn
import paufregi.connectfeed.presentation.utils.MainDispatcherRule

@ExperimentalCoroutinesApi
class MainViewModelTest {

    private val isLoggedIn = mockk<IsLoggedIn>()

    private lateinit var viewModel: MainViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup(){

    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        every { isLoggedIn() } returns flowOf(true)

        viewModel = MainViewModel(isLoggedIn)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.loggedIn).isTrue()
            assertThat(state.showLogin).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        verify { isLoggedIn() }
        confirmVerified(isLoggedIn)
    }

    @Test
    fun `Show login`() = runTest {
        every { isLoggedIn() } returns flowOf(true)

        viewModel = MainViewModel(isLoggedIn)
        viewModel.showLogin()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.loggedIn).isTrue()
            assertThat(state.showLogin).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        verify { isLoggedIn() }
        confirmVerified(isLoggedIn)
    }

    @Test
    fun `Hide login`() = runTest {
        every { isLoggedIn() } returns flowOf(true)

        viewModel = MainViewModel(isLoggedIn)
        viewModel.hideLogin()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.loggedIn).isTrue()
            assertThat(state.showLogin).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        verify { isLoggedIn() }
        confirmVerified(isLoggedIn)
    }
}