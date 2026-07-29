package paufregi.connectfeed.data.database.coverters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.GearType

class GearTypeConverterTest {

    private val converter = GearTypeConverter()

    @Test
    fun `To Gear type - shoe`() {
        val name = converter.toName(GearType.Shoe)
        assertThat(name).isEqualTo(GearType.Shoe.name)

        val gear = converter.fromName(name)
        assertThat(gear).isEqualTo(GearType.Shoe)
    }

    @Test
    fun `To Gear type - bike`() {
        val name = converter.toName(GearType.Bike)
        assertThat(name).isEqualTo(GearType.Bike.name)

        val gear = converter.fromName(name)
        assertThat(gear).isEqualTo(GearType.Bike)
    }

    @Test
    fun `To Gear type - unknown`() {
        val name = converter.toName(GearType.Unknown)
        assertThat(name).isEqualTo(GearType.Unknown.name)

        val gear = converter.fromName(name)
        assertThat(gear).isEqualTo(GearType.Unknown)
    }

    @Test
    fun `To Gear type - null`() {
        val gear = converter.fromName(null)
        assertThat(gear).isEqualTo(GearType.Unknown)
    }

    @Test
    fun `To Gear type - invalid`() {
        val gear = converter.fromName("NOPE")
        assertThat(gear).isEqualTo(GearType.Unknown)
    }
}