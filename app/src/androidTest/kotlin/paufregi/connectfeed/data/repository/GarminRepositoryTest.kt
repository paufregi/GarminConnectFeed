package paufregi.connectfeed.data.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.preferences.core.edit
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.connectDispatcher
import paufregi.connectfeed.connectPort
import paufregi.connectfeed.consumer
import paufregi.connectfeed.core.models.Activity as CoreActivity
import paufregi.connectfeed.core.models.ActivityType as CoreActivityType
import paufregi.connectfeed.core.models.Course as CoreCourse
import paufregi.connectfeed.core.models.EventType as CoreEventType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.database.GarminDatabase
import paufregi.connectfeed.data.datastore.AuthStore
import paufregi.connectfeed.garminSSODispatcher
import paufregi.connectfeed.garminSSOPort
import paufregi.connectfeed.garthDispatcher
import paufregi.connectfeed.garthPort
import paufregi.connectfeed.oauth1
import paufregi.connectfeed.oauth2
import paufregi.connectfeed.sslSocketFactory
import java.io.File
import javax.inject.Inject

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

    @Inject
    lateinit var database: GarminDatabase

    private val connectServer = MockWebServer()
    private val garminSSOServer = MockWebServer()
    private val garthServer = MockWebServer()

    @Before
    fun setup() {
        hiltRule.inject()
        connectServer.useHttps(sslSocketFactory, false)
        connectServer.start(connectPort)
        garminSSOServer.useHttps(sslSocketFactory, false)
        garminSSOServer.start(garminSSOPort)
        garthServer.useHttps(sslSocketFactory, false)
        garthServer.start(garthPort)

        connectServer.dispatcher = connectDispatcher
        garthServer.dispatcher = garthDispatcher
        garminSSOServer.dispatcher = garminSSODispatcher
    }

    @After
    fun tearDown() {
        connectServer.shutdown()
        garminSSOServer.shutdown()
        garthServer.shutdown()
        database.close()
        runBlocking(Dispatchers.IO){
            authStore.dataStore.edit { it.clear() }
        }
    }


    @Test
    fun `Fetch user`() = runTest {
        authStore.saveConsumer(consumer)
        authStore.saveOAuth1(oauth1)
        authStore.saveOAuth2(oauth2)

        val expected = User("Paul", "https://profile.image.com/large.jpg")

        val res = repo.fetchUser()

        assertThat(res.isSuccessful).isTrue()
        res as Result.Success
        assertThat(res.data).isEqualTo(expected)
    }

    @Test
    fun `Store profiles`() = runTest {
        val profile = Profile(id = 1, name = "test")
        repo.saveProfile(profile)
        assertThat(repo.getProfile(profile.id)).isEqualTo(profile)

        repo.deleteProfile(profile)
        assertThat(repo.getProfile(profile.id)).isNull()

        repo.getAllProfiles().test{
            assertThat(awaitItem()).isEmpty()
            repo.saveProfile(profile)
            assertThat(awaitItem()).containsExactly(profile)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Get latest activities`() = runTest {
        authStore.saveConsumer(consumer)
        authStore.saveOAuth1(oauth1)
        authStore.saveOAuth2(oauth2)

        val expected = listOf(
            CoreActivity(id = 1, name = "Activity 1", distance = 17804.00, type = CoreActivityType.Cycling),
            CoreActivity(id = 2, name = "Activity 2", distance = 17760.00, type = CoreActivityType.Cycling)
        )

        val res = repo.getLatestActivities(5)

        assertThat(res.isSuccessful).isTrue()
        res as Result.Success
        assertThat(res.data).isEqualTo(expected)
    }

    @Test
    fun `Get courses`() = runTest {
        authStore.saveConsumer(consumer)
        authStore.saveOAuth1(oauth1)
        authStore.saveOAuth2(oauth2)

        val expected = listOf(
            CoreCourse(id = 1, name = "Course 1", distance = 10235.00, type = CoreActivityType.Running),
            CoreCourse(id = 2, name = "Course 2", distance = 15008.00, type = CoreActivityType.Cycling),
        )

        val res = repo.getCourses()

        assertThat(res.isSuccessful).isTrue()
        res as Result.Success
        assertThat(res.data).isEqualTo(expected)
    }

    @Test
    fun `Get event types`() = runTest {
        authStore.saveConsumer(consumer)
        authStore.saveOAuth1(oauth1)
        authStore.saveOAuth2(oauth2)

        val expected = listOf(
            CoreEventType(id = 1, name = "Race"),
            CoreEventType(id = 2, name = "Training"),
        )

        val res = repo.getEventTypes()

        assertThat(res.isSuccessful).isTrue()
        res as Result.Success
        assertThat(res.data).isEqualTo(expected)
    }

    @Test
    fun `Update activity`() = runTest {
        authStore.saveConsumer(consumer)
        authStore.saveOAuth1(oauth1)
        authStore.saveOAuth2(oauth2)

        val activity = CoreActivity(id = 1, name = "activity", distance = 17803.00, type = CoreActivityType.Cycling)
        val profile = Profile(
            name = "newName",
            rename = true,
            eventType = EventType(id = 1, name = "event1"),
            activityType = CoreActivityType.Cycling,
            course = Course(id = 1, name = "course1", distance = 10234.00, type = CoreActivityType.Cycling),
            water = 1
        )

        val res = repo.updateActivity(activity, profile, 50f, 90f)

        assertThat(res.isSuccessful).isTrue()
    }

    @Test
    fun `Upload file`() = runTest {
        authStore.saveConsumer(consumer)
        authStore.saveOAuth1(oauth1)
        authStore.saveOAuth2(oauth2)

        val testFile = File.createTempFile("test", "test")
        testFile.deleteOnExit()
        val res = repo.uploadFile(testFile)

        assertThat(res.isSuccessful).isTrue()
    }
}