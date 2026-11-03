package paufregi.connectfeed.data.repository.converters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.data.api.garmin.models.ActivityTypeDto
import paufregi.connectfeed.data.api.garmin.models.CourseDto

class CourseConverterTest {

    @Test
    fun `DTO to core`() {
        val dto = CourseDto(
            id = 1,
            name = "course",
            distance = 1234.5,
            type = ActivityTypeDto(id = 1, key = "running"),
        )

        val course = Course(
            id = 1,
            name = "course",
            distance = 1234.5,
            type = ActivityType.Running
        )

        assertThat(dto.toCore()).isEqualTo(course)
    }
}