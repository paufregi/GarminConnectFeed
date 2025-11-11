package paufregi.connectfeed.presentation.settings

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
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.usecases.DeleteProfile
import paufregi.connectfeed.core.usecases.GetProfiles
import paufregi.connectfeed.presentation.profiles.ProfileAction
import paufregi.connectfeed.presentation.profiles.ProfilesViewModel
import paufregi.connectfeed.presentation.utils.MainDispatcherRule

@ExperimentalCoroutinesApi
class ProfilesViewModelTest {

    private val getProfiles = mockk<GetProfiles>()
    private val deleteProfile = mockk<DeleteProfile>()

    lateinit var viewModel: ProfilesViewModel

    val profiles = listOf(
        Profile(id = 1, name = "Profile 1"),
        Profile(id = 2, name = "Profile 2"),
    )

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup(){

    }

    @After
    fun tearDown(){
        confirmVerified(getProfiles, deleteProfile)
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        every { getProfiles() } returns flowOf(profiles)

        viewModel = ProfilesViewModel(getProfiles, deleteProfile)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.profiles).isEqualTo(profiles)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getProfiles() }
    }

    @Test
    fun `Initial state - empty list`() = runTest {
        every { getProfiles() } returns flowOf(emptyList())

        viewModel = ProfilesViewModel(getProfiles, deleteProfile)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.profiles).isEqualTo(emptyList<Profile>())
            cancelAndIgnoreRemainingEvents()
        }

        verify { getProfiles() }
    }

    @Test
    fun `Delete profile`() = runTest {
        every { getProfiles() } returns flowOf(profiles)
        coEvery { deleteProfile(any()) } returns Unit

        viewModel = ProfilesViewModel(getProfiles, deleteProfile)

        viewModel.onAction(ProfileAction.Delete(profiles[0]))

        verify { getProfiles() }
        coVerify { deleteProfile(profiles[0]) }
    }
}