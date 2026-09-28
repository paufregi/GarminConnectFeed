package paufregi.connectfeed.presentation.settings

import io.mockk.clearAllMocks
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.usecases.DeleteProfile
import paufregi.connectfeed.core.usecases.GetProfiles
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
        verify { getProfiles() }
        confirmVerified(getProfiles, deleteProfile)
        clearAllMocks()
    }
}