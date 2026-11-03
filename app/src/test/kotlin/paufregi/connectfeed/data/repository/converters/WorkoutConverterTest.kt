package paufregi.connectfeed.data.repository.converters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.data.api.garmin.models.WorkoutDto
import paufregi.connectfeed.core.models.Workout as CoreWorkout

class WorkoutConverterTest {

    @Test
    fun `DTO to core`() {
        val dto = WorkoutDto(id = 1, name = "name",)

        val workout = CoreWorkout(id = 1, name = "name")

        assertThat(dto.toCore()).isEqualTo(workout)
    }
}
