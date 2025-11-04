package paufregi.connectfeed.core.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ProfileTypeTest {

    @Test
    fun `Compatible - any`() {
        assertThat(ProfileType.Any.compatible(ActivityType.Running)).isTrue()
    }

    @Test
    fun `Compatible - same`() {
        assertThat(ProfileType.Running.compatible(ActivityType.Running)).isTrue()
    }

    @Test
    fun `Compatible - false`() {
        assertThat(ProfileType.Cycling.compatible(ActivityType.Running)).isTrue()
    }
}
