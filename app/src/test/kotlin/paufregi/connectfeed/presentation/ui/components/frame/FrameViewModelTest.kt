package paufregi.connectfeed.presentation.ui.components.frame

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
import paufregi.connectfeed.core.usecases.GetUser
import paufregi.connectfeed.presentation.utils.MainDispatcherRule
import paufregi.connectfeed.user

@ExperimentalCoroutinesApi
class FrameViewModelTest {

    private val getUser = mockk<GetUser>()

    private lateinit var viewModel: FrameViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup(){

    }

    @After
    fun tearDown(){
        verify { getUser() }
        confirmVerified(getUser)
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        every { getUser() } returns flowOf(user)

        viewModel = FrameViewModel(getUser)

        viewModel.user.test {
            assertThat(awaitItem()).isEqualTo(user)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Initial state - no user`() = runTest {
        every { getUser() } returns flowOf(null)

        viewModel = FrameViewModel(getUser)

        viewModel.user.test {
            assertThat(awaitItem()).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }
}