package paufregi.connectfeed.data.repository.converters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.api.garmin.models.UserProfileDto
import paufregi.connectfeed.data.api.garmin.models.WorkoutDto
import paufregi.connectfeed.core.models.Workout as CoreWorkout

class UserConverterTest {

    @Test
    fun `DTO to core`() {
        val dto = UserProfileDto(
            id = 1,
            name = "name",
            avatarUrl = "avatarUrls"
        )

        val user = User(
            id = 1,
            name = "name",
            profileImageUrl = "avatarUrls"
        )

        assertThat(dto.toCore()).isEqualTo(user)
    }
}
