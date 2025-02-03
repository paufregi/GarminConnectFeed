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
import paufregi.connectfeed.data.repository.StravaAuthRepository

class SaveStravaCodeTest{
    private val repo = mockk<StravaAuthRepository>()
    private lateinit var useCase: SaveStravaCode

    @Before
    fun setup(){
        useCase = SaveStravaCode(repo)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Save Strava code use-case`() = runTest {
        val code = "strava_code"
        coEvery { repo.saveCode(any()) } returns Unit

        useCase(code)

        coVerify { repo.saveCode(code) }
        confirmVerified(repo)
    }
}