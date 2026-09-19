package paufregi.connectfeed.data.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.MockServer
import paufregi.connectfeed.authToken
import paufregi.connectfeed.connectDispatcher
import paufregi.connectfeed.connectPort
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.models.Workout
import paufregi.connectfeed.data.api.garmin.models.Activity
import paufregi.connectfeed.data.api.garmin.models.ActivityType
import paufregi.connectfeed.data.api.garmin.models.Course
import paufregi.connectfeed.data.api.garmin.models.Gear
import paufregi.connectfeed.data.datastore.AuthStore
import paufregi.connectfeed.garminSSODispatcher
import paufregi.connectfeed.garminSSOPort
import java.io.File
import javax.inject.Inject
import kotlin.time.Instant
import paufregi.connectfeed.core.models.Activity as CoreActivity
import paufregi.connectfeed.core.models.ActivityType as CoreActivityType
import paufregi.connectfeed.core.models.Course as CoreCourse
import paufregi.connectfeed.core.models.EventType as CoreEventType
import paufregi.connectfeed.core.models.Gear as CoreGear

@HiltAndroidTest
class GarminRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: GarminRepository

    @Inject
    lateinit var authStore: AuthStore

    @JvmField @Rule val connectServer = MockServer.createSecure(connectPort, connectDispatcher)
    @JvmField @Rule val garminSSOServer = MockServer.createSecure(garminSSOPort, garminSSODispatcher)

    @Before
    fun setup() {
        hiltRule.inject()
        runBlocking(Dispatchers.IO){
            authStore.saveGarminToken(authToken)
        }
    }

    @After
    fun tearDown() {
        runBlocking(Dispatchers.IO){
            authStore.clear()
        }
    }

    @Test
    fun `Get user profile`() = runTest {
        val expected = User(1, "Paul", "https://profile.image.com/large.jpg")

        val res = repo.getUserProfile()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)
    }

    @Test
    fun `Get activities`() = runTest {
        val expected = listOf(
            Activity(
                id = 1,
                name = "Activity 1",
                type = ActivityType(10, "road_bike", CoreActivityType.RoadBiking),
                eventType = CoreEventType.Transportation,
                distance = 17804.00,
                trainingEffectLabel = "RECOVERY",
                beginTimestamp = Instant.fromEpochMilliseconds(1729754100000),
                workoutId = 1
            ),
            Activity(
                id = 2,
                name = "Activity 2",
                type = ActivityType(10, "road_bike", CoreActivityType.RoadBiking),
                eventType = CoreEventType.Transportation,
                distance = 17760.00,
                trainingEffectLabel = "RECOVERY",
                beginTimestamp = Instant.fromEpochMilliseconds(1729705968000),
                workoutId = 2
            )
        )

        val res = repo.getActivities(5)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)
    }

    @Test
    fun `Get gears`() = runTest {
        val expected = listOf(
            Gear(
                id = "522f1e21-0822-451d-88a3-b0b661802c2f",
                brand = "Mizuno",
                model = "Neo Vista",
                name = null,
                type = GearType.Shoe,
                distance = 51955.4501953125
            ),
            Gear(
                id = "789dccf8-b669-4903-bf46-7d8d9369124e",
                brand = "Giant",
                model = "Contend AR",
                name = "Nova",
                type = GearType.Bike,
                distance = 17226955.28363037
            ),
        )

        val res = repo.getGears()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)
    }

    @Test
    fun `Get courses`() = runTest {
        val expected = listOf(
            Course(id = 1, name = "Course 1", distance = 10235.00, type = ActivityType(10, "running", CoreActivityType.Running)),
            Course(id = 2, name = "Course 2", distance = 15008.00, type = ActivityType(10, "road_bike", CoreActivityType.RoadBiking)),
        )

        val res = repo.getCourses()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)
    }

    @Test
    fun `Get workout`() = runTest {
        val expected = Workout(1, "Power - Zone 6")

        val res = repo.getWorkout(1)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)
    }

    @Test
    fun `Update activity`() = runTest {
        val activity = CoreActivity(
            id = 1,
            name = "activity",
            type = CoreActivityType.Cycling,
            eventType = EventType.Training,
            date = Instant.parse("2024-10-24T07:15:30Z"),
        )
        val name = "newName"
        val description = "newDescription"
        val eventType = CoreEventType.Training
        val course = CoreCourse(1, "course", 10234.00, CoreActivityType.Cycling)
        val water = 2
        val effort = 50f
        val feel = 80f
        val gear = CoreGear("bike-1", "bike", GearType.Bike, 1000)

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
    }

    @Test
    fun `Upload file`() = runTest {
        val testFile = File.createTempFile("test", "test")
        testFile.deleteOnExit()
        val res = repo.uploadFile(testFile)

        assertThat(res.isSuccess).isTrue()
    }
}