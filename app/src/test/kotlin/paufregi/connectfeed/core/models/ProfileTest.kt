package paufregi.connectfeed.core.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.LocalDateTime

class ProfileTest {

    private val profile = Profile(1, "Morning run", ActivityType.Running)
    private val activity = Activity(
        id = 2,
        name = "Park run",
        type = ActivityType.Running,
        eventType = EventType.Training,
        date = LocalDateTime.MIN,
    )

    @Test
    fun `compatible - same type`() {
        assertThat(profile.compatible(activity)).isTrue()
        assertThat(profile.copy(type = ActivityType.Cycling).compatible(
            activity.copy(type = ActivityType.Cycling)
        )).isTrue()
    }

    @Test
    fun `compatible - parent and subtype`() {
        assertThat(profile.compatible(activity.copy(type = ActivityType.TrailRunning))).isTrue()
        assertThat(profile.copy(type = ActivityType.TrailRunning).compatible(activity)).isTrue()
    }

    @Test
    fun `compatible - same parent`() {
        assertThat(profile.copy(type = ActivityType.TrailRunning).compatible(
            activity.copy(type = ActivityType.TrackRunning)
        )).isTrue()
    }

    @Test
    fun `compatible - different types`() {
        assertThat(profile.compatible(activity.copy(type = ActivityType.Cycling))).isFalse()
        assertThat(profile.copy(type = ActivityType.Cycling).compatible(activity)).isFalse()
        assertThat(profile.compatible(activity.copy(type = ActivityType.Walking))).isFalse()
    }

    @Test
    fun `compatible - different parents`() {
        assertThat(profile.copy(type = ActivityType.TrailRunning).compatible(
            activity.copy(type = ActivityType.RoadBiking)
        )).isFalse()
    }

    @Test
    fun `compatible - wildcard profile types`() {
        assertThat(Profile().compatible(activity)).isTrue()
        assertThat(profile.copy(type = ActivityType.Unknown).compatible(activity)).isTrue()
    }

    @Test
    fun `compatible - wildcard activity types`() {
        assertThat(profile.compatible(activity.copy(type = ActivityType.Any))).isTrue()
        assertThat(profile.compatible(activity.copy(type = ActivityType.Unknown))).isTrue()
    }
}
