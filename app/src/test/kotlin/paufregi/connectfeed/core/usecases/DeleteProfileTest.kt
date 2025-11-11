package paufregi.connectfeed.core.usecases

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.user

class DeleteProfileTest {

    private val auth = mockk<AuthRepository>()
    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: DeleteProfile

    @Before
    fun setup(){
        useCase = DeleteProfile(auth, repo)
    }

    @After
    fun tearDown(){
        confirmVerified(auth, repo)
        clearAllMocks()
    }

    @Test
    fun `Delete profile`() = runTest {
        every { auth.getUser() } returns flowOf(user)
        coEvery { repo.deleteProfile(any(), any()) } returns Unit

        val profile = Profile()
        useCase(profile)

        verify { auth.getUser() }
        coVerify { repo.deleteProfile(user, profile) }
    }

    @Test
    fun `No user`() = runTest {
        every { auth.getUser() } returns flowOf(null)

        val profile = Profile()
        useCase(profile)

        verify { auth.getUser() }
    }
}