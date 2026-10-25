package paufregi.connectfeed.core.usecases

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.repository.AppRepository
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.user

@ExperimentalCoroutinesApi
class GetGearsTest {
    private val auth = mockk<AuthRepository>()
    private val repo = mockk<AppRepository>()
    private lateinit var useCase: GetGears

    @Before
    fun setup() {
        useCase = GetGears(auth, repo)
    }

    @After
    fun tearDown() {
        confirmVerified(auth, repo)
        clearAllMocks()
    }

    @Test
    fun `Get gears`() = runTest {
        val gears = listOf(
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

        every { auth.getUser() } returns flowOf(user)
        every { repo.getAllGears(user) } returns flowOf(gears)

        val res = useCase()
        res.test {
            assertThat(awaitItem()).isEqualTo(gears)
            cancelAndIgnoreRemainingEvents()
        }

        verify { auth.getUser() }
        verify { repo.getAllGears(user) }
    }

    @Test
    fun `Get gears - empty list`() = runTest {
        every { auth.getUser() } returns flowOf(user)
        every { repo.getAllGears(user) } returns flowOf(emptyList())

        val res = useCase()
        res.test {
            assertThat(awaitItem()).isEqualTo(emptyList<Gear>())
            cancelAndIgnoreRemainingEvents()
        }

        verify { auth.getUser() }
        verify { repo.getAllGears(user) }
    }

    @Test
    fun `Get gears - no user`() = runTest {
        every { auth.getUser() } returns flowOf(null)

        val res = useCase()
        res.test {
            assertThat(awaitItem()).isEqualTo(emptyList<Gear>())
            cancelAndIgnoreRemainingEvents()
        }

        verify { auth.getUser() }
    }

    @Test
    fun `Get gears - user switch drops previous stream`() = runTest {
        val otherUser = user.copy(id = user.id + 1, name = "other")
        val userGear = Gear(id = "gear-1", name = "gear 1", type = GearType.Shoe)
        val otherGear = Gear(id = "gear-2", name = "gear 2", type = GearType.Bike)
        val users = MutableStateFlow<User?>(user)
        val userGears = MutableStateFlow(listOf(userGear))

        every { auth.getUser() } returns users
        every { repo.getAllGears(user) } returns userGears
        every { repo.getAllGears(otherUser) } returns flowOf(listOf(otherGear))

        val res = useCase()
        res.test {
            assertThat(awaitItem()).isEqualTo(listOf(userGear))

            users.value = otherUser
            assertThat(awaitItem()).isEqualTo(listOf(otherGear))

            userGears.value = emptyList()
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }

        verify { auth.getUser() }
        verify { repo.getAllGears(user) }
        verify { repo.getAllGears(otherUser) }
    }
}
