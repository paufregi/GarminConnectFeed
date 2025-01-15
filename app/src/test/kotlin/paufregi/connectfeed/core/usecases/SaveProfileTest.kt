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
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.data.repository.GarminRepository

class SaveProfileTest{
    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: SaveProfile

    @Before
    fun setup(){
        useCase = SaveProfile(repo)
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
            eventType = EventType(id = 1, name = "event 1"),
            activityType = ActivityType.Cycling,
            course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Cycling),
            water = 550
        )
        coEvery { repo.saveProfile(any()) } returns Unit

        val res = useCase(profile)

        assertThat(res.isSuccessful).isTrue()

        coVerify { repo.saveProfile(profile) }
        confirmVerified(repo)
    }

    @Test
    fun `Invalid - No name`() = runTest {
        val profile = Profile(
            id = 1,
            name = "",
            rename = true,
            eventType = EventType(id = 1, name = "event 1"),
            activityType = ActivityType.Cycling,
            course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Cycling),
            water = 550
        )

        val res = useCase(profile)

        assertThat(res.isSuccessful).isFalse()
        res as Result.Failure
        assertThat(res.reason).isEqualTo("Name cannot be empty")
    }

    @Test
    fun `Invalid - Strength profile with course`() = runTest {
        val profile = Profile(
            id = 1,
            name = "Test",
            rename = true,
            eventType = EventType(id = 1, name = "event 1"),
            activityType = ActivityType.Strength,
            course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Cycling),
            water = 550
        )

        val res = useCase(profile)

        assertThat(res.isSuccessful).isFalse()
        res as Result.Failure
        assertThat(res.reason).isEqualTo("Can't have course for strength activity")
    }

    @Test
    fun `Invalid - Any profile with course`() = runTest {
        val profile = Profile(
            id = 1,
            name = "Test",
            rename = true,
            eventType = EventType(id = 1, name = "event 1"),
            activityType = ActivityType.Any,
            course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Cycling),
            water = 550
        )

        val res = useCase(profile)

        assertThat(res.isSuccessful).isFalse()
        res as Result.Failure
        assertThat(res.reason).isEqualTo("Can't have course for strength activity")
    }

    @Test
    fun `Invalid - Course not matching activity type`() = runTest {
        val profile = Profile(
            id = 1,
            name = "Test",
            rename = true,
            eventType = EventType(id = 1, name = "event 1"),
            activityType = ActivityType.Running,
            course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Cycling),
            water = 550
        )

        val res = useCase(profile)

        assertThat(res.isSuccessful).isFalse()
        res as Result.Failure
        assertThat(res.reason).isEqualTo("Course must match activity type")
    }
}