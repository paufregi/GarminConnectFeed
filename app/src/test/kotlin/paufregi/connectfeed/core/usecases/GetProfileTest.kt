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
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.data.repository.AppRepository
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.user

class GetProfileTest{
    private val authRepo = mockk<AuthRepository>()
    private val repo = mockk<AppRepository>()
    private lateinit var useCase: GetProfile

    private val profile = Profile(
        id = 1,
        name = "Commute to home",
        rename = true,
        eventType = EventType.Training,
        type = ActivityType.Cycling,
        course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Cycling),
        water = 550
    )

    @Before
    fun setup(){
        useCase = GetProfile(authRepo, repo)
    }

    @After
    fun tearDown(){
        confirmVerified(authRepo, repo)
        clearAllMocks()
    }

    @Test
    fun `Get profile`() = runTest {
        every { authRepo.getUser() } returns flowOf(user)
        coEvery { repo.getProfile(user, 1) } returns profile

        val res = useCase(1)

        assertThat(res).isEqualTo(profile)
        verify { authRepo.getUser() }
        coVerify { repo.getProfile(user, 1) }
    }

    @Test
    fun `No profile`() = runTest {
        every { authRepo.getUser() } returns flowOf(user)
        coEvery { repo.getProfile(user, 1) } returns null

        val res = useCase(1)

        assertThat(res).isNull()
        verify { authRepo.getUser() }
        coVerify { repo.getProfile(user, 1) }
    }

    @Test
    fun `No id`() = runTest {
        val res = useCase(null)

        assertThat(res).isNull()
        coVerify(exactly = 0) { repo.getProfile(any(), any()) }
    }

    @Test
    fun `No user`() = runTest {
        every { authRepo.getUser() } returns flowOf(null)

        val res = useCase(1)

        assertThat(res).isNull()
        verify { authRepo.getUser() }
        coVerify(exactly = 0) { repo.getProfile(any(), any()) }
    }
}

