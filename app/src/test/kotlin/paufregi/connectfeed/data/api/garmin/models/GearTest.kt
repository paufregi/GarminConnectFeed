package paufregi.connectfeed.data.api.garmin.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.Gear as CoreGear
import paufregi.connectfeed.core.models.GearType as CoreGearType

class GearTest {

    @Test
    fun `To Core gear - name`() {
        val gear = Gear(
            id = "123",
            brand = "brand",
            model = "model",
            name = "name",
            type = "BIKE"
        )

        val coreGear = CoreGear(
            id = "123",
            name = "name",
            gearType = CoreGearType.Bike,
            distance = null
        )

        assertThat(gear.toCore()).isEqualTo(coreGear)
    }

    @Test
    fun `To Core gear - brand model`() {
        val gear = Gear(
            id = "123",
            brand = "brand",
            model = "model",
            name = null,
            type = "SHOE"
        )

        val coreGear = CoreGear(
            id = "123",
            name = "brand model",
            gearType = CoreGearType.Shoe,
            distance = null
        )

        assertThat(gear.toCore()).isEqualTo(coreGear)
    }

    @Test
    fun `To Core gear - unknown type`() {
        val gear = Gear(
            id = "123",
            brand = "brand",
            model = "model",
            name = "name",
            type = "UNKNOWN"
        )

        val coreGear = CoreGear(
            id = "123",
            name = "name",
            gearType = null,
            distance = null
        )

        assertThat(gear.toCore()).isEqualTo(coreGear)
    }
}
