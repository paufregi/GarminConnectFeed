package paufregi.connectfeed.core.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class GearTypeTest {
    @Test
    fun `compatible - bike`() {
        assertThat(GearType.Bike.compatible(ActivityType.Cycling)).isTrue()
        assertThat(GearType.Bike.compatible(ActivityType.RoadBiking)).isTrue()

        assertThat(GearType.Bike.compatible(ActivityType.Running)).isFalse()
        assertThat(GearType.Bike.compatible(ActivityType.Walking)).isFalse()
        assertThat(GearType.Shoe.compatible(ActivityType.Swimming)).isFalse()
    }

    @Test
    fun `compatible - shoe`() {
        assertThat(GearType.Shoe.compatible(ActivityType.Running)).isTrue()
        assertThat(GearType.Shoe.compatible(ActivityType.TrailRunning)).isTrue()
        assertThat(GearType.Shoe.compatible(ActivityType.Walking)).isTrue()
        assertThat(GearType.Shoe.compatible(ActivityType.Hiking)).isTrue()

        assertThat(GearType.Shoe.compatible(ActivityType.Cycling)).isFalse()
        assertThat(GearType.Shoe.compatible(ActivityType.Swimming)).isFalse()
    }

    @Test
    fun `compatible - unknown gear`() {
        assertThat(GearType.Unknown.compatible(ActivityType.Cycling)).isTrue()
        assertThat(GearType.Unknown.compatible(ActivityType.Running)).isTrue()
        assertThat(GearType.Unknown.compatible(ActivityType.Swimming)).isTrue()
        assertThat(GearType.Unknown.compatible(ActivityType.Unknown)).isTrue()
    }

    @Test
    fun `compatible - wildcard activity types`() {
        assertThat(GearType.Bike.compatible(ActivityType.Any)).isTrue()
        assertThat(GearType.Bike.compatible(ActivityType.Unknown)).isTrue()
        assertThat(GearType.Shoe.compatible(ActivityType.Any)).isTrue()
        assertThat(GearType.Shoe.compatible(ActivityType.Unknown)).isTrue()
    }
}

