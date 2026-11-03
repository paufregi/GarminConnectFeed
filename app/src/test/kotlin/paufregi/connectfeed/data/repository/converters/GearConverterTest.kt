package paufregi.connectfeed.data.repository.converters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.data.api.garmin.models.GearDto
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.data.database.entities.GearEntity
import paufregi.connectfeed.user

class GearConverterTest {

    val gear = Gear(
        id = "ID_1",
        name = "gear",
        type = GearType.Shoe,
        distance = 1,
    )

    val entityGear = GearEntity(
        id = "ID_1",
        userId = user.id,
        name = "gear",
        type = GearType.Shoe,
        distance = 1,
    )

    @Test
    fun `Gear to entity`() {
        val result = gear.toEntity(user.id)

        assertThat(result).isEqualTo(entityGear)
    }

    @Test
    fun `Entity to gear`() {
        val result = entityGear.toCore()

        assertThat(result).isEqualTo(gear)
    }

    @Test
    fun `DTO to core - name`() {
        val dto = GearDto(
            id = "123",
            brand = "brand",
            model = "model",
            name = "name",
            type = "BIKE",
            distance = 12345.6
        )

        val coreGear = Gear(
            id = "123",
            name = "name",
            type = GearType.Bike,
            distance = 12346
        )

        assertThat(dto.toCore()).isEqualTo(coreGear)
    }

    @Test
    fun `DTO to core - brand model`() {
        val dto = GearDto(
            id = "123",
            brand = "brand",
            model = "model",
            name = null,
            type = "SHOES",
            distance = 1000.0
        )

        val coreGear = Gear(
            id = "123",
            name = "brand model",
            type = GearType.Shoe,
            distance = 1000
        )

        assertThat(dto.toCore()).isEqualTo(coreGear)
    }

    @Test
    fun `DTO to core - unknown type`() {
        val dto = GearDto(
            id = "123",
            brand = "brand",
            model = "model",
            name = "name",
            type = "UNKNOWN",
            distance = null
        )

        val coreGear = Gear(
            id = "123",
            name = "name",
            type = GearType.Unknown,
            distance = null
        )

        assertThat(dto.toCore()).isEqualTo(coreGear)
    }
}
