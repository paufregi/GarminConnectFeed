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
import paufregi.connectfeed.data.repository.AuthRepository

class DisconnectStravaTest {

    private val repo = mockk<AuthRepository>()
    private lateinit var useCase: DisconnectStrava

    @Before
    fun setup(){
        useCase = DisconnectStrava(repo)
    }

    @After
    fun tearDown(){
        confirmVerified(repo)
        clearAllMocks()
    }

    @Test
    fun `Disconnect strava`() = runTest {
        coEvery { repo.clearStravaToken() } returns Unit
        useCase()
        coVerify { repo.clearStravaToken() }
    }
}