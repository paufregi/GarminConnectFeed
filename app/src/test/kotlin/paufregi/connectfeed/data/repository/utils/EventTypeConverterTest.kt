package paufregi.connectfeed.data.repository.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.data.api.garmin.models.EventType as GarminEventType

class EventTypeConverterTest {

	@Test
	fun `garmin converts Race`() {
		val type = GarminEventType(1L, "race")
		assertThat(EventTypeConverter.garmin(type)).isEqualTo(EventType.Race)
	}

	@Test
	fun `garmin converts Recreation`() {
		val type = GarminEventType(2L, "recreation")
		assertThat(EventTypeConverter.garmin(type)).isEqualTo(EventType.Recreation)
	}

	@Test
	fun `garmin converts SpecialEvent`() {
		val type = GarminEventType(3L, "special_event")
		assertThat(EventTypeConverter.garmin(type)).isEqualTo(EventType.SpecialEvent)
	}

	@Test
	fun `garmin converts Training`() {
		val type = GarminEventType(4L, "training")
		assertThat(EventTypeConverter.garmin(type)).isEqualTo(EventType.Training)
	}

	@Test
	fun `garmin converts Transportation`() {
		val type = GarminEventType(5L, "transportation")
		assertThat(EventTypeConverter.garmin(type)).isEqualTo(EventType.Transportation)
	}

	@Test
	fun `garmin converts Touring`() {
		val type = GarminEventType(6L, "touring")
		assertThat(EventTypeConverter.garmin(type)).isEqualTo(EventType.Touring)
	}

	@Test
	fun `garmin converts Geocaching`() {
		val type = GarminEventType(7L, "geocaching")
		assertThat(EventTypeConverter.garmin(type)).isEqualTo(EventType.Geocaching)
	}

	@Test
	fun `garmin converts Fitness`() {
		val type = GarminEventType(8L, "fitness")
		assertThat(EventTypeConverter.garmin(type)).isEqualTo(EventType.Fitness)
	}

	@Test
	fun `garmin converts Uncategorized`() {
		val type = GarminEventType(9L, "uncategorized")
		assertThat(EventTypeConverter.garmin(type)).isEqualTo(EventType.Uncategorized)
	}

	@Test
	fun `garmin returns Uncategorized for unsupported type id`() {
		val type = GarminEventType(-1L, "unknown")
		assertThat(EventTypeConverter.garmin(type)).isEqualTo(EventType.Uncategorized)
	}
}

