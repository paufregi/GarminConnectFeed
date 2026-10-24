package paufregi.connectfeed.presentation.syncweight

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.models.Weight
import paufregi.connectfeed.core.usecases.GetUser
import paufregi.connectfeed.core.usecases.SyncWeight
import paufregi.connectfeed.core.utils.RenphoReader
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.presentation.ui.models.Status
import paufregi.connectfeed.presentation.utils.MainDispatcherRule
import paufregi.connectfeed.user
import java.io.ByteArrayInputStream
import java.util.Date

@ExperimentalCoroutinesApi
class SyncWeightViewModelTest {

    private val getUser = mockk<GetUser>()
    private val syncWeight = mockk<SyncWeight>()

    private val inputStream = ByteArrayInputStream("weights".toByteArray())
    private val weights = listOf(
        Weight(
            timestamp = Date(1_700_000_000_000),
            weight = 70.5f,
            bmi = 22.1f,
            fat = 15.2f,
            visceralFat = 5,
            water = 60.3f,
            muscle = 55.4f,
            bone = 3.1f,
            basalMet = 1650f,
            metabolicAge = 30,
        )
    )

    private lateinit var viewModel: SyncWeightViewModel

    fun createViewModel() = SyncWeightViewModel(getUser, syncWeight)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        mockkObject(RenphoReader)
    }

    @After
    fun tearDown() {
        confirmVerified(getUser, syncWeight, RenphoReader)
        clearAllMocks()
        unmockkObject(RenphoReader)
    }

    @Test
    fun `Initial state`() = runTest {
        every { getUser() } returns flowOf(user)

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(Status.Loading)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
    }

    @Test
    fun `Initial user`() = runTest {
        every { getUser() } returns flowOf(user)

        viewModel = createViewModel()

        viewModel.user.test {
            assertThat(awaitItem()).isEqualTo(user)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
    }

    @Test
    fun `Initial user - no user`() = runTest {
        every { getUser() } returns flowOf(null)

        viewModel = createViewModel()

        viewModel.user.test {
            assertThat(awaitItem()).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
    }

    @Test
    fun `Initial user - user updated`() = runTest {
        val userFlow = MutableStateFlow<User?>(null)
        every { getUser() } returns userFlow

        viewModel = createViewModel()

        viewModel.user.test {
            assertThat(awaitItem()).isNull()

            userFlow.value = user

            assertThat(awaitItem()).isEqualTo(user)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
    }

    @Test
    fun `Sync weight - success`() = runTest {
        val syncResult = CompletableDeferred<Result<Unit>>()
        every { getUser() } returns flowOf(user)
        every { RenphoReader.read(any()) } returns Result.success(weights)
        coEvery { syncWeight(any(), any()) } coAnswers { syncResult.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(Status.Loading)
            viewModel.injectState(Status.Failure("previous"))
            assertThat(awaitItem()).isEqualTo(Status.Failure("previous"))

            viewModel.updateWeight(inputStream)

            assertThat(awaitItem()).isEqualTo(Status.Loading)
            syncResult.complete(Result.success(Unit))
            assertThat(awaitItem()).isEqualTo(Status.Success("Sync succeeded"))
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
        verify { RenphoReader.read(inputStream) }
        coVerify { syncWeight(weights, any()) }
    }

    @Test
    fun `Sync weight - failure`() = runTest {
        val syncResult = CompletableDeferred<Result<Unit>>()
        every { getUser() } returns flowOf(user)
        every { RenphoReader.read(any()) } returns Result.success(weights)
        coEvery { syncWeight(any(), any()) } coAnswers { syncResult.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(Status.Loading)
            viewModel.injectState(Status.Failure("previous"))
            assertThat(awaitItem()).isEqualTo(Status.Failure("previous"))

            viewModel.updateWeight(inputStream)

            assertThat(awaitItem()).isEqualTo(Status.Loading)
            syncResult.complete(Result.failure("error"))
            assertThat(awaitItem()).isEqualTo(Status.Failure("error"))
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
        verify { RenphoReader.read(inputStream) }
        coVerify { syncWeight(weights, any()) }
    }

    @Test
    fun `Sync weight - failure without message`() = runTest {
        val syncResult = CompletableDeferred<Result<Unit>>()
        every { getUser() } returns flowOf(user)
        every { RenphoReader.read(any()) } returns Result.success(weights)
        coEvery { syncWeight(any(), any()) } coAnswers { syncResult.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(Status.Loading)
            viewModel.injectState(Status.Failure("previous"))
            assertThat(awaitItem()).isEqualTo(Status.Failure("previous"))

            viewModel.updateWeight(inputStream)

            assertThat(awaitItem()).isEqualTo(Status.Loading)
            syncResult.complete(Result.failure(Exception()))
            assertThat(awaitItem()).isEqualTo(Status.Failure("Unknown error"))
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
        verify { RenphoReader.read(inputStream) }
        coVerify { syncWeight(weights, any()) }
    }

    @Test
    fun `Sync weight - no input stream`() = runTest {
        every { getUser() } returns flowOf(user)

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(Status.Loading)

            viewModel.updateWeight(null)

            assertThat(awaitItem()).isEqualTo(Status.Failure("nothing to sync"))
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
    }

    @Test
    fun `Sync weight - read error`() = runTest {
        every { getUser() } returns flowOf(user)
        every { RenphoReader.read(any()) } returns Result.failure("error")

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(Status.Loading)

            viewModel.updateWeight(inputStream)

            assertThat(awaitItem()).isEqualTo(Status.Failure("failed to read file"))
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
        verify { RenphoReader.read(inputStream) }
    }

    @Test
    fun `Sync weight - empty file`() = runTest {
        every { getUser() } returns flowOf(user)
        every { RenphoReader.read(any()) } returns Result.success(emptyList())

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(Status.Loading)

            viewModel.updateWeight(inputStream)

            assertThat(awaitItem()).isEqualTo(Status.Failure("nothing to sync"))
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
        verify { RenphoReader.read(inputStream) }
    }
}
