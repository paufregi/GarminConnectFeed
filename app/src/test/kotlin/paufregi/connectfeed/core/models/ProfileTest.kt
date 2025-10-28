package paufregi.connectfeed.core.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ProfileTest {

    @Test
    fun `Compatible with activity`() {
        val profile = Profile(category = ActivityCategory.Running)
        val activity = Activity(1, "Running", ActivityType.Running)

        assertThat(profile.compatibleWith(activity)).isTrue()
    }

    @Test
    fun `Compatible with activity - Any`() {
        val profile = Profile(category = ActivityCategory.Any)
        val activity = Activity(1, "Running", ActivityType.Running)

        assertThat(profile.compatibleWith(activity)).isTrue()
    }
    @Test
    fun `Not compatible with activity`() {
        val profile = Profile(category = ActivityCategory.Running)
        val activity = Activity(1, "Cycling", ActivityType.Cycling)

        assertThat(profile.compatibleWith(activity)).isFalse()
    }
}