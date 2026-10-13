package paufregi.connectfeed.presentation.app.settings

import android.net.Uri
import app.cash.turbine.ReceiveTurbine
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
import paufregi.connectfeed.core.usecases.IsStravaConnected
import paufregi.connectfeed.core.usecases.RefreshUser
import paufregi.connectfeed.core.usecases.SignOut
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.presentation.ui.components.notification.NotificationManager
import paufregi.connectfeed.presentation.utils.MainDispatcherRule
import paufregi.connectfeed.system.Downloader

@ExperimentalCoroutinesApi
class SettingsViewModelTest {

    private val getUser = mockk<GetUser>()
    private val refreshUser = mockk<RefreshUser>()
    private val signOut = mockk<SignOut>()
    private val isStravaConnected = mockk<IsStravaConnected>()
    private val disconnectStrava = mockk<DisconnectStrava>()
    private val notification = mockk<NotificationManager>(relaxed = true)
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
        SettingsViewModel(getUser, isStravaConnected, getLatestRelease, refreshUser, signOut, disconnectStrava, notification, currentVersion, downloader, stravaUri)

    private suspend fun ReceiveTurbine<SettingsState>.awaitStateWhere(predicate: (SettingsState) -> Boolean): SettingsState {
        repeat(10) {
            val state = awaitItem()
            if (predicate(state)) return state
        }
        error("Expected state was not emitted")
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup(){
        every { getUser() } returns flowOf(user)
    }

    @After
    fun tearDown(){
        confirmVerified(refreshUser, signOut, disconnectStrava, notification, stravaUri, getLatestRelease, downloader)
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        every { isStravaConnected() } returns flowOf(true)
        coEvery { getLatestRelease() } returns Result.success(release)

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitStateWhere {
                !it.loading && !it.updating && it.user == user && it.hasStrava == true && it.latestRelease == release
            }
            assertThat(state.loading).isFalse()
            assertThat(state.updating).isFalse()
            assertThat(state.user).isEqualTo(user)
            assertThat(state.hasStrava).isTrue()
            assertThat(state.currentVersion).isEqualTo(Version.parse(currentVersion))
            assertThat(state.latestRelease).isEqualTo(release)
            cancelAndIgnoreRemainingEvents()
        }
        coVerify { getLatestRelease() }
    }

    @Test
    fun `Initial state - no strava`() = runTest {
        every { isStravaConnected() } returns flowOf(false)
        coEvery { getLatestRelease() } returns Result.success(release)

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitStateWhere {
                !it.loading && !it.updating && it.user == user && it.hasStrava == false && it.latestRelease == release
            }
            assertThat(state.loading).isFalse()
            assertThat(state.updating).isFalse()
            assertThat(state.user).isEqualTo(user)
            assertThat(state.hasStrava).isFalse()
            assertThat(state.currentVersion).isEqualTo(Version.parse(currentVersion))
            assertThat(state.latestRelease).isEqualTo(release)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getLatestRelease() }
    }

    @Test
    fun `Initial state - fail to load latest release`() = runTest {
        every { isStravaConnected() } returns flowOf(false)
        coEvery { getLatestRelease() } returns Result.failure("error")

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitStateWhere {
                !it.loading && !it.updating && it.user == user && it.hasStrava == false && it.latestRelease == null
            }
            assertThat(state.loading).isFalse()
            assertThat(state.updating).isFalse()
            assertThat(state.user).isEqualTo(user)
            assertThat(state.hasStrava).isFalse()
            assertThat(state.currentVersion).isEqualTo(Version.parse(currentVersion))
            assertThat(state.latestRelease).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getLatestRelease() }
    }

    @Test
    fun `Refresh user - success`() = runTest {
        coEvery { refreshUser() } returns Result.success(Unit)
        every { isStravaConnected() } returns flowOf(true)
        coEvery { getLatestRelease() } returns Result.success(release)

        viewModel = createViewModel()

        viewModel.state.test {
            awaitStateWhere { !it.loading && !it.updating && it.latestRelease == release }
            viewModel.onAction(SettingsAction.RefreshUser)

            assertThat(awaitStateWhere { it.loading }.loading).isTrue()
            val state = awaitStateWhere { !it.loading && !it.updating && it.latestRelease == release }
            assertThat(state.loading).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            refreshUser()
            getLatestRelease()
        }
        verify { notification.show("User data refreshed") }
    }

    @Test
    fun `Refresh user - failure`() = runTest {
        coEvery { refreshUser() } returns Result.failure("error")
        every { isStravaConnected() } returns flowOf(true)
        coEvery { getLatestRelease() } returns Result.success(release)

        viewModel = createViewModel()

        viewModel.state.test {
            awaitStateWhere { !it.loading && !it.updating && it.latestRelease == release }
            viewModel.onAction(SettingsAction.RefreshUser)

            assertThat(awaitStateWhere { it.loading }.loading).isTrue()
            val state = awaitStateWhere { !it.loading && !it.updating && it.latestRelease == release }
            assertThat(state.loading).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            refreshUser()
            getLatestRelease()
        }
        verify { notification.show("Failed to refresh user data: error") }
    }

    @Test
    fun `Sign out`() = runTest {
        coEvery { signOut() } returns Unit
        coEvery { disconnectStrava() } returns Unit
        every { isStravaConnected() } returns flowOf(true)
        coEvery { getLatestRelease() } returns Result.success(release)

        viewModel = createViewModel()

        viewModel.state.test {
            awaitStateWhere { !it.loading && !it.updating && it.latestRelease == release }
            viewModel.onAction(SettingsAction.SignOut)

            assertThat(awaitStateWhere { it.loading }.loading).isTrue()
            val state = awaitStateWhere { !it.loading && !it.updating && it.user == user }
            assertThat(state.loading).isFalse()
            assertThat(state.user).isEqualTo(user)
            cancelAndIgnoreRemainingEvents()
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
        every { isStravaConnected() } returns flowOf(true)
        coEvery { getLatestRelease() } returns Result.success(release)

        viewModel = createViewModel()

        viewModel.state.test {
            awaitStateWhere { !it.loading && !it.updating && it.latestRelease == release }
            viewModel.onAction(SettingsAction.StravaDisconnect)

            assertThat(awaitStateWhere { it.loading }.loading).isTrue()
            val state = awaitStateWhere { !it.loading && !it.updating && it.latestRelease == release }
            assertThat(state.loading).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            disconnectStrava()
            getLatestRelease()
        }
    }

    @Test
    fun `Update action`() = runTest {
        every { isStravaConnected() } returns flowOf(true)
        coEvery { getLatestRelease() } returns Result.success(release)
        every { downloader.downloadApk(any()) } returns Result.success(Unit)

        viewModel = createViewModel()

        viewModel.state.test {
            awaitStateWhere { !it.loading && !it.updating && it.latestRelease == release }
            viewModel.onAction(SettingsAction.Update)

            val state = awaitStateWhere { it.updating }
            assertThat(state.updating).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getLatestRelease() }
        verify { downloader.downloadApk(release) }
    }
}