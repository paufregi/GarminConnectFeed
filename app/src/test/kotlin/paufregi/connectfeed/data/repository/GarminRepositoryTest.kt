package paufregi.connectfeed.data.repository

import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.models.Workout
import paufregi.connectfeed.data.api.garmin.GarminConnect
import paufregi.connectfeed.data.api.garmin.models.Metadata
import paufregi.connectfeed.data.api.garmin.models.Summary
import paufregi.connectfeed.data.api.garmin.models.UpdateActivity
import paufregi.connectfeed.data.api.garmin.models.UserProfile
import retrofit2.Response
import java.io.File
import kotlin.time.Instant
import paufregi.connectfeed.data.api.garmin.models.Activity as GarminActivity
import paufregi.connectfeed.data.api.garmin.models.ActivityType as GarminActivityType
import paufregi.connectfeed.data.api.garmin.models.Course as GarminCourse
import paufregi.connectfeed.data.api.garmin.models.Gear as GarminGear
import paufregi.connectfeed.data.api.garmin.models.Workout as GarminWorkout

class GarminRepositoryTest {

    private lateinit var repo: GarminRepository
    private val connect = mockk<GarminConnect>()

    @Before
    fun setup() {
        repo = GarminRepository(connect)
    }

    @After
    fun tearDown() {
        confirmVerified(connect)
        clearAllMocks()
    }

    val activity = Activity(
        id = 1,
        name = "activity",
        distance = 17803.00,
        type = ActivityType.Cycling,
        eventType = EventType.Training,
        date = Instant.parse("2022-05-02T12:15:09Z"),
        stravaId = 10L
    )
    val gear = Gear(
        id = "gear-1",
        name = "gear 1",
        type = GearType.Bike,
        distance = 1000,
        stravaId = "strava-gear-id"
    )

    @Test
    fun `Get user profile`() = runTest {
        val userProfile = UserProfile(1, "user", "url")
        val user = User(1, "user", "url")
        coEvery { connect.getUserProfile() } returns Response.success(userProfile)

        val res = repo.getUserProfile()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(user)

        coVerify { connect.getUserProfile() }
    }

    @Test
    fun `Get user - failure`() = runTest {
        coEvery { connect.getUserProfile() } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.getUserProfile()

        assertThat(res.isSuccess).isFalse()

        coVerify { connect.getUserProfile() }
    }

    @Test
    fun `Get activities`() = runTest {
        val activities = listOf(
            GarminActivity(
                id = 1,
                name = "activity_1",
                distance = 10234.00,
                trainingEffectLabel = "recovery",
                type = GarminActivityType(id = 1, key = "running", type = ActivityType.Running),
                eventType = EventType.Training,
                beginTimestamp = Instant.parse("2022-05-02T12:15:09Z"),
                workoutId = 1
            ),
            GarminActivity(
                id = 2,
                name = "activity_2",
                distance = 17759.00,
                trainingEffectLabel = "recovery",
                type = GarminActivityType(id = 10, key = "road_biking", type = ActivityType.RoadBiking),
                eventType = EventType.Training,
                beginTimestamp = Instant.parse("2022-05-02T12:15:09Z"),
                workoutId = 2
            )
        )
        coEvery { connect.getActivities(any()) } returns Response.success(activities)

        val res = repo.getActivities(limit = 5)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(activities)
        coVerify { connect.getActivities(5) }
    }

    @Test
    fun `Get activities - empty list`() = runTest {
        coEvery { connect.getActivities(any()) } returns Response.success(emptyList<GarminActivity>())

        val res = repo.getActivities(limit = 5)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(emptyList<GarminActivity>())
        coVerify { connect.getActivities(5) }
    }

    @Test
    fun `Get activities - null`() = runTest {
        coEvery { connect.getActivities(any()) } returns Response.success(null)

        val res = repo.getActivities(limit = 5)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(emptyList<GarminActivity>())
        coVerify { connect.getActivities(5) }
    }

    @Test
    fun `Get activities - failure`() = runTest {
        coEvery { connect.getActivities(any()) } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.getActivities(limit = 5)

        assertThat(res.isSuccess).isFalse()
        coVerify { connect.getActivities(5) }
    }

    @Test
    fun `Get gears`() = runTest {
        val gears = listOf(
            GarminGear(id = "1", brand = "Mizuno", model = "Neo Vista", name = null, type = GearType.Shoe),
            GarminGear(id = "2", brand = "Giant", model = "Contend AR", name = "Nova", type = GearType.Bike)
        )
        coEvery { connect.getGears() } returns Response.success(gears)

        val res = repo.getGears()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(gears)
        coVerify { connect.getGears() }
    }

    @Test
    fun `Get gears - empty list`() = runTest {
        coEvery { connect.getGears() } returns Response.success(emptyList())

        val res = repo.getGears()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(emptyList<Gear>())
        coVerify { connect.getGears() }
    }

    @Test
    fun `Get gears - null`() = runTest {
        coEvery { connect.getGears() } returns Response.success(null)

        val res = repo.getGears()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(emptyList<Gear>())
        coVerify { connect.getGears() }
    }

    @Test
    fun `Get gears - failure`() = runTest {
        coEvery { connect.getGears() } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.getGears()

        assertThat(res.isSuccess).isFalse()
        coVerify { connect.getGears() }
    }

    @Test
    fun `Get courses`() = runTest {
        val courses = listOf(
            GarminCourse(id = 1, name = "course 1", distance = 10234.00, type = GarminActivityType(id = 1, key = "running", type = ActivityType.Running)),
            GarminCourse(id = 2, name = "course 2", distance = 15007.00, type = GarminActivityType(id = 10, key = "road_biking", type = ActivityType.RoadBiking))
        )
        coEvery { connect.getCourses() } returns Response.success(courses)

        val expected = listOf(
            Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Running),
            Course(id = 2, name = "course 2", distance = 15007.00, type = ActivityType.RoadBiking),
        )

        val res = repo.getCourses()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)
        coVerify { connect.getCourses() }
    }

    @Test
    fun `Get courses - empty list`() = runTest {
        coEvery { connect.getCourses() } returns Response.success(emptyList())

        val res = repo.getCourses()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(emptyList<Course>())
        coVerify { connect.getCourses() }
    }

    @Test
    fun `Get courses - null`() = runTest {
        coEvery { connect.getCourses() } returns Response.success(null)

        val res = repo.getCourses()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(emptyList<Course>())
        coVerify { connect.getCourses() }
    }

    @Test
    fun `Get workout`() = runTest {
        val workout = GarminWorkout(1, "workout")
        coEvery { connect.getWorkout(any()) } returns Response.success(workout)

        val expected = Workout(1, "workout")

        val res = repo.getWorkout(1)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)
        coVerify { connect.getWorkout(1) }
    }

    @Test
    fun `Get workout - failure`() = runTest {
        coEvery { connect.getWorkout(any()) } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.getWorkout(1)

        assertThat(res.isSuccess).isFalse()
        coVerify { connect.getWorkout(1) }
    }

    @Test
    fun `Update activity`() = runTest {
        coEvery { connect.updateActivity(any(), any()) } returns Response.success(Unit)
        coEvery { connect.associateGears(any(), any()) } returns Response.success(Unit)


        val name = "newName"
        val description = "newDescription"
        val eventType = EventType.Training
        val course = Course(1, "course", 10234.00, ActivityType.Cycling)
        val water = 2
        val effort = 50f
        val feel = 80f

        val expectedUpdateRequest = UpdateActivity(
            id = 1,
            name = name,
            description = description,
            eventType = eventType,
            metadata = Metadata(courseId = course.id),
            summary = Summary(water = water, feel = feel, effort = effort),
        )

        val res = repo.updateActivity(
            activity = activity,
            name = name,
            description = description,
            eventType = eventType,
            course = course,
            water = water,
            effort = effort,
            feel = feel,
            gear = gear,
        )

        assertThat(res.isSuccess).isTrue()
        coVerify { connect.updateActivity(activity.id, expectedUpdateRequest) }
        coVerify { connect.associateGears(activity.id, listOf(gear.id)) }
    }

    @Test
    fun `Update activity - no gear`() = runTest {
        coEvery { connect.updateActivity(any(), any()) } returns Response.success(Unit)

        val expectedRequest = UpdateActivity(
            id = 1,
            name = "newName",
            description = "newDescription",
            eventType = EventType.Training,
            metadata = Metadata(courseId = 1),
            summary = Summary(water = 2, feel = 80f, effort = 50f)
        )

        val res = repo.updateActivity(
            activity = activity,
            name = "newName",
            description = "newDescription",
            eventType = EventType.Training,
            course = Course(1, "course", 10234.00, ActivityType.Cycling),
            water = 2,
            effort = 50f,
            feel = 80f,
            gear = null,
        )

        assertThat(res.isSuccess).isTrue()
        coVerify { connect.updateActivity(activity.id, expectedRequest) }
    }

    @Test
    fun `Update activity - failure`() = runTest {
        coEvery { connect.updateActivity(any(), any()) } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val name = "newName"
        val description = "newDescription"
        val eventType = EventType.Training
        val course = Course(1, "course", 10234.00, ActivityType.Cycling)
        val water = 2

        val expectedRequest = UpdateActivity(
            id = 1,
            name = name,
            description = description,
            eventType = eventType,
            metadata = Metadata(courseId = 1),
            summary = Summary(water = 2, feel = null, effort = null)
        )

        val res = repo.updateActivity(
            activity = activity,
            name = name,
            description = description,
            eventType = eventType,
            course = course,
            water = water,
            effort = null,
            feel = null,
            gear = null,
        )

        assertThat(res.isSuccess).isFalse()
        coVerify { connect.updateActivity(activity.id, expectedRequest) }
        coVerify(exactly = 0) { connect.associateGears(any(), any()) }
    }

    @Test
    fun `Update activity - associate gear failure`() = runTest {
        coEvery { connect.updateActivity(any(), any()) } returns Response.success(Unit)
        coEvery { connect.associateGears(any(), any()) } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val activity = Activity(
            id = 1,
            name = "activity",
            distance = 17803.00,
            type = ActivityType.Cycling,
            eventType = EventType.Training,
            date = Instant.parse("2022-05-02T12:15:09Z"),
            stravaId = null
        )

        val res = repo.updateActivity(
            activity = activity,
            name = "newName",
            description = "newDescription",
            eventType = EventType.Training,
            course = Course(1, "course", 10234.00, ActivityType.Cycling),
            water = 2,
            effort = 50f,
            feel = 80f,
            gear = gear,
        )

        assertThat(res.isSuccess).isFalse()
        coVerify { connect.updateActivity(any(), any()) }
        coVerify { connect.associateGears(activity.id, listOf(gear.id)) }
    }

    @Test
    fun `Upload file`() = runTest {
        val testFile = File.createTempFile("test", "test")

        coEvery { connect.uploadFile(any()) } returns Response.success(Unit)

        val res = repo.uploadFile(testFile)

        assertThat(res.isSuccess).isTrue()
        coVerify { connect.uploadFile(any()) }
    }

    @Test
    fun `Upload file - failure`() = runTest {
        val testFile = File.createTempFile("test", "test")

        coEvery { connect.uploadFile(any()) } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.uploadFile(testFile)

        assertThat(res.isSuccess).isFalse()
        coVerify { connect.uploadFile(any()) }
    }
}

