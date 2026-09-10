package paufregi.connectfeed.data.api.strava.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType

class BikeTest {

    @Test
    fun `toGear conversion`() {
        val bike = Bike(
            id = "bike-1",
            name = "Road bike",
            distance = 2000
        )

        val expected = Gear(
            id = "bike-1",
            name = "Road bike",
            type = GearType.Bike,
            distance = 2000,
        )

        assertThat(bike.toGear()).isEqualTo(expected)
    }

    @Test
    fun `toGear - no name`() {
        val bike = Bike(id = "bike-1", name = null, distance = 0)

        val expected = Gear(id = "bike-1", name = "", type = GearType.Bike, distance = 0)

        assertThat(bike.toGear()).isEqualTo(expected)
    }
}
