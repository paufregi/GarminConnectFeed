package paufregi.connectfeed.data.api.garmin.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.Gear as CoreGear
import paufregi.connectfeed.core.models.GearType as CoreGearType

class GearDtoTest {

    @Test
    fun `To Core gear - name`() {
        val gearDto = GearDto(
            id = "123",
            brand = "brand",
            model = "model",
            name = "name",
            type = "BIKE",
            distance = 12345.6
        )

        val coreGear = CoreGear(
            id = "123",
            name = "name",
            type = CoreGearType.Bike,
            distance = 12346
        )

        assertThat(gearDto.toCore()).isEqualTo(coreGear)
    }

    @Test
    fun `To Core gear - brand model`() {
        val gearDto = GearDto(
            id = "123",
            brand = "brand",
            model = "model",
            name = null,
            type = "SHOES",
            distance = 1000.0
        )

        val coreGear = CoreGear(
            id = "123",
            name = "brand model",
            type = CoreGearType.Shoe,
            distance = 1000
        )

        assertThat(gearDto.toCore()).isEqualTo(coreGear)
    }

    @Test
    fun `To Core gear - unknown type`() {
        val gearDto = GearDto(
            id = "123",
            brand = "brand",
            model = "model",
            name = "name",
            type = "UNKNOWN",
            distance = null
        )

        val coreGear = CoreGear(
            id = "123",
            name = "name",
            type = CoreGearType.Unknown,
            distance = null
        )

        assertThat(gearDto.toCore()).isEqualTo(coreGear)
    }
}
