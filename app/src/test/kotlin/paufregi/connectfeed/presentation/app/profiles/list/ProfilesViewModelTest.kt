package paufregi.connectfeed.presentation.app.profiles.list

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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.usecases.DeleteProfile
import paufregi.connectfeed.core.usecases.GetProfiles
import paufregi.connectfeed.presentation.ui.components.notification.NotificationManager
import paufregi.connectfeed.presentation.utils.MainDispatcherRule

@ExperimentalCoroutinesApi
class ProfilesViewModelTest {

    private val getProfiles = mockk<GetProfiles>()
    private val deleteProfile = mockk<DeleteProfile>()
    private val notification = mockk<NotificationManager>(relaxed = true)

    private val profile = Profile(id = 1, name = "Run", type = ActivityType.Running)
    private val otherProfile = Profile(id = 2, name = "Ride", type = ActivityType.Cycling)

    private lateinit var viewModel: ProfilesViewModel

    fun createViewModel() = ProfilesViewModel(getProfiles, deleteProfile, notification)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @After
    fun tearDown() {
        confirmVerified(getProfiles, deleteProfile, notification)
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        every { getProfiles() } returns flowOf(listOf(profile, otherProfile))

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.profiles).containsExactly(profile, otherProfile).inOrder()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getProfiles() }
    }

    @Test
    fun `Initial state - no profiles`() = runTest {
        every { getProfiles() } returns flowOf(emptyList())

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.profiles).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getProfiles() }
    }

    @Test
    fun `Initial state - loading`() = runTest {
        val profilesResult = CompletableDeferred<List<Profile>>()
        every { getProfiles() } returns flow { emit(profilesResult.await()) }

        viewModel = createViewModel()

        viewModel.state.test {
            val loading = awaitItem()
            assertThat(loading.isLoading).isTrue()
            assertThat(loading.profiles).isEmpty()

            profilesResult.complete(listOf(profile))

            val state = awaitItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.profiles).containsExactly(profile)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getProfiles() }
    }

    @Test
    fun `Delete profile`() = runTest {
        val deletion = CompletableDeferred<Unit>()
        every { getProfiles() } returns flowOf(listOf(profile, otherProfile))
        coEvery { deleteProfile(profile) } coAnswers { deletion.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ProfilesAction.Delete(profile))

            verify(exactly = 0) { notification.show(any()) }
            deletion.complete(Unit)
            verify { notification.show("Profile deleted") }
            cancelAndIgnoreRemainingEvents()
        }

        verify { getProfiles() }
        coVerify { deleteProfile(profile) }
    }

    @Test
    fun `Delete profile - no user`() = runTest {
        every { getProfiles() } returns flowOf(listOf(profile))
        coEvery { deleteProfile(profile) } returns null

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ProfilesAction.Delete(profile))
            cancelAndIgnoreRemainingEvents()
        }

        verify { getProfiles() }
        verify { notification.show("Profile deleted") }
        coVerify { deleteProfile(profile) }
    }
}
