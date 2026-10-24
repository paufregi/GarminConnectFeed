package paufregi.connectfeed.presentation.app.settings

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
import kotlinx.coroutines.CompletableDeferred
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

    private fun createViewModel() =
        SettingsViewModel(getUser, isStravaConnected, getLatestRelease, refreshUser, signOut, disconnectStrava, notification, currentVersion, downloader, stravaUri)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        every { getUser() } returns flowOf(user)
    }

    @After
    fun tearDown() {
        confirmVerified(getUser, refreshUser, signOut, isStravaConnected, disconnectStrava, notification, stravaUri, getLatestRelease, downloader)
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        every { isStravaConnected() } returns flowOf(true)
        coEvery { getLatestRelease() } returns Result.success(release)

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.loading).isFalse()
            assertThat(state.updating).isFalse()
            assertThat(state.user).isEqualTo(user)
            assertThat(state.hasStrava).isTrue()
            assertThat(state.currentVersion).isEqualTo(Version.parse(currentVersion))
            assertThat(state.latestRelease).isEqualTo(release)
            cancelAndIgnoreRemainingEvents()
        }
        verify { getUser() }
        verify { isStravaConnected() }
        coVerify { getLatestRelease() }
    }

    @Test
    fun `Initial state - no strava`() = runTest {
        every { isStravaConnected() } returns flowOf(false)
        coEvery { getLatestRelease() } returns Result.success(release)

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.loading).isFalse()
            assertThat(state.updating).isFalse()
            assertThat(state.user).isEqualTo(user)
            assertThat(state.hasStrava).isFalse()
            assertThat(state.currentVersion).isEqualTo(Version.parse(currentVersion))
            assertThat(state.latestRelease).isEqualTo(release)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
        verify { isStravaConnected() }
        coVerify { getLatestRelease() }
    }

    @Test
    fun `Initial state - fail to load latest release`() = runTest {
        every { isStravaConnected() } returns flowOf(false)
        coEvery { getLatestRelease() } returns Result.failure("error")

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.loading).isFalse()
            assertThat(state.updating).isFalse()
            assertThat(state.user).isEqualTo(user)
            assertThat(state.hasStrava).isFalse()
            assertThat(state.currentVersion).isEqualTo(Version.parse(currentVersion))
            assertThat(state.latestRelease).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
        verify { isStravaConnected() }
        coVerify { getLatestRelease() }
    }

    @Test
    fun `Initial state - loading latest release`() = runTest {
        val result = CompletableDeferred<Result<Release>>()
        every { isStravaConnected() } returns flowOf(true)
        coEvery { getLatestRelease() } coAnswers { result.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            val initial = awaitItem()
            assertThat(initial.loading).isFalse()
            assertThat(initial.updating).isTrue()
            assertThat(initial.user).isEqualTo(user)
            assertThat(initial.hasStrava).isTrue()
            assertThat(initial.latestRelease).isNull()

            result.complete(Result.success(release))

            val loaded = awaitItem()
            assertThat(loaded.updating).isFalse()
            assertThat(loaded.latestRelease).isEqualTo(release)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
        verify { isStravaConnected() }
        coVerify { getLatestRelease() }
    }

    @Test
    fun `Refresh user - success`() = runTest {
        val syncResult = CompletableDeferred<Result<Unit>>()
        coEvery { refreshUser() } coAnswers { syncResult.await() }
        every { isStravaConnected() } returns flowOf(true)
        coEvery { getLatestRelease() } returns Result.success(release)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(SettingsAction.RefreshUser)

            assertThat(awaitItem().loading).isTrue()
            syncResult.complete(Result.success(Unit))
            assertThat(awaitItem().loading).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { refreshUser() }
        coVerify { getLatestRelease() }
        verify { getUser() }
        verify { isStravaConnected() }
        verify { notification.show("User data refreshed") }
    }

    @Test
    fun `Refresh user - failure`() = runTest {
        val syncResult = CompletableDeferred<Result<Unit>>()
        coEvery { refreshUser() } coAnswers { syncResult.await() }
        every { isStravaConnected() } returns flowOf(true)
        coEvery { getLatestRelease() } returns Result.success(release)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(SettingsAction.RefreshUser)

            assertThat(awaitItem().loading).isTrue()
            syncResult.complete(Result.failure(Exception("error")))
            assertThat(awaitItem().loading).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { refreshUser() }
        coVerify { getLatestRelease() }
        verify { getUser() }
        verify { isStravaConnected() }
        verify { notification.show("Failed to refresh user data: error") }
    }

    @Test
    fun `Sign out`() = runTest {
        val disconnect = CompletableDeferred<Unit>()
        coEvery { signOut() } coAnswers { disconnect.await() }
        coEvery { disconnectStrava() } coAnswers { disconnect.await() }
        every { isStravaConnected() } returns flowOf(true)
        coEvery { getLatestRelease() } returns Result.success(release)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(SettingsAction.SignOut)

            assertThat(awaitItem().loading).isTrue()
            disconnect.complete(Unit)
            val state = awaitItem()
            assertThat(state.loading).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { signOut() }
        coVerify { disconnectStrava() }
        coVerify { getLatestRelease() }
        verify { getUser() }
        verify { isStravaConnected() }
    }

    @Test
    fun `Sign out strava`() = runTest {
        val disconnect = CompletableDeferred<Unit>()
        coEvery { disconnectStrava() } coAnswers { disconnect.await() }
        every { isStravaConnected() } returns flowOf(true)
        coEvery { getLatestRelease() } returns Result.success(release)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(SettingsAction.StravaDisconnect)

            assertThat(awaitItem().loading).isTrue()
            disconnect.complete(Unit)
            assertThat(awaitItem().loading).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { disconnectStrava() }
        coVerify { getLatestRelease() }
        verify { getUser() }
        verify { isStravaConnected() }
    }

    @Test
    fun `Update - success`() = runTest {
        every { isStravaConnected() } returns flowOf(true)
        coEvery { getLatestRelease() } returns Result.success(release)
        every { downloader.downloadApk(any()) } returns Result.success(Unit)

        viewModel = createViewModel()

        viewModel.state.test {
            awaitItem()
            viewModel.onAction(SettingsAction.Update)

            val state = awaitItem()
            assertThat(state.updating).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getLatestRelease() }
        verify { getUser() }
        verify { isStravaConnected() }
        verify { downloader.downloadApk(release) }
    }

    @Test
    fun `Update - no latest release`() = runTest {
        every { isStravaConnected() } returns flowOf(true)
        coEvery { getLatestRelease() } returns Result.failure("error")

        viewModel = createViewModel()

        viewModel.state.test {
            awaitItem()
            viewModel.onAction(SettingsAction.Update)

            expectNoEvents()
            assertThat(viewModel.state.value.updating).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
        verify { isStravaConnected() }
        coVerify { getLatestRelease() }
    }

    @Test
    fun `Update - failure`() = runTest {
        every { isStravaConnected() } returns flowOf(true)
        coEvery { getLatestRelease() } returns Result.success(release)
        every { downloader.downloadApk(release) } returns Result.failure("error")

        viewModel = createViewModel()

        viewModel.state.test {
            awaitItem()
            viewModel.onAction(SettingsAction.Update)

            assertThat(awaitItem().updating).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
        verify { isStravaConnected() }
        verify { downloader.downloadApk(release) }
        coVerify { getLatestRelease() }
    }

    @Test
    fun `Reset`() = runTest {
        every { isStravaConnected() } returns flowOf(true)
        coEvery { getLatestRelease() } returns Result.success(release)
        val initial = SettingsState(
            loading = true,
            updating = true,
            currentVersion = Version.parse(currentVersion),
            latestRelease = release,
        )

        viewModel = createViewModel()

        viewModel.state.test {
            awaitItem()
            viewModel.injectState(initial)
            assertThat(awaitItem().loading).isTrue()

            viewModel.onAction(SettingsAction.Reset)

            val state = awaitItem()
            assertThat(state.loading).isFalse()
            assertThat(state.updating).isTrue()
            assertThat(state.user).isEqualTo(user)
            assertThat(state.hasStrava).isTrue()
            assertThat(state.currentVersion).isEqualTo(Version.parse(currentVersion))
            assertThat(state.latestRelease).isEqualTo(release)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getUser() }
        verify { isStravaConnected() }
        coVerify { getLatestRelease() }
    }
}