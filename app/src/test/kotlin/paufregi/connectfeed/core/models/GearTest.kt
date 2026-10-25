package paufregi.connectfeed.core.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.LocalDateTime

class GearTest {

    private val bike = Gear("bike-1", "Road bike", GearType.Bike)
    private val shoe = Gear("shoe-1", "Trainers", GearType.Shoe)
    private val activity = Activity(
        id = 1,
        name = "Morning run",
        type = ActivityType.Running,
        eventType = EventType.Training,
        date = LocalDateTime.MIN,
    )

    @Test
    fun `compatible - bike`() {
        assertThat(bike.compatible(activity.copy(type = ActivityType.Cycling))).isTrue()
        assertThat(bike.compatible(activity.copy(type = ActivityType.RoadBiking))).isTrue()
        assertThat(bike.compatible(activity.copy(type = ActivityType.MountainBiking))).isTrue()
    }

    @Test
    fun `compatible - bike incompatible activity`() {
        assertThat(bike.compatible(activity)).isFalse()
        assertThat(bike.compatible(activity.copy(type = ActivityType.TrailRunning))).isFalse()
        assertThat(bike.compatible(activity.copy(type = ActivityType.Walking))).isFalse()
        assertThat(bike.compatible(activity.copy(type = ActivityType.Swimming))).isFalse()
    }

    @Test
    fun `compatible - shoe`() {
        assertThat(shoe.compatible(activity)).isTrue()
        assertThat(shoe.compatible(activity.copy(type = ActivityType.TrailRunning))).isTrue()
        assertThat(shoe.compatible(activity.copy(type = ActivityType.Walking))).isTrue()
        assertThat(shoe.compatible(activity.copy(type = ActivityType.Hiking))).isTrue()
    }

    @Test
    fun `compatible - shoe incompatible activity`() {
        assertThat(shoe.compatible(activity.copy(type = ActivityType.Cycling))).isFalse()
        assertThat(shoe.compatible(activity.copy(type = ActivityType.RoadBiking))).isFalse()
        assertThat(shoe.compatible(activity.copy(type = ActivityType.Swimming))).isFalse()
        assertThat(shoe.compatible(activity.copy(type = ActivityType.Other))).isFalse()
    }

    @Test
    fun `compatible - unknown gear`() {
        val gear = Gear("gear-1", "Unknown gear")

        assertThat(gear.compatible(activity)).isTrue()
        assertThat(gear.compatible(activity.copy(type = ActivityType.Cycling))).isTrue()
        assertThat(gear.compatible(activity.copy(type = ActivityType.Swimming))).isTrue()
        assertThat(gear.compatible(activity.copy(type = ActivityType.Unknown))).isTrue()
    }

    @Test
    fun `compatible - wildcard activity types`() {
        assertThat(bike.compatible(activity.copy(type = ActivityType.Any))).isTrue()
        assertThat(bike.compatible(activity.copy(type = ActivityType.Unknown))).isTrue()
        assertThat(shoe.compatible(activity.copy(type = ActivityType.Any))).isTrue()
        assertThat(shoe.compatible(activity.copy(type = ActivityType.Unknown))).isTrue()
    }
}
