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
import paufregi.connectfeed.data.repository.AppRepository
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.user

class SyncGearTest {

    private val auth = mockk<AuthRepository>()
    private val appRepo = mockk<AppRepository>()
    private val garminRepo = mockk<GarminRepository>()
    private lateinit var useCase: SyncGear

    @Before
    fun setup() {
        useCase = SyncGear(auth, appRepo, garminRepo)
    }

    @After
    fun tearDown() {
        confirmVerified(auth, appRepo, garminRepo)
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
        coEvery { garminRepo.getGears() } returns Result.success(apiGears)
        every { appRepo.getAllGears(user) } returns flowOf(dbGears)
        coEvery { appRepo.saveGear(any(), any()) } returns Unit
        coEvery { appRepo.deleteGear(any(), any()) } returns Unit

        val res = useCase()

        assertThat(res.isSuccess).isTrue()

        verify { auth.getUser() }
        coVerify { garminRepo.getGears() }
        verify { appRepo.getAllGears(user) }
        coVerify {
            appRepo.deleteGear(user, dbGears[1])
            appRepo.saveGear(user, apiGears[0])
            appRepo.saveGear(user, apiGears[1])
        }
    }

    @Test
    fun `Sync gears - empty api`() = runTest {
        val dbGears = listOf(
            Gear(id = "gear-1", name = "gear 1", type = GearType.Shoe, distance = 1000),
            Gear(id = "gear-2", name = "gear 2", type = GearType.Bike, distance = 2000)
        )

        every { auth.getUser() } returns flowOf(user)
        coEvery { garminRepo.getGears() } returns Result.success(emptyList())
        every { appRepo.getAllGears(user) } returns flowOf(dbGears)
        coEvery { appRepo.saveGear(any(), any()) } returns Unit
        coEvery { appRepo.deleteGear(any(), any()) } returns Unit

        val res = useCase()

        assertThat(res.isSuccess).isTrue()

        verify { auth.getUser() }
        coVerify { garminRepo.getGears() }
        coVerify(exactly = 2) { appRepo.deleteGear(user, any()) }
        verify { appRepo.getAllGears(user) }
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
        coEvery { garminRepo.getGears() } returns Result.failure(Exception("error"))

        val res = useCase()

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("error")

        verify { auth.getUser() }
        coVerify { garminRepo.getGears() }
    }
}
