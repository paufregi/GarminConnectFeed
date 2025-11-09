package paufregi.connectfeed.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.api.garmin.GarminConnect
import paufregi.connectfeed.data.api.garmin.models.Activity
import paufregi.connectfeed.data.api.garmin.models.ActivityType
import paufregi.connectfeed.data.api.garmin.models.Course
import paufregi.connectfeed.data.api.garmin.models.EventType
import paufregi.connectfeed.data.api.garmin.models.Metadata
import paufregi.connectfeed.data.api.garmin.models.Summary
import paufregi.connectfeed.data.api.garmin.models.UpdateActivity
import paufregi.connectfeed.data.api.garmin.models.UserProfile
import paufregi.connectfeed.data.api.strava.Strava
import paufregi.connectfeed.data.database.GarminDao
import paufregi.connectfeed.data.database.entities.ProfileEntity
import paufregi.connectfeed.user
import retrofit2.Response
import java.io.File
import paufregi.connectfeed.core.models.Activity as CoreActivity
import paufregi.connectfeed.core.models.ActivityType as CoreActivityType
import paufregi.connectfeed.core.models.Course as CoreCourse
import paufregi.connectfeed.core.models.EventType as CoreEventType
import paufregi.connectfeed.data.api.strava.models.Activity as StravaActivity
import paufregi.connectfeed.data.api.strava.models.UpdateActivity as StravaUpdateActivity
import paufregi.connectfeed.data.api.strava.models.UpdateProfile as StravaUpdateProfile

class GarminRepositoryTest {

    private lateinit var repo: GarminRepository
    private val dao = mockk<GarminDao>()
    private val connect = mockk<GarminConnect>()
    private val strava = mockk<Strava>()

    @Before
    fun setup(){
        repo = GarminRepository(dao, connect, strava)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Fetch user`() = runTest {
        val userProfile = UserProfile(1, "user", "url")
        val user = User(1, "user", "url")
        coEvery { connect.getUserProfile() } returns Response.success(userProfile)

        val res = repo.fetchUser()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(user)

        coVerify { connect.getUserProfile() }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Fetch user - failure`() = runTest {
        coEvery { connect.getUserProfile() } returns Response.error<UserProfile>(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.fetchUser()

        assertThat(res.isSuccess).isFalse()

        coVerify { connect.getUserProfile() }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get all profile - no result`() = runTest {
        coEvery { dao.getProfile(any()) } returns null

        val res = repo.getProfile(1)

        assertThat(res).isNull()

        coVerify { dao.getProfile(1) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get all profiles`() = runTest {
        val profiles = listOf(
            Profile(
                id = 1,
                name = "profile 1",
                eventType = CoreEventType.Training,
                type = CoreActivityType.Cycling,
                course = CoreCourse(1, "course 1", 10234.00, CoreActivityType.Cycling),
                water = 2
            ),
            Profile(
                id = 2,
                name = "profile 2",
                eventType = CoreEventType.Recreation,
                type = CoreActivityType.Running,
                course = CoreCourse(2, "course 2", 15007.00, CoreActivityType.Running),
            )
        )
        val profileEntities = listOf(
            ProfileEntity(
                id = 1,
                userId = user.id,
                name = "profile 1",
                eventType = CoreEventType.Training,
                type = CoreActivityType.Cycling,
                course = CoreCourse(1, "course 1", 10234.00, CoreActivityType.Cycling),
                water = 2
            ),
            ProfileEntity(
                id = 2,
                userId = user.id,
                name = "profile 2",
                eventType = CoreEventType.Recreation,
                type = CoreActivityType.Running,
                course = CoreCourse(2, "course 2", 15007.00, CoreActivityType.Running),
            )
        )

        coEvery { dao.getAllProfiles(any()) } returns flowOf(profileEntities)

        val res = repo.getAllProfiles(user)

        res.test {
            assertThat(awaitItem()).isEqualTo(profiles)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { dao.getAllProfiles(user.id) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get all profiles - empty list`() = runTest {
        coEvery { dao.getAllProfiles(any()) } returns flowOf(emptyList<ProfileEntity>())

        val res = repo.getAllProfiles(user)

        res.test {
            assertThat(awaitItem()).isEqualTo(emptyList<Profile>())
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { dao.getAllProfiles(user.id) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get profile`() = runTest {
        val profile = Profile(
            id = 1,
            name = "profile",
            eventType = CoreEventType.Training,
            type = CoreActivityType.Cycling,
            course = CoreCourse(1, "course", 10234.00, CoreActivityType.Cycling),
            water = 2
        )
        val profileEntity = ProfileEntity(
            id = 1,
            userId = user.id,
            name = "profile",
            eventType = CoreEventType.Training,
            type = CoreActivityType.Cycling,
            course = CoreCourse(1, "course", 10234.00, CoreActivityType.Cycling),
            water = 2
        )

        coEvery { dao.getProfile(any()) } returns profileEntity

        val res = repo.getProfile(1)

        assertThat(res).isEqualTo(profile)

        coVerify { dao.getProfile(1) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Save profile`() = runTest {
        val profile = Profile(
            name = "profile",
            eventType = CoreEventType.Training,
            type = CoreActivityType.Cycling,
            course = CoreCourse(1, "course", 10234.00, CoreActivityType.Cycling),
            water = 2
        )

        val profileEntity  = ProfileEntity(
            name = "profile",
            userId = user.id,
            eventType = CoreEventType.Training,
            type = CoreActivityType.Cycling,
            course = CoreCourse(1, "course", 10234.00, CoreActivityType.Cycling),
            water = 2
        )

        coEvery { dao.saveProfile(any()) } returns Unit

        repo.saveProfile(user, profile)

        coVerify { dao.saveProfile(profileEntity) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Delete profile`() = runTest {
        val profile = Profile(
            id = 1,
            name = "profile",
            eventType = CoreEventType.Training,
            type = CoreActivityType.Cycling,
            course = CoreCourse(1, "course", 10234.00, CoreActivityType.Cycling),
        )

        val profileEntity  = ProfileEntity(
            id = 1,
            userId = user.id,
            name = "profile",
            eventType = CoreEventType.Training,
            type = CoreActivityType.Cycling,
            course = CoreCourse(1, "course", 10234.00, CoreActivityType.Cycling),
        )

        coEvery { dao.deleteProfile(any()) } returns Unit

        repo.deleteProfile(user, profile)

        coVerify { dao.deleteProfile(profileEntity) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get activities`() = runTest {
        val activities = listOf(
            Activity(
                id = 1,
                name = "activity_1",
                distance = 10234.00,
                trainingEffectLabel = "recovery",
                type = ActivityType(id = 1, key = "running"),
                eventType = EventType(id = 4, key = "training"),
                beginTimestamp = 1729754100000
            ),
            Activity(
                id = 2,
                name = "activity_2",
                distance = 17759.00,
                trainingEffectLabel = "recovery",
                type = ActivityType(id = 10, key = "road_biking"),
                eventType = EventType(id = 4, key = "training"),
                beginTimestamp = 1729705968000
            )
        )
        coEvery { connect.getActivities(any()) } returns Response.success(activities)

        val expected = activities.map { it.toCore() }

        val res = repo.getActivities(limit = 5)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)
        coVerify { connect.getActivities(5) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get activities - cached`() = runTest {
        val activities = listOf(
            Activity(
                id = 1,
                name = "activity_1",
                distance = 10234.00,
                trainingEffectLabel = "recovery",
                type = ActivityType(id = 1, key = "running"),
                eventType = EventType(id = 4, key = "training"),
                beginTimestamp = 1729754100000
            ),
            Activity(
                id = 2,
                name = "activity_2",
                distance = 17759.00,
                trainingEffectLabel = "recovery",
                type = ActivityType(id = 10, key = "road_biking"),
                eventType = EventType(id = 4, key = "training"),
                beginTimestamp = 1729705968000
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
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get activities - force refresh`() = runTest {
        val activities = listOf(
            Activity(
                id = 1,
                name = "activity_1",
                distance = 10234.00,
                trainingEffectLabel = "recovery",
                type = ActivityType(id = 1, key = "running"),
                eventType = EventType(id = 4, key = "training"),
                beginTimestamp = 1729754100000
            ),
            Activity(
                id = 2,
                name = "activity_2",
                distance = 17759.00,
                trainingEffectLabel = "recovery",
                type = ActivityType(id = 10, key = "road_biking"),
                eventType = EventType(id = 4, key = "training"),
                beginTimestamp = 1729705968000
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
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get activities - empty list`() = runTest {
        coEvery { connect.getActivities(any()) } returns Response.success(emptyList<Activity>())

        val res = repo.getActivities(limit = 5)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(emptyList<CoreActivity>())
        coVerify { connect.getActivities(5) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get activities - null`() = runTest {
        coEvery { connect.getActivities(any()) } returns Response.success(null)

        val res = repo.getActivities(limit = 5)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(emptyList<CoreActivity>())
        coVerify { connect.getActivities(5) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get activities - failure`() = runTest {
        coEvery { connect.getActivities(any()) } returns Response.error<List<Activity>>(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.getActivities(limit = 5)

        assertThat(res.isSuccess).isFalse()
        coVerify { connect.getActivities(5) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get activities - failure cache`() = runTest {
        coEvery { connect.getActivities(any()) } returns Response.error<List<Activity>>(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.getActivities(limit = 5)
        val res2 = repo.getActivities(limit = 5)

        assertThat(res.isSuccess).isFalse()
        assertThat(res2.isSuccess).isFalse()
        coVerify(exactly = 2) { connect.getActivities(5) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get Strava activities`() = runTest {
        val activities = listOf(
            StravaActivity(
                id = 1,
                name = "activity_1",
                distance = 10234.00,
                sportType = "Run",
                startDate = "2018-05-02T12:15:09Z"
            ),
            StravaActivity(
                id = 2,
                name = "activity_2",
                distance = 17759.00,
                sportType = "Ride",
                startDate = "2018-04-30T12:35:51Z"
            )
        )
        coEvery { strava.getActivities(perPage = any()) } returns Response.success(activities)

        val expected = activities.map { it.toCore() }

        val res = repo.getStravaActivities(limit = 5)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)
        coVerify { strava.getActivities(perPage = 5) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get Strava activities - cached`() = runTest {
        val activities = listOf(
            StravaActivity(
                id = 1,
                name = "activity_1",
                distance = 10234.00,
                sportType = "Run",
                startDate = "2018-05-02T12:15:09Z"
            ),
            StravaActivity(
                id = 2,
                name = "activity_2",
                distance = 17759.00,
                sportType = "Ride",
                startDate = "2018-04-30T12:35:51Z"
            )
        )
        coEvery { strava.getActivities(perPage = any()) } returns Response.success(activities)

        val expected = activities.map { it.toCore() }

        val res = repo.getStravaActivities(limit = 5)
        val res2 = repo.getStravaActivities(limit = 5)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)
        assertThat(res2.isSuccess).isTrue()
        assertThat(res2.getOrNull()).isEqualTo(expected)
        coVerify(exactly = 1) { strava.getActivities(perPage = 5) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get Strava activities - force refresh`() = runTest {
        val activities = listOf(
            StravaActivity(
                id = 1,
                name = "activity_1",
                distance = 10234.00,
                sportType = "Run",
                startDate = "2018-05-02T12:15:09Z"
            ),
            StravaActivity(
                id = 2,
                name = "activity_2",
                distance = 17759.00,
                sportType = "Ride",
                startDate = "2018-04-30T12:35:51Z"
            )
        )
        coEvery { strava.getActivities(perPage = any()) } returns Response.success(activities)

        val expected = activities.map { it.toCore() }

        val res = repo.getStravaActivities(limit = 5)
        val res2 = repo.getStravaActivities(limit = 5, force = true)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)
        assertThat(res2.isSuccess).isTrue()
        assertThat(res2.getOrNull()).isEqualTo(expected)
        coVerify(exactly = 2) { strava.getActivities(perPage = 5) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get Strava activities - empty list`() = runTest {
        coEvery { strava.getActivities(perPage = any()) } returns Response.success(emptyList<StravaActivity>())

        val res = repo.getStravaActivities(limit = 5)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(emptyList<CoreActivity>())
        coVerify { strava.getActivities(perPage = 5) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get Strava activities - null`() = runTest {
        coEvery { strava.getActivities(perPage = any()) } returns Response.success(null)

        val res = repo.getStravaActivities(limit = 5)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(emptyList<CoreActivity>())
        coVerify { strava.getActivities(perPage = 5) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get Strava activities - failure`() = runTest {
        coEvery { strava.getActivities(perPage = any()) } returns Response.error<List<StravaActivity>>(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.getStravaActivities(limit = 5)

        assertThat(res.isSuccess).isFalse()
        coVerify { strava.getActivities(perPage = 5) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get Strava activities - failure cache`() = runTest {
        coEvery { strava.getActivities(perPage = any()) } returns Response.error<List<StravaActivity>>(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.getStravaActivities(limit = 5)
        val res2 = repo.getStravaActivities(limit = 5)

        assertThat(res.isSuccess).isFalse()
        assertThat(res2.isSuccess).isFalse()
        coVerify(exactly = 2) { strava.getActivities(perPage = 5) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get courses`() = runTest {
        val courses = listOf(
            Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType(id = 1, key = "running")),
            Course(id = 2, name = "course 2", distance = 15007.00, type = ActivityType(id = 10, key = "road_biking"))
        )
        coEvery { connect.getCourses() } returns Response.success(courses)

        val expected = listOf(
            CoreCourse(id = 1, name = "course 1", distance = 10234.00, type = CoreActivityType.Running),
            CoreCourse(id = 2, name = "course 2", distance = 15007.00, type = CoreActivityType.RoadBiking),
        )

        val res = repo.getCourses()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)
        coVerify { connect.getCourses() }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get courses - cache`() = runTest {
        val courses = listOf(
            Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType(id = 1, key = "running")),
            Course(id = 2, name = "course 2", distance = 15007.00, type = ActivityType(id = 10, key = "road_biking"))
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
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get courses - force refresh`() = runTest {
        val courses = listOf(
            Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType(id = 1, key = "running")),
            Course(id = 2, name = "course 2", distance = 15007.00, type = ActivityType(id = 10, key = "road_biking"))
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
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get courses - empty list`() = runTest {
        coEvery { connect.getCourses() } returns Response.success(emptyList())

        val res = repo.getCourses()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(emptyList<CoreCourse>())
        coVerify { connect.getCourses() }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get courses - null`() = runTest {
        coEvery { connect.getCourses() } returns Response.success(null)

        val res = repo.getCourses()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(emptyList<CoreCourse>())
        coVerify { connect.getCourses() }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get courses - failure`() = runTest {
        coEvery { connect.getCourses() } returns Response.error<List<Course>>(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.getCourses()

        assertThat(res.isSuccess).isFalse()
        coVerify { connect.getCourses() }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Get courses - failure cache`() = runTest {
        coEvery { connect.getCourses() } returns Response.error<List<Course>>(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.getCourses()
        val res2 = repo.getCourses()

        assertThat(res.isSuccess).isFalse()
        assertThat(res2.isSuccess).isFalse()
        coVerify(exactly = 2) { connect.getCourses() }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Update activity`() = runTest {
        coEvery { connect.updateActivity(any(), any()) } returns Response.success(Unit)

        val activity = CoreActivity(
            id = 1,
            name = "activity",
            distance = 17803.00,
            type = CoreActivityType.Cycling
        )
        val name = "newName"
        val eventType = CoreEventType.Training
        val course = CoreCourse(1, "course", 10234.00, CoreActivityType.Cycling)
        val water = 2
        val effort = 50f
        val feel = 80f

        val expectedRequest = UpdateActivity(
            id = 1,
            name = name,
            eventType = EventType(id = eventType.id, key = eventType.key),
            metadata = Metadata(courseId = course.id),
            summary = Summary(water = water, feel = feel, effort = effort)
        )

        val res = repo.updateActivity(
            activity = activity,
            name = name,
            eventType = eventType,
            course = course,
            water = water,
            effort = effort,
            feel = feel
        )

        assertThat(res.isSuccess).isTrue()
        coVerify { connect.updateActivity(activity.id, expectedRequest) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Update activity - failure`() = runTest {
        coEvery { connect.updateActivity(any(), any()) } returns Response.error<Unit>(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))
        val activity = CoreActivity(
            id = 1,
            name = "activity",
            distance = 17803.00,
            type = CoreActivityType.Cycling
        )
        val name = "newName"
        val eventType = CoreEventType.Training
        val course = CoreCourse(1, "course", 10234.00, CoreActivityType.Cycling)
        val water = 2

        val expectedRequest = UpdateActivity(
            id = 1,
            name = "newName",
            eventType = EventType(id = eventType.id, key = eventType.key),
            metadata = Metadata(courseId = 1),
            summary = Summary(water = 2, feel = null, effort = null)
        )

        val res = repo.updateActivity(
            activity = activity,
            name = name,
            eventType = eventType,
            course = course,
            water = water,
            effort = null,
            feel = null
        )

        assertThat(res.isSuccess).isFalse()
        coVerify { connect.updateActivity(activity.id, expectedRequest) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Update strava activity`() = runTest {
        coEvery { strava.updateActivity(any(), any()) } returns Response.success(Unit)

        val activity = CoreActivity(
            id = 1,
            name = "activity",
            distance = 17803.00,
            type = CoreActivityType.Cycling
        )
        val name = "newName"
        val description = "newDescription"
        val commute = true

        val expectedRequest = StravaUpdateActivity(
            name = name,
            description = description,
            commute = commute
        )

        val res = repo.updateStravaActivity(
            activity = activity,
            name = name,
            description = description,
            commute = commute
        )

        assertThat(res.isSuccess).isTrue()
        coVerify { strava.updateActivity(activity.id, expectedRequest) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Update strava activity - failure`() = runTest {
        coEvery { strava.updateActivity(any(), any()) } returns Response.error<Unit>(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val activity = CoreActivity(
            id = 1,
            name = "activity",
            distance = 17803.00,
            type = CoreActivityType.Cycling
        )
        val name = "newName"
        val description = "newDescription"
        val commute = true

        val expectedRequest = StravaUpdateActivity(
            name = name,
            description = description,
            commute = commute
        )

        val res = repo.updateStravaActivity(
            activity = activity,
            name = name,
            description = description,
            commute = commute
        )

        assertThat(res.isSuccess).isFalse()
        coVerify { strava.updateActivity(activity.id, expectedRequest) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Update strava profile`() = runTest {
        coEvery { strava.updateProfile(any()) } returns Response.success(Unit)

        val expectedRequest = StravaUpdateProfile(weight = 75.9f)

        val res = repo.updateStravaProfile(weight = 75.9f)

        assertThat(res.isSuccess).isTrue()
        coVerify { strava.updateProfile(expectedRequest) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Update strava profile - failure`() = runTest {
        coEvery { strava.updateProfile(any(), ) } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val expectedRequest = StravaUpdateProfile(weight = 75.9f)

        val res = repo.updateStravaProfile(weight = 75.9f)

        assertThat(res.isSuccess).isFalse()
        coVerify { strava.updateProfile(expectedRequest) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Upload file`() = runTest {
        val testFile = File.createTempFile("test", "test")

        coEvery { connect.uploadFile(any()) } returns Response.success(Unit)

        val res = repo.uploadFile(testFile)

        assertThat(res.isSuccess).isTrue()
        coVerify { connect.uploadFile(any()) }
        confirmVerified(dao, connect, strava)
    }

    @Test
    fun `Upload file - failure`() = runTest {
        val testFile = File.createTempFile("test", "test")

        coEvery { connect.uploadFile(any()) } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.uploadFile(testFile)

        assertThat(res.isSuccess).isFalse()
        coVerify { connect.uploadFile(any()) }
        confirmVerified(dao, connect, strava)
    }
}