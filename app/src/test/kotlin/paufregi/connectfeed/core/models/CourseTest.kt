package paufregi.connectfeed.core.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CourseTest {

    @Test
    fun `Compatible with profile`() {
        val profile = Profile(category = ActivityCategory.Running)
        val course = Course(1, "Running", 1.0, ActivityType.Running)

        assertThat(course.compatibleWith(profile)).isTrue()
    }

    @Test
    fun `Compatible with profile - Any`() {
        val profile = Profile(category = ActivityCategory.Any)
        val course = Course(1, "Running", 1.0, ActivityType.Running)

        assertThat(course.compatibleWith(profile)).isTrue()
    }

    @Test
    fun `Not compatible with profile`() {
        val profile = Profile(category = ActivityCategory.Running)
        val course = Course(1, "Cycling", 1.0, ActivityType.Cycling)

        assertThat(course.compatibleWith(profile)).isFalse()
    }

    @Test
    fun `Not compatible with profile - null`() {
        val course = Course(1, "Cycling", 1.0, ActivityType.Cycling)

        assertThat(course.compatibleWith(null)).isFalse()
    }
}