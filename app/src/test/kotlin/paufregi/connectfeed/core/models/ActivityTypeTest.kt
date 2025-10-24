package paufregi.connectfeed.core.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ActivityTypeTest {

    @Test
    fun `Match type`() {
        assertThat(ActivityType.Running.match(ActivityType.Running)).isTrue()
    }

    @Test
    fun `Match any`() {
        assertThat(ActivityType.Any.match(ActivityType.Running)).isTrue()
    }

    @Test
    fun `Match null`() {
        assertThat(ActivityType.Any.match(null)).isTrue()
    }

    @Test
    fun `No match`() {
        assertThat(ActivityType.Cycling.match(ActivityType.Running)).isFalse()
    }
}