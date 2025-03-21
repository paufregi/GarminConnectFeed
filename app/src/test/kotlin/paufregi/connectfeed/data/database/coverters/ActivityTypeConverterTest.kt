package paufregi.connectfeed.data.database.coverters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.ActivityType

class ActivityTypeConverterTest {

    private val converter = ActivityTypeConverter()

    @Test
    fun `To Activity type - any`() {
        val name = converter.toName(ActivityType.Any)
        assertThat(name).isEqualTo(ActivityType.Any.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Any)
    }

    @Test
    fun `To Activity type - running`() {
        val name = converter.toName(ActivityType.Running)
        assertThat(name).isEqualTo(ActivityType.Running.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Running)
    }

    @Test
    fun `To Activity type - trail running`() {
        val name = converter.toName(ActivityType.TrailRunning)
        assertThat(name).isEqualTo(ActivityType.TrailRunning.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.TrailRunning)
    }

    @Test
    fun `To Activity type - treadmill running`() {
        val name = converter.toName(ActivityType.TreadmillRunning)
        assertThat(name).isEqualTo(ActivityType.TreadmillRunning.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.TreadmillRunning)
    }

    @Test
    fun `To Activity type - cycling`() {
        val name = converter.toName(ActivityType.Cycling)
        assertThat(name).isEqualTo(ActivityType.Cycling.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Cycling)
    }

    @Test
    fun `To Activity type - indoor cycling`() {
        val name = converter.toName(ActivityType.IndoorCycling)
        assertThat(name).isEqualTo(ActivityType.IndoorCycling.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.IndoorCycling)
    }

    @Test
    fun `To Activity type - eBiking`() {
        val name = converter.toName(ActivityType.EBiking)
        assertThat(name).isEqualTo(ActivityType.EBiking.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.EBiking)
    }

    @Test
    fun `To Activity type - swimming`() {
        val name = converter.toName(ActivityType.Swimming)
        assertThat(name).isEqualTo(ActivityType.Swimming.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Swimming)
    }

    @Test
    fun `To Activity type - open water swimming`() {
        val name = converter.toName(ActivityType.OpenWaterSwimming)
        assertThat(name).isEqualTo(ActivityType.OpenWaterSwimming.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.OpenWaterSwimming)
    }

    @Test
    fun `To Activity type - strength`() {
        val name = converter.toName(ActivityType.Strength)
        assertThat(name).isEqualTo(ActivityType.Strength.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Strength)
    }

    @Test
    fun `To Activity type - walking`() {
        val name = converter.toName(ActivityType.Walking)
        assertThat(name).isEqualTo(ActivityType.Walking.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Walking)
    }

    @Test
    fun `To Activity type - hiking`() {
        val name = converter.toName(ActivityType.Hiking)
        assertThat(name).isEqualTo(ActivityType.Hiking.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Hiking)
    }

    @Test
    fun `To Activity type - rugby`() {
        val name = converter.toName(ActivityType.Rugby)
        assertThat(name).isEqualTo(ActivityType.Rugby.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Rugby)
    }

    @Test
    fun `To Activity type - soccer`() {
        val name = converter.toName(ActivityType.Soccer)
        assertThat(name).isEqualTo(ActivityType.Soccer.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Soccer)
    }

    @Test
    fun `To Activity type - jump rope`() {
        val name = converter.toName(ActivityType.JumpRope)
        assertThat(name).isEqualTo(ActivityType.JumpRope.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.JumpRope)
    }

    @Test
    fun `To Activity type - breathwork`() {
        val name = converter.toName(ActivityType.Breathwork)
        assertThat(name).isEqualTo(ActivityType.Breathwork.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Breathwork)
    }

    @Test
    fun `To Activity type - meditation`() {
        val name = converter.toName(ActivityType.Meditation)
        assertThat(name).isEqualTo(ActivityType.Meditation.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Meditation)
    }

    @Test
    fun `To Activity type - yoga`() {
        val name = converter.toName(ActivityType.Yoga)
        assertThat(name).isEqualTo(ActivityType.Yoga.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Yoga)
    }

    @Test
    fun `To Activity type - other`() {
        val name = converter.toName(ActivityType.Other)
        assertThat(name).isEqualTo(ActivityType.Other.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Other)
    }

    @Test
    fun `To Activity type - unknown`() {
        val name = converter.toName(ActivityType.Unknown)
        assertThat(name).isEqualTo(ActivityType.Unknown.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Activity type - nope`() {
        val type = converter.fromName("nope")
        assertThat(type).isEqualTo(ActivityType.Unknown)
    }
}