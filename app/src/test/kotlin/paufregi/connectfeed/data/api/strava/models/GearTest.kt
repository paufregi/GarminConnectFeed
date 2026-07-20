package paufregi.connectfeed.data.api.strava.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.Gear as CoreGear
import paufregi.connectfeed.core.models.GearType as CoreGearType

class GearTest {

    @Test
    fun `To Core gear`() {
        val gear = Gear(
            id = "123",
            name = "name",
        )

        val coreGear = CoreGear(
            id = "123",
            name = "name",
            type = CoreGearType.Bike,
        )

        assertThat(gear.toCore(CoreGearType.Bike)).isEqualTo(coreGear)
    }
}
