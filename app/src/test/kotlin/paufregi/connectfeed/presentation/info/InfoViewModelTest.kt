package paufregi.connectfeed.presentation.info

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
import paufregi.connectfeed.core.models.Release
import paufregi.connectfeed.core.models.Version
import paufregi.connectfeed.core.usecases.GetLatestRelease
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule
import paufregi.connectfeed.system.Downloader

@ExperimentalCoroutinesApi
class InfoViewModelTest {
    private val getLatestRelease = mockk<GetLatestRelease>()
    private val downloader = mockk<Downloader>()
    private val currentVersion = "1.2.3"
    private val release = Release(
        version = Version(1, 2, 3),
        downloadUrl = "http://example.com/app.apk"
    )

    private lateinit var viewModel: InfoViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        viewModel = InfoViewModel(
            version = currentVersion,
            downloader = downloader,
            getLatestRelease = getLatestRelease
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.currentVersion).isEqualTo(Version.parse(currentVersion))
            assertThat(state.latestRelease).isNull()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
        }
        confirmVerified(
            getLatestRelease,
            downloader
        )
    }

    @Test
    fun `Check update`() = runTest {
        coEvery { getLatestRelease.invoke() } returns Result.success(release)

        viewModel.onAction(InfoAction.CheckUpdate)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.latestRelease).isEqualTo(release)
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            cancelAndIgnoreRemainingEvents()
        }
        coVerify { getLatestRelease() }
        confirmVerified(
            getLatestRelease,
            downloader
        )
    }

    @Test
    fun `Check update - failure`() = runTest {
        coEvery { getLatestRelease.invoke() } returns Result.failure(Exception("error"))

        viewModel.onAction(InfoAction.CheckUpdate)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.latestRelease).isNull()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't get latest release"))
            cancelAndIgnoreRemainingEvents()
        }
        coVerify { getLatestRelease() }
        confirmVerified(
            getLatestRelease,
            downloader
        )
    }

    @Test
    fun `Update action`() = runTest {
        viewModel = InfoViewModel(
            version = currentVersion,
            downloader = downloader,
            getLatestRelease = getLatestRelease
        )
        coEvery { getLatestRelease() } returns Result.success(release)
        coEvery { downloader.downloadApk(any()) } returns Result.success(Unit)

        viewModel.onAction(InfoAction.CheckUpdate)

        viewModel.state.value.copy(latestRelease = release)

        viewModel.onAction(InfoAction.Update)

        coVerify {
            downloader.downloadApk(release)
            getLatestRelease()
        }
        confirmVerified(
            getLatestRelease,
            downloader
        )
    }
}