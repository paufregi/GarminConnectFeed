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

class SaveProfileTest{
    private val auth = mockk<AuthRepository>()
    private val repo = mockk<AppRepository>()
    private lateinit var useCase: SaveProfile

    @Before
    fun setup(){
        useCase = SaveProfile(auth, repo)
    }

    @After
    fun tearDown(){
        confirmVerified(auth, repo)
        clearAllMocks()
    }

    @Test
    fun `Save profile`() = runTest {
        val profile = Profile(
            id = 1,
            name = "Commute to home",
            rename = true,
            eventType = EventType.Training,
            type = ActivityType.Cycling,
            course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Cycling),
            water = 550
        )
        every { auth.getUser() } returns flowOf(user)
        coEvery { repo.saveProfile(any(), any()) } returns Unit

        val res = useCase(profile)

        assertThat(res.isSuccess).isTrue()

        verify { auth.getUser() }
        coVerify { repo.saveProfile(user, profile) }
    }

    @Test
    fun `Invalid - No user`() = runTest {
        val profile = Profile(
            id = 1,
            name = "",
            rename = true,
            eventType = EventType.Training,
            type = ActivityType.Cycling,
            course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Cycling),
            water = 550
        )
        every { auth.getUser() } returns flowOf(null)

        val res = useCase(profile)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("User must be logged in")

        verify { auth.getUser() }
    }

    @Test
    fun `Invalid - No name`() = runTest {
        val profile = Profile(
            id = 1,
            name = "",
            rename = true,
            eventType = EventType.Training,
            type = ActivityType.Cycling,
            course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Cycling),
            water = 550
        )
        every { auth.getUser() } returns flowOf(user)

        val res = useCase(profile)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Name cannot be empty")

        verify { auth.getUser() }
    }

    @Test
    fun `Invalid - Course not allowed`() = runTest {
        val profile = Profile(
            id = 1,
            name = "Test",
            rename = true,
            eventType = EventType.Training,
            type = ActivityType.StrengthTraining,
            course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Cycling),
            water = 550
        )
        every { auth.getUser() } returns flowOf(user)

        val res = useCase(profile)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Can't have course for Strength Training activity type")

        verify { auth.getUser() }
    }

    @Test
    fun `Invalid - Course not compatible with activity type`() = runTest {
        val profile = Profile(
            id = 1,
            name = "Test",
            rename = true,
            eventType = EventType.Training,
            type = ActivityType.Running,
            course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Cycling),
            water = 550
        )
        every { auth.getUser() } returns flowOf(user)

        val res = useCase(profile)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Course not compatible with profile")

        verify { auth.getUser() }
    }
}