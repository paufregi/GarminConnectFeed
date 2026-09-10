package paufregi.connectfeed.data.api.strava.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType

class ShoeTest {

    @Test
    fun `toGear conversion`() {
        val shoe = Shoe(
            id = "shoe-1",
            name = "Daily trainers",
            distance = 1000
        )

        val expected = Gear(
            id = "shoe-1",
            name = "Daily trainers",
            type = GearType.Shoe,
            distance = 1000,
        )

        assertThat(shoe.toGear()).isEqualTo(expected)
    }

    @Test
    fun `toGear conversion - no name`() {
        val shoe = Shoe(id = "shoe-1", name = null, distance = 0)

        val expected = Gear(id = "shoe-1", name = "", type = GearType.Shoe, distance = 0)

        assertThat(shoe.toGear()).isEqualTo(expected)
    }
}
