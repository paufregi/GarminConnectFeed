package paufregi.connectfeed.data.api.garmin.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.Workout as CoreWorkout

class WorkoutTest {

    @Test
    fun `To Core workout`() {
        val workout = Workout(id = 1, name = "name",)

        val coreWorkout = CoreWorkout(id = 1, name = "name")

        assertThat(workout.toCore()).isEqualTo(coreWorkout)
    }
}
