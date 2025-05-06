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
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
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

    private var server: MockWebServer = MockWebServer()
    private lateinit var api: GarminConnect
    private val authInterceptor = mockk<AuthInterceptor>()

    val chain = slot<Interceptor.Chain>()

    private val testFile = File.createTempFile("test", "test")
    private val fitFile =
        MultipartBody.Part.createFormData("fit", testFile.name, testFile.asRequestBody())

    @Before
    fun setup() {
        server.start()
        api = GarminConnect.client(authInterceptor, server.url("/").toString())
        every {
            authInterceptor.intercept(capture(chain))
        } answers {
            chain.captured.proceed(chain.captured.request())
        }
    }

    @After
    fun tearDown() {
        clearAllMocks()
        server.shutdown()
    }

    @Test
    fun `Get user profile`() = runTest {
        val response = MockResponse().setResponseCode(200).setBody(userProfileJson)
        server.enqueue(response)

        val res = api.getUserProfile()

        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(UserProfile("Paul", "https://profile.image.com/large.jpg"))
        verify { authInterceptor.intercept(any()) }
        confirmVerified(authInterceptor)
    }

    @Test
    fun `Get user profile - failure`() = runTest {
        val response = MockResponse().setResponseCode(400)
        server.enqueue(response)

        val res = api.getUserProfile()

        assertThat(res.isSuccessful).isFalse()
        verify { authInterceptor.intercept(any()) }
        confirmVerified(authInterceptor)
    }

    @Test
    fun `Upload file`() = runTest {
        val response = MockResponse().setResponseCode(200)
        server.enqueue(response)

        val res = api.uploadFile(fitFile)

        val request = server.takeRequest()
        assertThat(request.method).isEqualTo("POST")
        assertThat(request.requestUrl?.toUrl()?.path).isEqualTo("/upload-service/upload")
        assertThat(res.isSuccessful).isTrue()
        verify { authInterceptor.intercept(any()) }
        confirmVerified(authInterceptor)
    }

    @Test
    fun `Upload file - failure`() = runTest {
        val response = MockResponse().setResponseCode(400)
        server.enqueue(response)

        val res = api.uploadFile(fitFile)

        assertThat(res.isSuccessful).isFalse()
        verify { authInterceptor.intercept(any()) }
        confirmVerified(authInterceptor)
    }

    @Test
    fun `Get activities`() = runTest {
        val response = MockResponse().setResponseCode(200).setBody(activitiesJson)
        server.enqueue(response)

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
        confirmVerified(authInterceptor)
    }

    @Test
    fun `Get activities - empty`() = runTest {
        val response = MockResponse().setResponseCode(200).setBody("[]")
        server.enqueue(response)

        val res = api.getActivities(limit = 3)

        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(emptyList<Activity>())
        verify { authInterceptor.intercept(any()) }
        confirmVerified(authInterceptor)
    }

    @Test
    fun `Get activities - failure`() = runTest {
        val response = MockResponse().setResponseCode(400)
        server.enqueue(response)

        val res = api.getActivities(limit = 3)

        assertThat(res.isSuccessful).isFalse()
        verify { authInterceptor.intercept(any()) }
        confirmVerified(authInterceptor)
    }

    @Test
    fun `Update activity`() = runTest {
        val response = MockResponse().setResponseCode(200)
        server.enqueue(response)
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
        confirmVerified(authInterceptor)
    }

    @Test
    fun `Update activity - failure`() = runTest {
        val response = MockResponse().setResponseCode(400)
        server.enqueue(response)
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
        confirmVerified(authInterceptor)
    }

    @Test
    fun `Get course`() = runTest {
        val response = MockResponse().setResponseCode(200).setBody(coursesJson)
        server.enqueue(response)

        val res = api.getCourses()

        val expected = listOf(
            Course(id = 1, name = "Course 1", distance = 10234.81, type = ActivityType(id = 1, key = "running")),
            Course(id = 2, name = "Course 2", distance = 15007.59, type = ActivityType(id = 10, key = "road_biking"))
        )

        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(expected)
        verify { authInterceptor.intercept(any()) }
        confirmVerified(authInterceptor)
    }

    @Test
    fun `Get course - empty`() = runTest {
        val response = MockResponse().setResponseCode(200).setBody("[]")
        server.enqueue(response)

        val res = api.getCourses()

        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEmpty()
        verify { authInterceptor.intercept(any()) }
        confirmVerified(authInterceptor)
    }

    @Test
    fun `Get course - failure`() = runTest {
        val response = MockResponse().setResponseCode(400)
        server.enqueue(response)

        val res = api.getCourses()

        assertThat(res.isSuccessful).isFalse()
        verify { authInterceptor.intercept(any()) }
        confirmVerified(authInterceptor)
    }
}