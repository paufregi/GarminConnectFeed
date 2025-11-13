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
        confirmVerified(isLoggedIn)
        clearAllMocks()
    }

    @Test
    fun `Initial state - Logged in`() = runTest {
        every { isLoggedIn() } returns flowOf(true)

        viewModel = MainViewModel(isLoggedIn)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.loggedIn).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        verify { isLoggedIn() }
    }

    @Test
    fun `Initial state - Logged out`() = runTest {
        every { isLoggedIn() } returns flowOf(false)

        viewModel = MainViewModel(isLoggedIn)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.loggedIn).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        verify { isLoggedIn() }
    }
}