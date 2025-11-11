package paufregi.connectfeed.presentation.strava

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.core.usecases.StravaCodeExchange
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule

@ExperimentalCoroutinesApi
class StravaViewModelTest {

    private val stravaCodeExchange = mockk<StravaCodeExchange>()

    private lateinit var viewModel: StravaViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup(){
        viewModel = StravaViewModel(stravaCodeExchange)
    }

    @After
    fun tearDown(){
        confirmVerified(stravaCodeExchange)
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Exchange token`() = runTest {
        coEvery { stravaCodeExchange(any()) } returns Result.success(Unit)

        viewModel.state.test {
            viewModel.exchangeToken("code")
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Success("Strava linked"))
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { stravaCodeExchange("code") }
    }

    @Test
    fun `Exchange token - failure`() = runTest {
        coEvery { stravaCodeExchange(any()) } returns Result.failure("error")

        viewModel.state.test {
            viewModel.exchangeToken("code")
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Link failed"))
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { stravaCodeExchange("code") }
    }
}