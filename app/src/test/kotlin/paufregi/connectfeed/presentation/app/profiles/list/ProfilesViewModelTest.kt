package paufregi.connectfeed.presentation.app.profiles.list

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test
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

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `Combines profiles into state and clears loading`() = runTest {
        val profiles = listOf(Profile(id = 1, name = "Run"))
        every { getProfiles() } returns flowOf(profiles)

        val viewModel = ProfilesViewModel(getProfiles, deleteProfile, notification)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.isLoading).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Delete profile notifies after deletion completes`() = runTest {
        val profile = Profile(id = 1, name = "Run")
        val deletion = CompletableDeferred<Unit>()
        every { getProfiles() } returns flowOf(listOf(profile))
        coEvery { deleteProfile(profile) } coAnswers { deletion.await() }

        val viewModel = ProfilesViewModel(getProfiles, deleteProfile, notification)
        viewModel.state.test {
            awaitItem()
            viewModel.onAction(ProfilesAction.Delete(profile))
            coVerify { deleteProfile(profile) }
            verify(exactly = 0) { notification.show(any()) }
            deletion.complete(Unit)
            verify { notification.show("Profile deleted") }
            cancelAndIgnoreRemainingEvents()
        }
    }
}
