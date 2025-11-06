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
import paufregi.connectfeed.core.models.ProfileType
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.user

class SaveProfileTest{
    private val auth = mockk<AuthRepository>()
    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: SaveProfile

    @Before
    fun setup(){
        useCase = SaveProfile(auth, repo)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Save profile use-case`() = runTest {
        val profile = Profile(
            id = 1,
            name = "Commute to home",
            rename = true,
            eventType = EventType.Training,
            type = ProfileType.Cycling,
            course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Cycling),
            water = 550
        )
        every { auth.getUser() } returns flowOf(user)
        coEvery { repo.saveProfile(any(), any()) } returns Unit

        val res = useCase(profile)

        assertThat(res.isSuccess).isTrue()

        verify { auth.getUser() }
        coVerify { repo.saveProfile(user, profile) }
        confirmVerified(auth, repo)
    }

    @Test
    fun `Invalid - No user`() = runTest {
        val profile = Profile(
            id = 1,
            name = "",
            rename = true,
            eventType = EventType.Training,
            type = ProfileType.Cycling,
            course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Cycling),
            water = 550
        )
        every { auth.getUser() } returns flowOf(null)

        val res = useCase(profile)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("User must be logged in")

        verify { auth.getUser() }
        confirmVerified(auth, repo)
    }

    @Test
    fun `Invalid - No name`() = runTest {
        val profile = Profile(
            id = 1,
            name = "",
            rename = true,
            eventType = EventType.Training,
            type = ProfileType.Cycling,
            course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Cycling),
            water = 550
        )
        every { auth.getUser() } returns flowOf(user)

        val res = useCase(profile)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Name cannot be empty")

        verify { auth.getUser() }
        confirmVerified(auth, repo)
    }

    @Test
    fun `Invalid - Strength profile with course`() = runTest {
        val profile = Profile(
            id = 1,
            name = "Test",
            rename = true,
            eventType = EventType.Training,
            type = ProfileType.Strength,
            course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Cycling),
            water = 550
        )
        every { auth.getUser() } returns flowOf(user)

        val res = useCase(profile)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Can't have course for Strength activity type")

        verify { auth.getUser() }
        confirmVerified(auth, repo)
    }

    @Test
    fun `Invalid - Any profile with course`() = runTest {
        val profile = Profile(
            id = 1,
            name = "Test",
            rename = true,
            eventType = EventType.Training,
            type = ProfileType.Any,
            course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Cycling),
            water = 550
        )
        every { auth.getUser() } returns flowOf(user)

        val res = useCase(profile)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Can't have course for Any activity type")

        verify { auth.getUser() }
        confirmVerified(auth, repo)
    }

    @Test
    fun `Invalid - Course not matching activity type`() = runTest {
        val profile = Profile(
            id = 1,
            name = "Test",
            rename = true,
            eventType = EventType.Training,
            type = ProfileType.Running,
            course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Cycling),
            water = 550
        )
        every { auth.getUser() } returns flowOf(user)

        val res = useCase(profile)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Course not compatible with profile")

        verify { auth.getUser() }
        confirmVerified(auth, repo)
    }
}