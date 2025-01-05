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
import paufregi.connectfeed.data.repository.GarminRepository

class ClearTokensTest{
    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: ClearTokens

    @Before
    fun setup(){
        useCase = ClearTokens(repo)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Clear tokens`() = runTest {
        coEvery { repo.deleteTokens() } returns Unit

        val res = useCase()

        assertThat(res).isInstanceOf(Result.Success(Unit).javaClass)
        coVerify {
            repo.deleteTokens()
        }
        confirmVerified(repo)
    }
}