package paufregi.connectfeed.data.database.coverters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.ActivityType

class ActivityTypeConverterTest {

    private val converter = ActivityTypeConverter()

    @Test
    fun `To Activity type - any`() {
        val result = converter.fromString("Any")

        assertThat(result).isEqualTo(ActivityType.Any)
    }

    @Test
    fun `To Activity type - running`() {
        val result = converter.fromString("Running")

        assertThat(result).isEqualTo(ActivityType.Running)
    }

    @Test
    fun `To Activity type - trail running`() {
        val result = converter.fromString("Trail Running")

        assertThat(result).isEqualTo(ActivityType.TrailRunning)
    }

    @Test
    fun `To Activity type - treadmill running`() {
        val result = converter.fromString("Treadmill Running")

        assertThat(result).isEqualTo(ActivityType.TreadmillRunning)
    }

    @Test
    fun `To Activity type - cycling`() {
        val result = converter.fromString("Cycling")

        assertThat(result).isEqualTo(ActivityType.Cycling)
    }

    @Test
    fun `To Activity type - indoor cycling`() {
        val result = converter.fromString("Indoor Cycling")

        assertThat(result).isEqualTo(ActivityType.IndoorCycling)
    }

    @Test
    fun `To Activity type - eBiking`() {
        val result = converter.fromString("eBiking")

        assertThat(result).isEqualTo(ActivityType.EBiking)
    }

    @Test
    fun `To Activity type - swimming`() {
        val result = converter.fromString("Swimming")

        assertThat(result).isEqualTo(ActivityType.Swimming)
    }

    @Test
    fun `To Activity type - open water swimming`() {
        val result = converter.fromString("Open Water Swimming")

        assertThat(result).isEqualTo(ActivityType.OpenWaterSwimming)
    }

    @Test
    fun `To Activity type - strength`() {
        val result = converter.fromString("Strength")

        assertThat(result).isEqualTo(ActivityType.Strength)
    }

    @Test
    fun `To Activity type - walking`() {
        val result = converter.fromString("Walking")

        assertThat(result).isEqualTo(ActivityType.Walking)
    }

    @Test
    fun `To Activity type - hiking`() {
        val result = converter.fromString("Hiking")

        assertThat(result).isEqualTo(ActivityType.Hiking)
    }

    @Test
    fun `To Activity type - rugby`() {
        val result = converter.fromString("Rugby")

        assertThat(result).isEqualTo(ActivityType.Rugby)
    }

    @Test
    fun `To Activity type - soccer`() {
        val result = converter.fromString("Soccer")

        assertThat(result).isEqualTo(ActivityType.Soccer)
    }

    @Test
    fun `To Activity type - jump rope`() {
        val result = converter.fromString("Jump Rope")

        assertThat(result).isEqualTo(ActivityType.JumpRope)
    }

    @Test
    fun `To Activity type - breathwork`() {
        val result = converter.fromString("Breathwork")

        assertThat(result).isEqualTo(ActivityType.Breathwork)
    }

    @Test
    fun `To Activity type - meditation`() {
        val result = converter.fromString("Meditation")

        assertThat(result).isEqualTo(ActivityType.Meditation)
    }

    @Test
    fun `To Activity type - yoga`() {
        val result = converter.fromString("Yoga")

        assertThat(result).isEqualTo(ActivityType.Yoga)
    }

    @Test
    fun `To Activity type - other`() {
        val result = converter.fromString("Other")

        assertThat(result).isEqualTo(ActivityType.Other)
    }

    @Test
    fun `To Activity type - unknown`() {
        val result = converter.fromString("nope")

        assertThat(result).isEqualTo(ActivityType.Unknown)
    }
}