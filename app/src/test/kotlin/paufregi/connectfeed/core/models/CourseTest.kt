package paufregi.connectfeed.core.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.LocalDateTime

class CourseTest {

    private val course = Course(1, "Park loop", 5.0, ActivityType.Running)
    private val activity = Activity(
        id = 2,
        name = "Morning run",
        type = ActivityType.Running,
        eventType = EventType.Training,
        date = LocalDateTime.MIN,
    )

    @Test
    fun `compatible - same type`() {
        assertThat(course.compatible(activity)).isTrue()
        assertThat(course.copy(type = ActivityType.Cycling).compatible(
            activity.copy(type = ActivityType.Cycling)
        )).isTrue()
    }

    @Test
    fun `compatible - parent and subtype`() {
        assertThat(course.compatible(activity.copy(type = ActivityType.TrailRunning))).isTrue()
        assertThat(course.copy(type = ActivityType.TrailRunning).compatible(activity)).isTrue()
    }

    @Test
    fun `compatible - same parent`() {
        assertThat(course.copy(type = ActivityType.TrailRunning).compatible(
            activity.copy(type = ActivityType.TrackRunning)
        )).isTrue()
    }

    @Test
    fun `compatible - different types`() {
        assertThat(course.compatible(activity.copy(type = ActivityType.Cycling))).isFalse()
        assertThat(course.copy(type = ActivityType.Cycling).compatible(activity)).isFalse()
        assertThat(course.compatible(activity.copy(type = ActivityType.Walking))).isFalse()
    }

    @Test
    fun `compatible - different parents`() {
        assertThat(course.copy(type = ActivityType.TrailRunning).compatible(
            activity.copy(type = ActivityType.RoadBiking)
        )).isFalse()
    }

    @Test
    fun `compatible - wildcard course types`() {
        assertThat(course.copy(type = ActivityType.Any).compatible(activity)).isTrue()
        assertThat(course.copy(type = ActivityType.Unknown).compatible(activity)).isTrue()
    }

    @Test
    fun `compatible - wildcard activity types`() {
        assertThat(course.compatible(activity.copy(type = ActivityType.Any))).isTrue()
        assertThat(course.compatible(activity.copy(type = ActivityType.Unknown))).isTrue()
    }
}
