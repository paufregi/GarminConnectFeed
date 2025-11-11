package paufregi.connectfeed.data.api.garmin

import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.MockWebServerRule
import paufregi.connectfeed.activitiesJson
import paufregi.connectfeed.coursesJson
import paufregi.connectfeed.data.api.garmin.interceptors.AuthInterceptor
import paufregi.connectfeed.data.api.garmin.models.Activity
import paufregi.connectfeed.data.api.garmin.models.ActivityType
import paufregi.connectfeed.data.api.garmin.models.Course
import paufregi.connectfeed.data.api.garmin.models.EventType
import paufregi.connectfeed.data.api.garmin.models.Metadata
import paufregi.connectfeed.data.api.garmin.models.Summary
import paufregi.connectfeed.data.api.garmin.models.UpdateActivity
import paufregi.connectfeed.data.api.garmin.models.UserProfile
import paufregi.connectfeed.userProfileJson
import java.io.File

class GarminConnectTest {

    @JvmField @Rule val server = MockWebServerRule()

    private lateinit var api: GarminConnect
    private val authInterceptor = mockk<AuthInterceptor>()

    val chain = slot<Interceptor.Chain>()

    private val testFile = File.createTempFile("test", "test")
    private val fitFile =
        MultipartBody.Part.createFormData("fit", testFile.name, testFile.asRequestBody())

    @Before
    fun setup() {
        api = GarminConnect.client(authInterceptor, server.url("/").toString())
        every {
            authInterceptor.intercept(capture(chain))
        } answers {
            chain.captured.proceed(chain.captured.request())
        }
    }

    @After
    fun tearDown() {
        confirmVerified(authInterceptor)
        clearAllMocks()
    }

    @Test
    fun `Get user profile`() = runTest {
        server.enqueue(code = 200, body = userProfileJson)

        val res = api.getUserProfile()

        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(UserProfile(1, "Paul", "https://profile.image.com/large.jpg"))
        verify { authInterceptor.intercept(any()) }
    }

    @Test
    fun `Get user profile - failure`() = runTest {
        server.enqueue(400)

        val res = api.getUserProfile()

        assertThat(res.isSuccessful).isFalse()
        verify { authInterceptor.intercept(any()) }
    }

    @Test
    fun `Upload file`() = runTest {
        server.enqueue(200)

        val res = api.uploadFile(fitFile)

        val request = server.takeRequest()
        assertThat(request.method).isEqualTo("POST")
        assertThat(request.url.encodedPath).isEqualTo("/upload-service/upload")
        assertThat(res.isSuccessful).isTrue()
        verify { authInterceptor.intercept(any()) }
    }

    @Test
    fun `Upload file - failure`() = runTest {
        server.enqueue(400)

        val res = api.uploadFile(fitFile)

        assertThat(res.isSuccessful).isFalse()
        verify { authInterceptor.intercept(any()) }
    }

    @Test
    fun `Get activities`() = runTest {
        server.enqueue(code = 200, body = activitiesJson)

        val res = api.getActivities(limit = 3)

        val expected = listOf(
            Activity(
                id = 1,
                name = "Activity 1",
                distance = 17803.69921875,
                trainingEffectLabel = "RECOVERY",
                type = ActivityType(id = 10, key = "road_biking"),
                eventType = EventType(id = 5, key = "transportation"),
                beginTimestamp = 1729754100000
            ),
            Activity(
                id = 2,
                name = "Activity 2",
                distance = 17759.779296875,
                trainingEffectLabel = "RECOVERY",
                type = ActivityType(id = 10, key = "road_biking"),
                eventType = EventType(id = 5, key = "transportation"),
                beginTimestamp = 1729705968000
            )
        )

        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(expected)
        verify { authInterceptor.intercept(any()) }
    }

    @Test
    fun `Get activities - empty`() = runTest {
        server.enqueue(code = 200, body = "[]")

        val res = api.getActivities(limit = 3)

        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(emptyList<Activity>())
        verify { authInterceptor.intercept(any()) }
    }

    @Test
    fun `Get activities - failure`() = runTest {
        server.enqueue(400)

        val res = api.getActivities(limit = 3)

        assertThat(res.isSuccessful).isFalse()
        verify { authInterceptor.intercept(any()) }
    }

    @Test
    fun `Update activity`() = runTest {
        server.enqueue(200)
        val updateActivity = UpdateActivity(
            id = 1,
            name = "newName",
            eventType = EventType(id = 1, key = "key"),
            metadata = Metadata(courseId = 1),
            summary = Summary(500, null, null),
        )
        val res = api.updateActivity(id = 1, updateActivity = updateActivity)

        assertThat(res.isSuccessful).isTrue()
        verify { authInterceptor.intercept(any()) }
    }

    @Test
    fun `Update activity - failure`() = runTest {
        server.enqueue(400)
        val updateActivity = UpdateActivity(
            id = 1,
            name = "newName",
            eventType = EventType(id = 1, key = "key"),
            metadata = Metadata(courseId = 1),
            summary = Summary(500, null, null),
        )
        val res = api.updateActivity(id = 1, updateActivity = updateActivity)

        assertThat(res.isSuccessful).isFalse()
        verify { authInterceptor.intercept(any()) }
    }

    @Test
    fun `Get course`() = runTest {
        server.enqueue(code = 200, body = coursesJson)

        val res = api.getCourses()

        val expected = listOf(
            Course(id = 1, name = "Course 1", distance = 10234.81, type = ActivityType(id = 1, key = "running")),
            Course(id = 2, name = "Course 2", distance = 15007.59, type = ActivityType(id = 10, key = "road_biking"))
        )

        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(expected)
        verify { authInterceptor.intercept(any()) }
    }

    @Test
    fun `Get course - empty`() = runTest {
        server.enqueue(code = 200, body = "[]")

        val res = api.getCourses()

        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEmpty()
        verify { authInterceptor.intercept(any()) }
    }

    @Test
    fun `Get course - failure`() = runTest {
        server.enqueue(400)

        val res = api.getCourses()

        assertThat(res.isSuccessful).isFalse()
        verify { authInterceptor.intercept(any()) }
    }
}