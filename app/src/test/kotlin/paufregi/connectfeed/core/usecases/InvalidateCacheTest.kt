package paufregi.connectfeed.core.usecases

import io.mockk.clearAllMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.data.repository.GarminRepository

class InvalidateCacheTest {

    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: InvalidateCache

    @Before
    fun setup(){
        useCase = InvalidateCache(repo)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Invalidate cache`() = runTest {
        every { repo.invalidateCaches() } returns Unit
        useCase()
        verify { repo.invalidateCaches() }
        confirmVerified(repo)
    }
}