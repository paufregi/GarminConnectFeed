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
import paufregi.connectfeed.core.models.Credential
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.data.repository.GarminRepository

class SaveCredentialTest{
    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: SaveCredential

    @Before
    fun setup(){
        useCase = SaveCredential(repo)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Save credential`() = runTest {
        val credential = Credential("user", "pass")
        coEvery { repo.saveCredential(any()) } returns Unit
        coEvery { repo.clearCache() } returns Unit
        val res = useCase(credential)

        assertThat(res).isInstanceOf(Result.Success(Unit).javaClass)
        coVerify {
            repo.saveCredential(credential)
            repo.clearCache()
        }
        confirmVerified(repo)
    }

    @Test
    fun `Invalid - No username`() = runTest {
        val credential = Credential("", "pass")
        val res = useCase(credential)

        assertThat(res).isInstanceOf(Result.Failure<Unit>("Validation error").javaClass)
    }

    @Test
    fun `Invalid - No pass`() = runTest {
        val credential = Credential("user", "")
        val res = useCase(credential)

        assertThat(res).isInstanceOf(Result.Failure<Unit>("Validation error").javaClass)
    }

    @Test
    fun `Invalid - all blank`() = runTest {
        val credential = Credential("", "")
        val res = useCase(credential)

        assertThat(res).isInstanceOf(Result.Failure<Unit>("Validation error").javaClass)
    }
}