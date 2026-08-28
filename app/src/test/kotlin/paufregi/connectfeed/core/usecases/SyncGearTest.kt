package paufregi.connectfeed.core.usecases

import com.google.common.truth.Truth.assertThat
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
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.user

class SyncGearTest {

    private val auth = mockk<AuthRepository>()
    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: SyncGear

    @Before
    fun setup() {
        useCase = SyncGear(auth, repo)
    }

    @After
    fun tearDown() {
        confirmVerified(auth, repo)
        clearAllMocks()
    }

    @Test
    fun `Sync gears`() = runTest {
        val apiGears = listOf(
            Gear(
                id = "gear-1",
                name = "gear 1",
                type = GearType.Shoe,
                distance = 1000
            ),
            Gear(
                id = "gear-2",
                name = "gear 2",
                type = GearType.Bike,
                distance = 2000
            )
        )
        val dbGears = listOf(
            Gear(
                id = "gear-1",
                name = "gear 1 old",
                type = GearType.Unknown,
                distance = null
            ),
            Gear(
                id = "gear-3",
                name = "gear 3",
                type = GearType.Shoe,
                distance = 3000
            )
        )

        every { auth.getUser() } returns flowOf(user)
        coEvery { repo.getGears() } returns Result.success(apiGears)
        every { repo.getAllGears(user) } returns flowOf(dbGears)
        coEvery { repo.saveGear(any(), any()) } returns Unit
        coEvery { repo.deleteGear(any(), any()) } returns Unit

        val res = useCase()

        assertThat(res.isSuccess).isTrue()

        verify { auth.getUser() }
        coVerify { repo.getGears() }
        verify { repo.getAllGears(user) }
        coVerify {
            repo.deleteGear(user, dbGears[1])
            repo.saveGear(user, apiGears[0])
            repo.saveGear(user, apiGears[1])
        }
    }

    @Test
    fun `Sync gears - empty api`() = runTest {
        val dbGears = listOf(
            Gear(id = "gear-1", name = "gear 1", type = GearType.Shoe, distance = 1000),
            Gear(id = "gear-2", name = "gear 2", type = GearType.Bike, distance = 2000)
        )

        every { auth.getUser() } returns flowOf(user)
        coEvery { repo.getGears() } returns Result.success(emptyList())
        every { repo.getAllGears(user) } returns flowOf(dbGears)
        coEvery { repo.saveGear(any(), any()) } returns Unit
        coEvery { repo.deleteGear(any(), any()) } returns Unit

        val res = useCase()

        assertThat(res.isSuccess).isTrue()

        verify { auth.getUser() }
        coVerify { repo.getGears() }
        coVerify(exactly = 2) { repo.deleteGear(user, any()) }
        coVerify(exactly = 0) { repo.saveGear(any(), any()) }
        verify { repo.getAllGears(user) }
    }

    @Test
    fun `Sync gears - no user`() = runTest {
        every { auth.getUser() } returns flowOf(null)

        val res = useCase()

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("User must be logged in")

        verify { auth.getUser() }
    }

    @Test
    fun `Sync gears - api failure`() = runTest {
        every { auth.getUser() } returns flowOf(user)
        coEvery { repo.getGears() } returns Result.failure(Exception("error"))

        val res = useCase()

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("error")

        verify { auth.getUser() }
        coVerify { repo.getGears() }
    }
}
