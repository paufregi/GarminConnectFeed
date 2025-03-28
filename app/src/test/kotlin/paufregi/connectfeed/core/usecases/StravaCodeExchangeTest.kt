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
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.createStravaToken
import paufregi.connectfeed.data.repository.StravaAuthRepository
import paufregi.connectfeed.tomorrow

class StravaCodeExchangeTest{
    private val repo = mockk<StravaAuthRepository>()
    private lateinit var useCase: StravaCodeExchange

    @Before
    fun setup(){
        useCase = StravaCodeExchange(repo, "CLIENT_ID", "CLIENT_SECRET")
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Exchange code`() = runTest{
        val token = createStravaToken(tomorrow)

        coEvery { repo.exchange(any(), any(), any()) } returns Result.Success(token)
        coEvery { repo.saveToken(any()) } returns Unit

        val result = useCase("code")

        assertThat(result.isSuccessful).isTrue()

        coVerify {
            repo.exchange("CLIENT_ID", "CLIENT_SECRET", "code")
            repo.saveToken(token)
        }
        confirmVerified(repo)
    }

    @Test
    fun `Exchange code - failure`() = runTest{
        coEvery { repo.exchange(any(), any(), any()) } returns Result.Failure("error")

        val result = useCase("code")

        assertThat(result.isSuccessful).isFalse()
        result as Result.Failure
        assertThat(result.reason).isEqualTo("error")


        coVerify {
            repo.exchange("CLIENT_ID", "CLIENT_SECRET", "code")
        }
        coVerify { repo.exchange("CLIENT_ID", "CLIENT_SECRET", "code") }
        confirmVerified(repo)

    }

}