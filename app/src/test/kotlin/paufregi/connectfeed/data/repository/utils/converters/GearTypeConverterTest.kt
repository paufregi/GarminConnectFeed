package paufregi.connectfeed.data.repository.utils.converters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.data.repository.utils.GearTypeConverter

class GearTypeConverterTest {

	@Test
	fun `garmin converts Shoe`() {
		assertThat(GearTypeConverter.garmin("SHOE")).isEqualTo(GearType.Shoe)
	}

	@Test
	fun `garmin converts Bike`() {
		assertThat(GearTypeConverter.garmin("BIKE")).isEqualTo(GearType.Bike)
	}

	@Test
	fun `garmin converts Unknown`() {
		assertThat(GearTypeConverter.garmin("UNKNOWN")).isEqualTo(GearType.Unknown)
	}
}

