package paufregi.connectfeed.presentation.settings

import android.net.Uri
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
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
import paufregi.connectfeed.core.models.Release
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.models.Version
import paufregi.connectfeed.core.usecases.DisconnectStrava
import paufregi.connectfeed.core.usecases.GetLatestRelease
import paufregi.connectfeed.core.usecases.GetUser
import paufregi.connectfeed.core.usecases.IsStravaLoggedIn
import paufregi.connectfeed.core.usecases.RefreshUser
import paufregi.connectfeed.core.usecases.SignOut
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule
import paufregi.connectfeed.system.Downloader

@ExperimentalCoroutinesApi
class SettingsViewModelTest {

    private val getUser = mockk<GetUser>()
    private val refreshUser = mockk<RefreshUser>()
    private val signOut = mockk<SignOut>()
    private val isStravaLoggedIn = mockk<IsStravaLoggedIn>()
    private val disconnectStrava = mockk<DisconnectStrava>()
    private val stravaUri = mockk<Uri>()
    private val getLatestRelease = mockk<GetLatestRelease>()
    private val downloader = mockk<Downloader>()

    private val user = User(1, "user", "url")
    private val currentVersion = "1.2.3"
    private val release = Release(
        version = Version(1, 2, 3),
        downloadUrl = "http://example.com/app.apk"
    )

    private lateinit var viewModel: SettingsViewModel

    fun createViewModel() =
        SettingsViewModel(getUser, isStravaLoggedIn, getLatestRelease, refreshUser, signOut, disconnectStrava, currentVersion, downloader, stravaUri)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup(){
        coEvery { getUser() } returns flowOf(user)
    }

    @After
    fun tearDown(){
        confirmVerified(getUser, refreshUser, signOut, isStravaLoggedIn, disconnectStrava, stravaUri, getLatestRelease, downloader)
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        every { getUser() } returns flowOf(user)
        every { isStravaLoggedIn() } returns flowOf(true)
        coEvery { getLatestRelease() } returns Result.success(release)

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.user).isEqualTo(user)
            assertThat(state.hasStrava).isTrue()
            assertThat(state.currentVersion).isEqualTo(Version.parse(currentVersion))
            assertThat(state.latestRelease).isEqualTo(release)
            cancelAndIgnoreRemainingEvents()
        }

        verify {
            getUser()
            isStravaLoggedIn()
        }
        coVerify { getLatestRelease() }
    }

    @Test
    fun `Initial state - no strava`() = runTest {
        every { getUser() } returns flowOf(user)
        every { isStravaLoggedIn() } returns flowOf(false)
        coEvery { getLatestRelease() } returns Result.success(release)

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.user).isEqualTo(user)
            assertThat(state.hasStrava).isFalse()
            assertThat(state.currentVersion).isEqualTo(Version.parse(currentVersion))
            assertThat(state.latestRelease).isEqualTo(release)
            cancelAndIgnoreRemainingEvents()
        }

        verify {
            getUser()
            isStravaLoggedIn()
        }
        coVerify { getLatestRelease() }
    }

    @Test
    fun `Initial state - fail to load latest release`() = runTest {
        every { getUser() } returns flowOf(user)
        every { isStravaLoggedIn() } returns flowOf(false)
        coEvery { getLatestRelease() } returns Result.failure("error")

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.user).isEqualTo(user)
            assertThat(state.hasStrava).isFalse()
            assertThat(state.currentVersion).isEqualTo(Version.parse(currentVersion))
            assertThat(state.latestRelease).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        verify {
            getUser()
            isStravaLoggedIn()
        }
        coVerify { getLatestRelease() }
    }

    @Test
    fun `Refresh user - success`() = runTest {
        coEvery { refreshUser() } returns Result.success(Unit)
        every { getUser() } returns flowOf(user)
        every { isStravaLoggedIn() } returns flowOf(true)
        coEvery { getLatestRelease() } returns Result.success(release)

        viewModel = createViewModel()

        viewModel.state.test {
            viewModel.onAction(SettingsAction.RefreshUser)
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Success("User data refreshed"))
            cancelAndIgnoreRemainingEvents()
        }

        verify {
            getUser()
            isStravaLoggedIn()
        }
        coVerify {
            refreshUser()
            getLatestRelease()
        }
    }

    @Test
    fun `Refresh user - failure`() = runTest {
        coEvery { refreshUser() } returns Result.failure("error")
        every { getUser() } returns flowOf(user)
        every { isStravaLoggedIn() } returns flowOf(true)
        coEvery { getLatestRelease() } returns Result.success(release)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(SettingsAction.RefreshUser)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("error"))
            cancelAndIgnoreRemainingEvents()
        }

        verify {
            getUser()
            isStravaLoggedIn()
        }
        coVerify {
            refreshUser()
            getLatestRelease()
        }
    }

    @Test
    fun `Sign out`() = runTest {
        coEvery { signOut() } returns Unit
        coEvery { disconnectStrava() } returns Unit
        every { getUser() } returns flowOf(user)
        every { isStravaLoggedIn() } returns flowOf(true)
        coEvery { getLatestRelease() } returns Result.success(release)

        viewModel = createViewModel()

        viewModel.state.test {
            viewModel.onAction(SettingsAction.SignOut)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.user).isEqualTo(user)
            cancelAndIgnoreRemainingEvents()
        }

        verify {
            getUser()
            isStravaLoggedIn()
        }
        coVerify {
            signOut()
            disconnectStrava()
            getLatestRelease()
        }
    }

    @Test
    fun `Sign out strava`() = runTest {
        coEvery { disconnectStrava() } returns Unit
        every { getUser() } returns flowOf(user)
        every { isStravaLoggedIn() } returns flowOf(true)
        coEvery { getLatestRelease() } returns Result.success(release)

        viewModel = createViewModel()

        viewModel.state.test {
            viewModel.onAction(SettingsAction.StravaDisconnect)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            cancelAndIgnoreRemainingEvents()
        }

        verify {
            getUser()
            isStravaLoggedIn()
        }
        coVerify {
            disconnectStrava()
            getLatestRelease()
        }
    }

    @Test
    fun `Update action`() = runTest {
        coEvery { disconnectStrava() } returns Unit
        every { getUser() } returns flowOf(user)
        every { isStravaLoggedIn() } returns flowOf(true)
        coEvery { getLatestRelease() } returns Result.success(release)
        coEvery { downloader.downloadApk(any()) } returns Result.success(Unit)

        viewModel = createViewModel()

        viewModel.state.test {
            viewModel.onAction(SettingsAction.Update)
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.onAction(SettingsAction.Update)

        verify {
            getUser()
            isStravaLoggedIn()
        }
        coVerify {
            downloader.downloadApk(release)
            getLatestRelease()
        }
    }
}