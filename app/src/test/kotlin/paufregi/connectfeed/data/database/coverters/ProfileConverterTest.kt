package paufregi.connectfeed.data.database.coverters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.data.database.entities.ProfileEntity
import paufregi.connectfeed.user

class ProfileConverterTest {

    val profile = Profile(
        id = 1,
        name = "profile",
        type = ActivityType.Cycling,
        eventType = EventType.Training,
        course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Cycling),
        water = 550,
        rename = true,
        customWater = true,
        feelAndEffort = true,
        trainingEffect = true
    )

    val entityProfile = ProfileEntity(
        id = 1,
        userId = user.id,
        name = "profile",
        type = ActivityType.Cycling,
        eventType = EventType.Training,
        course = Course(id = 1, name = "course 1", distance = 10234.00, type = ActivityType.Cycling),
        water = 550,
        rename = true,
        customWater = true,
        feelAndEffort = true,
        trainingEffect = true
    )

    @Test
    fun `Profile to entity`() {
        val result = profile.toEntity(user.id)

        assertThat(result).isEqualTo(entityProfile)
    }

    @Test
    fun `Entity profile to profile`() {
        val result = entityProfile.toCore()

        assertThat(result).isEqualTo(profile)
    }
}
