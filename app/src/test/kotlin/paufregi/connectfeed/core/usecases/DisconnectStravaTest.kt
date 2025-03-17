package paufregi.connectfeed.core.usecases

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.data.repository.StravaAuthRepository

class DisconnectStravaTest {

    private val repo = mockk<StravaAuthRepository>()
    private lateinit var useCase: DisconnectStrava

    @Before
    fun setup(){
        useCase = DisconnectStrava(repo)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Disconnect strava`() = runTest {
        coEvery { repo.clear() } returns Unit
        useCase()
        coVerify { repo.clear() }
        confirmVerified(repo)
    }
}