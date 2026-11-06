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
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.api.garmin.GarminConnect
import paufregi.connectfeed.data.api.garmin.models.ActivityDto
import paufregi.connectfeed.data.api.garmin.models.ActivityTypeDto
import paufregi.connectfeed.data.api.garmin.models.CourseDto
import paufregi.connectfeed.data.api.garmin.models.EventTypeDto
import paufregi.connectfeed.data.api.garmin.models.GearDto
import paufregi.connectfeed.data.api.garmin.models.MetadataDto
import paufregi.connectfeed.data.api.garmin.models.SummaryDto
import paufregi.connectfeed.data.api.garmin.models.UpdateActivityDto
import paufregi.connectfeed.data.api.garmin.models.UserProfileDto
import paufregi.connectfeed.data.api.garmin.models.WorkoutDto
import paufregi.connectfeed.data.repository.converters.toCore
import retrofit2.Response
import java.io.File
import paufregi.connectfeed.core.models.Activity as CoreActivity
import paufregi.connectfeed.core.models.ActivityType as CoreActivityType
import paufregi.connectfeed.core.models.Course as CoreCourse
import paufregi.connectfeed.core.models.EventType as CoreEventType
import paufregi.connectfeed.core.models.Gear as CoreGear

class GarminRepositoryTest {

    private lateinit var repo: GarminRepository
    private val connect = mockk<GarminConnect>()

    @Before
    fun setup(){
        repo = GarminRepository(connect)
    }

    @After
    fun tearDown(){
        confirmVerified(connect)
        clearAllMocks()
    }

    @Test
    fun `Get user`() = runTest {
        val userProfile = UserProfileDto(1, "user", "url")
        val user = User(1, "user", "url")
        coEvery { connect.getUserProfile() } returns Response.success(userProfile)

        val res = repo.getUser()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(user)

        coVerify { connect.getUserProfile() }
    }

    @Test
    fun `Get user - failure`() = runTest {
        coEvery { connect.getUserProfile() } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.getUser()

        assertThat(res.isSuccess).isFalse()

        coVerify { connect.getUserProfile() }
    }

    @Test
    fun `Get activities`() = runTest {
        val activities = listOf(
            ActivityDto(
                id = 1,
                name = "activity_1",
                distance = 10234.00,
                trainingEffectLabel = "recovery",
                type = ActivityTypeDto(id = 1, key = "running"),
                eventType = EventTypeDto(id = 4, key = "training"),
                beginTimestamp = 1729754100000,
                workoutId = 1
            ),
            ActivityDto(
                id = 2,
                name = "activity_2",
                distance = 17759.00,
                trainingEffectLabel = "recovery",
                type = ActivityTypeDto(id = 10, key = "road_biking"),
                eventType = EventTypeDto(id = 4, key = "training"),
                beginTimestamp = 1729705968000,
                workoutId = 2
            )
        )
        coEvery { connect.getActivities(any()) } returns Response.success(activities)

        val expected = activities.map { it.toCore() }

        val res = repo.getActivities(limit = 5)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)
        coVerify { connect.getActivities(5) }
    }

    @Test
    fun `Get activities - cached`() = runTest {
        val activities = listOf(
            ActivityDto(
                id = 1,
                name = "activity_1",
                distance = 10234.00,
                trainingEffectLabel = "recovery",
                type = ActivityTypeDto(id = 1, key = "running"),
                eventType = EventTypeDto(id = 4, key = "training"),
                beginTimestamp = 1729754100000,
                workoutId = 1
            ),
            ActivityDto(
                id = 2,
                name = "activity_2",
                distance = 17759.00,
                trainingEffectLabel = "recovery",
                type = ActivityTypeDto(id = 10, key = "road_biking"),
                eventType = EventTypeDto(id = 4, key = "training"),
                beginTimestamp = 1729705968000,
                workoutId = 2
            )
        )
        coEvery { connect.getActivities(any()) } returns Response.success(activities)

        val expected = activities.map { it.toCore() }

        val res = repo.getActivities(limit = 5)
        val res2 = repo.getActivities(limit = 5)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)
        assertThat(res2.isSuccess).isTrue()
        assertThat(res2.getOrNull()).isEqualTo(expected)
        coVerify(exactly = 1) { connect.getActivities(5) }
    }

    @Test
    fun `Get activities - force refresh`() = runTest {
        val activities = listOf(
            ActivityDto(
                id = 1,
                name = "activity_1",
                distance = 10234.00,
                trainingEffectLabel = "recovery",
                type = ActivityTypeDto(id = 1, key = "running"),
                eventType = EventTypeDto(id = 4, key = "training"),
                beginTimestamp = 1729754100000,
                workoutId = 1
            ),
            ActivityDto(
                id = 2,
                name = "activity_2",
                distance = 17759.00,
                trainingEffectLabel = "recovery",
                type = ActivityTypeDto(id = 10, key = "road_biking"),
                eventType = EventTypeDto(id = 4, key = "training"),
                beginTimestamp = 1729705968000,
                workoutId = 2
            )
        )
        coEvery { connect.getActivities(any()) } returns Response.success(activities)

        val expected = activities.map { it.toCore() }

        val res = repo.getActivities(limit = 5)
        val res2 = repo.getActivities(limit = 5, force = true)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)
        assertThat(res2.isSuccess).isTrue()
        assertThat(res2.getOrNull()).isEqualTo(expected)
        coVerify(exactly = 2) { connect.getActivities(5) }
    }

    @Test
    fun `Get activities - empty list`() = runTest {
        coEvery { connect.getActivities(any()) } returns Response.success(emptyList<ActivityDto>())

        val res = repo.getActivities(limit = 5)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(emptyList<CoreActivity>())
        coVerify { connect.getActivities(5) }
    }

    @Test
    fun `Get activities - null`() = runTest {
        coEvery { connect.getActivities(any()) } returns Response.success(null)

        val res = repo.getActivities(limit = 5)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(emptyList<CoreActivity>())
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
    fun `Get activities - failure cache`() = runTest {
        coEvery { connect.getActivities(any()) } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.getActivities(limit = 5)
        val res2 = repo.getActivities(limit = 5)

        assertThat(res.isSuccess).isFalse()
        assertThat(res2.isSuccess).isFalse()
        coVerify(exactly = 2) { connect.getActivities(5) }
    }

    @Test
    fun `Get courses`() = runTest {
        val cours = listOf(
            CourseDto(id = 1, name = "course 1", distance = 10234.00, type = ActivityTypeDto(id = 1, key = "running")),
            CourseDto(id = 2, name = "course 2", distance = 15007.00, type = ActivityTypeDto(id = 10, key = "road_biking"))
        )
        coEvery { connect.getCourses() } returns Response.success(cours)

        val expected = listOf(
            CoreCourse(id = 1, name = "course 1", distance = 10234.00, type = CoreActivityType.Running),
            CoreCourse(id = 2, name = "course 2", distance = 15007.00, type = CoreActivityType.RoadBiking),
        )

        val res = repo.getCourses()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)
        coVerify { connect.getCourses() }
    }

    @Test
    fun `Get courses - cache`() = runTest {
        val courses = listOf(
            CourseDto(id = 1, name = "course 1", distance = 10234.00, type = ActivityTypeDto(id = 1, key = "running")),
            CourseDto(id = 2, name = "course 2", distance = 15007.00, type = ActivityTypeDto(id = 10, key = "road_biking"))
        )
        coEvery { connect.getCourses() } returns Response.success(courses)

        val expected = courses.map { it.toCore() }

        val res = repo.getCourses()
        val res2 = repo.getCourses()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)
        assertThat(res2.isSuccess).isTrue()
        assertThat(res2.getOrNull()).isEqualTo(expected)
        coVerify(exactly = 1) { connect.getCourses() }
    }

    @Test
    fun `Get courses - force refresh`() = runTest {
        val courses = listOf(
            CourseDto(id = 1, name = "course 1", distance = 10234.00, type = ActivityTypeDto(id = 1, key = "running")),
            CourseDto(id = 2, name = "course 2", distance = 15007.00, type = ActivityTypeDto(id = 10, key = "road_biking"))
        )
        coEvery { connect.getCourses() } returns Response.success(courses)

        val expected = courses.map { it.toCore() }

        val res = repo.getCourses()
        val res2 = repo.getCourses(true)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)
        assertThat(res2.isSuccess).isTrue()
        assertThat(res2.getOrNull()).isEqualTo(expected)
        coVerify(exactly = 2) { connect.getCourses() }
    }

    @Test
    fun `Get courses - empty list`() = runTest {
        coEvery { connect.getCourses() } returns Response.success(emptyList())

        val res = repo.getCourses()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(emptyList<CoreCourse>())
        coVerify { connect.getCourses() }
    }

    @Test
    fun `Get courses - null`() = runTest {
        coEvery { connect.getCourses() } returns Response.success(null)

        val res = repo.getCourses()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(emptyList<CoreCourse>())
        coVerify { connect.getCourses() }
    }

    @Test
    fun `Get courses - failure`() = runTest {
        coEvery { connect.getCourses() } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.getCourses()

        assertThat(res.isSuccess).isFalse()
        coVerify { connect.getCourses() }
    }

    @Test
    fun `Get workout`() = runTest {
        val workout = WorkoutDto(1, "workout")
        coEvery { connect.getWorkout(any()) } returns Response.success(workout)

        val expected = workout.toCore()

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
    fun `Get gears`() = runTest {
        val gears = listOf(
            GearDto(id = "1", brand = "Mizuno", model = "Neo Vista", name = null, type = "SHOE"),
            GearDto(id = "2", brand = "Giant", model = "Contend AR", name = "Nova", type = "BIKE")
        )
        coEvery { connect.getGears() } returns Response.success(gears)

        val expected = gears.map { it.toCore() }

        val res = repo.getGears()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)
        coVerify { connect.getGears() }
    }

    @Test
    fun `Get gears - empty list`() = runTest {
        coEvery { connect.getGears() } returns Response.success(emptyList())

        val res = repo.getGears()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(emptyList<CoreGear>())
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
    fun `Update activity`() = runTest {
        coEvery { connect.updateActivity(any(), any()) } returns Response.success(Unit)
        coEvery { connect.associateGears(any(), any()) } returns Response.success(Unit)


        val activity = CoreActivity(
            id = 1,
            name = "activity",
            distance = 17803.00,
            type = CoreActivityType.Cycling
        )
        val name = "newName"
        val description = "newDescription"
        val eventType = CoreEventType.Training
        val course = CoreCourse(1, "course", 10234.00, CoreActivityType.Cycling)
        val water = 2
        val effort = 50f
        val feel = 80f
        val gear = CoreGear(id = "gear-1", name = "gear 1", type = GearType.Bike, distance = 1000)

        val expectedUpdateRequest = UpdateActivityDto(
            id = 1,
            name = name,
            description = description,
            eventType = EventTypeDto(id = eventType.id, key = eventType.key),
            metadata = MetadataDto(courseId = course.id),
            summary = SummaryDto(water = water, feel = feel, effort = effort),
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

        val activity = CoreActivity(
            id = 1,
            name = "activity",
            distance = 17803.00,
            type = CoreActivityType.Cycling
        )

        val expectedRequest = UpdateActivityDto(
            id = 1,
            name = "newName",
            description = "newDescription",
            eventType = EventTypeDto(id = CoreEventType.Training.id, key = CoreEventType.Training.key),
            metadata = MetadataDto(courseId = 1),
            summary = SummaryDto(water = 2, feel = 80f, effort = 50f)
        )

        val res = repo.updateActivity(
            activity = activity,
            name = "newName",
            description = "newDescription",
            eventType = CoreEventType.Training,
            course = CoreCourse(1, "course", 10234.00, CoreActivityType.Cycling),
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
        val activity = CoreActivity(
            id = 1,
            name = "activity",
            distance = 17803.00,
            type = CoreActivityType.Cycling
        )
        val name = "newName"
        val description = "newDescription"
        val eventType = CoreEventType.Training
        val course = CoreCourse(1, "course", 10234.00, CoreActivityType.Cycling)
        val water = 2

        val expectedRequest = UpdateActivityDto(
            id = 1,
            name = name,
            description = description,
            eventType = EventTypeDto(id = eventType.id, key = eventType.key),
            metadata = MetadataDto(courseId = 1),
            summary = SummaryDto(water = 2, feel = null, effort = null)
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

        val activity = CoreActivity(
            id = 1,
            name = "activity",
            distance = 17803.00,
            type = CoreActivityType.Cycling
        )
        val gear = CoreGear(id = "gear-1", name = "gear 1", type = GearType.Bike, distance = 1000)

        val res = repo.updateActivity(
            activity = activity,
            name = "newName",
            description = "newDescription",
            eventType = CoreEventType.Training,
            course = CoreCourse(1, "course", 10234.00, CoreActivityType.Cycling),
            water = 2,
            effort = 50f,
            feel = 80f,
            gear = gear,
        )

        assertThat(res.isSuccess).isFalse()
        coVerify { connect.updateActivity(any(), any()) }
        coVerify { connect.associateGears(activity.id, listOf("gear-1")) }
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