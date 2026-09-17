package paufregi.connectfeed.core.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.createStravaToken
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.tomorrow

class ConnectStravaTest{
    private val repo = mockk<AuthRepository>()
    private lateinit var useCase: ConnectStrava

    @Before
    fun setup(){
        useCase = ConnectStrava(repo, "CLIENT_ID", "CLIENT_SECRET")
    }

    @After
    fun tearDown(){
        confirmVerified(repo)
        clearAllMocks()
    }

    @Test
    fun `Connect Strava`() = runTest {
        val token = createStravaToken(tomorrow)

        coEvery { repo.exchangeStravaToken (any(), any(), any()) } returns Result.success(token)
        coEvery { repo.saveStravaToken(any()) } returns Unit

        val result = useCase("code")

        assertThat(result.isSuccess).isTrue()

        coVerify {
            repo.exchangeStravaToken("CLIENT_ID", "CLIENT_SECRET", "code")
            repo.saveStravaToken(token)
        }
    }

    @Test
    fun `Exchange code - failure`() = runTest {
        coEvery { repo.exchangeStravaToken(any(), any(), any()) } returns Result.failure("error")

        val result = useCase("code")

        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("error")


        coVerify {
            repo.exchangeStravaToken("CLIENT_ID", "CLIENT_SECRET", "code")
        }
        coVerify { repo.exchangeStravaToken("CLIENT_ID", "CLIENT_SECRET", "code") }

    }
}