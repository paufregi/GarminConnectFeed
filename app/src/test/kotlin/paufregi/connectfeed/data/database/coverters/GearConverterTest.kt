package paufregi.connectfeed.data.database.coverters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
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
    fun `Entity gear to gear`() {
        val result = entityGear.toCore()

        assertThat(result).isEqualTo(gear)
    }
}