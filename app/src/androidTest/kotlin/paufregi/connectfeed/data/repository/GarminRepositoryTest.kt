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
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.database.GarminDatabase
import paufregi.connectfeed.data.datastore.AuthStore
import paufregi.connectfeed.data.datastore.StravaStore
import paufregi.connectfeed.garminSSODispatcher
import paufregi.connectfeed.garminSSOPort
import paufregi.connectfeed.garthDispatcher
import paufregi.connectfeed.garthPort
import paufregi.connectfeed.oauth1
import paufregi.connectfeed.oauth2
import paufregi.connectfeed.sslSocketFactory
import paufregi.connectfeed.stravaDispatcher
import paufregi.connectfeed.stravaPort
import paufregi.connectfeed.stravaToken
import java.io.File
import javax.inject.Inject
import paufregi.connectfeed.core.models.Activity as CoreActivity
import paufregi.connectfeed.core.models.ActivityType as CoreActivityType
import paufregi.connectfeed.core.models.Course as CoreCourse
import paufregi.connectfeed.core.models.EventType as CoreEventType

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
    lateinit var stravaStore: StravaStore

    @Inject
    lateinit var database: GarminDatabase

    private val connectServer = MockWebServer()
    private val garminSSOServer = MockWebServer()
    private val garthServer = MockWebServer()
    private val stravaServer = MockWebServer()

    @Before
    fun setup() {
        hiltRule.inject()
        connectServer.useHttps(sslSocketFactory, false)
        connectServer.start(connectPort)
        garminSSOServer.useHttps(sslSocketFactory, false)
        garminSSOServer.start(garminSSOPort)
        garthServer.useHttps(sslSocketFactory, false)
        garthServer.start(garthPort)
        stravaServer.useHttps(sslSocketFactory, false)
        stravaServer.start(stravaPort)

        connectServer.dispatcher = connectDispatcher
        garthServer.dispatcher = garthDispatcher
        garminSSOServer.dispatcher = garminSSODispatcher
        stravaServer.dispatcher = stravaDispatcher
    }

    @After
    fun tearDown() {
        connectServer.shutdown()
        garminSSOServer.shutdown()
        garthServer.shutdown()
        stravaServer.shutdown()
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
            CoreActivity(id = 1, name = "Activity 1", distance = 17804.00, trainingEffect = "recovery", type = CoreActivityType.Cycling),
            CoreActivity(id = 2, name = "Activity 2", distance = 17760.00, trainingEffect = "recovery", type = CoreActivityType.Cycling)
        )

        val res = repo.getLatestActivities(5)

        assertThat(res.isSuccessful).isTrue()
        res as Result.Success
        assertThat(res.data).isEqualTo(expected)
    }

    @Test
    fun `Get latest Strava activities`() = runTest {
        stravaStore.saveToken(stravaToken)

        val expected = listOf(
            CoreActivity(id = 1, name = "Happy Friday", distance = 7804.0, type = CoreActivityType.Running),
            CoreActivity(id = 2, name = "Bondcliff", distance = 23676.0, type = CoreActivityType.Cycling)
        )

        val res = repo.getLatestStravaActivities(5)

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
    fun `Update activity`() = runTest {
        authStore.saveConsumer(consumer)
        authStore.saveOAuth1(oauth1)
        authStore.saveOAuth2(oauth2)

        val activity = CoreActivity(id = 1, name = "activity", distance = 17803.00, trainingEffect = "", type = CoreActivityType.Cycling)
        val name = "newName"
        val eventType = CoreEventType.Training
        val course = CoreCourse(1, "course", 10234.00, CoreActivityType.Cycling)
        val water = 2
        val effort = 50f
        val feel = 80f

        val res = repo.updateActivity(
            activity = activity,
            name = name,
            eventType = eventType,
            course = course,
            water = water,
            effort = effort,
            feel = feel
        )

        assertThat(res.isSuccessful).isTrue()
    }

    @Test
    fun `Update strava activity`() = runTest {
        stravaStore.saveToken(stravaToken)

        val activity = CoreActivity(id = 1, name = "activity", distance = 17803.00, trainingEffect = "", type = CoreActivityType.Cycling)
        val name = "newName"
        val description = "description"
        val commute = true

        val res = repo.updateStravaActivity(
            activity = activity,
            name = name,
            description = description,
            commute = commute
        )

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