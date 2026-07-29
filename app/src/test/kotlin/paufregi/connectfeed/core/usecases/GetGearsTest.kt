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
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.repository.GarminRepository

class GetGearsTest {

    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: GetGears

    @Before
    fun setup(){
        useCase = GetGears(repo)
    }

    @After
    fun tearDown(){
        confirmVerified(repo)
        clearAllMocks()
    }

    @Test
    fun `Get gears`() = runTest {
        val gears = listOf(
            Gear("ID_1", "Bike", GearType.Bike, 1L),
            Gear("ID_2", "Shoe", GearType.Shoe, 2L)
        )
        coEvery { repo.getGears() } returns Result.success(gears)
        val res = useCase()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(gears)
        coVerify { repo.getGears() }
    }

    @Test
    fun `Get gears - failure`() = runTest {
        coEvery { repo.getGears() } returns Result.failure("Failed")
        val res = useCase()

        assertThat(res.isSuccess).isFalse()
        coVerify { repo.getGears() }
    }
}