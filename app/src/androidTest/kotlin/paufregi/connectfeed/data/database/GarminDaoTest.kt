package paufregi.connectfeed.data.database

import android.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.ProfileType
import paufregi.connectfeed.data.database.entities.ProfileEntity
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
class GarminDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: GarminDatabase

    @Inject
    lateinit var dao: GarminDao

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `Save delete and retrieve profiles`() = runTest {
        val profile = ProfileEntity(
            id = 1,
            userId = 1,
            name = "profile1",
            eventType = EventType.Training,
            type = ProfileType.Running,
            course = Course(id = 1, name = "course1", distance = 1000.50, type = ActivityType.Running),
            water = 100,
            rename = true,
            customWater = true,
            feelAndEffort = true,
            trainingEffect = true
        )
        val profile2 = ProfileEntity(
            id = 2,
            userId = 2,
            name = "profile2",
            eventType = EventType.Training,
            type = ProfileType.Running,
            course = Course(id = 1, name = "course2", distance = 2000.50, type = ActivityType.Running),
            water = 200,
            rename = true,
            customWater = true,
            feelAndEffort = true,
            trainingEffect = true
        )

        dao.saveProfile(profile)
        assertThat(dao.getProfile(profile.id)).isEqualTo(profile)

        dao.saveProfile(profile2)
        assertThat(dao.getProfile(profile2.id)).isEqualTo(profile2)

        dao.deleteProfile(profile)
        assertThat(dao.getProfile(profile.id)).isNull()

        dao.getAllProfiles(1).test {
            assertThat(awaitItem()).isEmpty()
            dao.saveProfile(profile)
            assertThat(awaitItem()).containsExactly(profile)
            cancelAndIgnoreRemainingEvents()
        }
    }
}