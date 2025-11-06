package paufregi.connectfeed.core.usecases

import app.cash.turbine.test
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
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.models.ProfileType
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.user

class GetProfilesTest{
    private val auth = mockk<AuthRepository>()
    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: GetProfiles

    @Before
    fun setup(){
        useCase = GetProfiles(auth, repo)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Get profiles`() = runTest {
        val profiles = listOf(
            Profile(
                id = 1,
                name = "profile 1",
                rename = true,
                eventType = EventType.Training,
                type = ProfileType.Cycling,
                course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Cycling),
                water = 550),
            Profile(
                id = 2,
                name = "profile 2",
                rename = true,
                eventType = EventType.Training,
                type = ProfileType.Running,
                course = Course(id = 2, name = "course 2", distance = 15007.00, type = ActivityType.Running)),
        )

        every { auth.getUser() } returns flowOf(user)
        coEvery { repo.getAllProfiles(any()) } returns flowOf(profiles)

        val res = useCase()
        res.test {
            assertThat(awaitItem()).isEqualTo(profiles)
            cancelAndIgnoreRemainingEvents()
        }

        verify { auth.getUser() }
        coVerify { repo.getAllProfiles(user) }
        confirmVerified(auth, repo)
    }

    @Test
    fun `Get profiles - empty list`() = runTest {
        every { auth.getUser() } returns flowOf(user)
        coEvery { repo.getAllProfiles(any()) } returns flowOf(emptyList<Profile>())

        val res = useCase()
        res.test {
            assertThat(awaitItem()).isEqualTo(emptyList<Profile>())
            cancelAndIgnoreRemainingEvents()
        }

        verify { auth.getUser() }
        coVerify { repo.getAllProfiles(user) }
        confirmVerified(repo)
    }

    @Test
    fun `Get profiles - no user`() = runTest {
        every { auth.getUser() } returns flowOf(null)

        val res = useCase()
        res.test {
            assertThat(awaitItem()).isEqualTo(emptyList<Profile>())
            cancelAndIgnoreRemainingEvents()
        }

        verify { auth.getUser() }
        confirmVerified(repo)
    }
}