package paufregi.connectfeed.core.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ActivityTypeTest {
    @Test
    fun `compatible - Any`() {
        // This
        assertThat(ActivityType.Any.compatible(ActivityType.Running)).isTrue()
        assertThat(ActivityType.Any.compatible(ActivityType.Cycling)).isTrue()

        // Other
        assertThat(ActivityType.Running.compatible(ActivityType.Any)).isTrue()
        assertThat(ActivityType.Cycling.compatible(ActivityType.Any)).isTrue()

        // Both
        assertThat(ActivityType.Any.compatible(ActivityType.Any)).isTrue()
    }

    @Test
    fun `compatible - same type`() {
        assertThat(ActivityType.Running.compatible(ActivityType.Running)).isTrue()
        assertThat(ActivityType.Cycling.compatible(ActivityType.Cycling)).isTrue()
    }

    @Test
    fun `compatible - same parent`() {
        assertThat(ActivityType.TrailRunning.compatible(ActivityType.TrackRunning)).isTrue()
        assertThat(ActivityType.EBiking.compatible(ActivityType.RoadBiking)).isTrue()
    }

    @Test
    fun `compatible - mix`() {
        assertThat(ActivityType.TrailRunning.compatible(ActivityType.Running)).isTrue()
        assertThat(ActivityType.Running.compatible(ActivityType.TrailRunning)).isTrue()
        assertThat(ActivityType.EBiking.compatible(ActivityType.Cycling)).isTrue()
        assertThat(ActivityType.Cycling.compatible(ActivityType.EBiking)).isTrue()
    }

    @Test
    fun `compatible - nope`() {
        assertThat(ActivityType.Running.compatible(ActivityType.Cycling)).isFalse()
        assertThat(ActivityType.TrailRunning.compatible(ActivityType.EBiking)).isFalse()
        assertThat(ActivityType.Fitness.compatible(ActivityType.Running)).isFalse()
        assertThat(ActivityType.Swimming.compatible(ActivityType.Cycling)).isFalse()
        assertThat(ActivityType.Walking.compatible(ActivityType.Running)).isFalse()
    }

    @Test
    fun `allow course`() {
        assertThat(ActivityType.Running.allowCourse).isTrue()
        assertThat(ActivityType.TrailRunning.allowCourse).isTrue()
        assertThat(ActivityType.Cycling.allowCourse).isTrue()
        assertThat(ActivityType.RoadBiking.allowCourse).isTrue()
    }

    @Test
    fun `not allow course`() {
        assertThat(ActivityType.Any.allowCourse).isFalse()
        assertThat(ActivityType.Swimming.allowCourse).isFalse()
        assertThat(ActivityType.Fitness.allowCourse).isFalse()
        assertThat(ActivityType.Other.allowCourse).isFalse()
        assertThat(ActivityType.Unknown.allowCourse).isFalse()
    }
}
