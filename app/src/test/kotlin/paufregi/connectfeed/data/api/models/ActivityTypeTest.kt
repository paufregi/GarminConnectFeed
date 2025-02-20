package paufregi.connectfeed.data.api.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.ActivityType as CoreActivityType

class ActivityTypeTest {

    @Test
    fun `To Core activity - running`() {
        val activityType = ActivityType(id = 1, key = "running")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Running)
    }

    @Test
    fun `To Core activity - cycling`() {
        val activityType = ActivityType(id = 2, key = "cycling")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Cycling)
    }

    @Test
    fun `To Core activity - hiking`() {
        val activityType = ActivityType(id = 3, key = "hiking")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Hiking)
    }

    @Test
    fun `To Core activity - other`() {
        val activityType = ActivityType(id = 4, key = "other")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Other)
    }

    @Test
    fun `To Core activity - trail running`() {
        val activityType = ActivityType(id = 6, key = "trail_running")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.TrailRunning)
    }

    @Test
    fun `To Core activity - walking`() {
        val activityType = ActivityType(id = 9, key = "walking")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Walking)
    }

    @Test
    fun `To Core activity - road cycling`() {
        val activityType = ActivityType(id = 10, key = "cycling")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Cycling)
    }

    @Test
    fun `To Core activity - strength`() {
        val activityType = ActivityType(id = 13, key = "strength_training")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Strength)
    }

    @Test
    fun `To Core activity - treadmill running`() {
        val activityType = ActivityType(id = 18, key = "treadmill_running")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.TreadmillRunning)
    }

    @Test
    fun `To Core activity - indoor cycling`() {
        val activityType = ActivityType(id = 25, key = "indoor_cycling")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.IndoorCycling)
    }

    @Test
    fun `To Core activity - swimming`() {
        val activityType = ActivityType(id = 26, key = "swimming")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Swimming)
    }

    @Test
    fun `To Core activity - lap swimming`() {
        val activityType = ActivityType(id = 27, key = "lap_swimming")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Swimming)
    }

    @Test
    fun `To Core activity - open water swimming`() {
        val activityType = ActivityType(id = 28, key = "open_water_swimming")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.OpenWaterSwimming)
    }

    @Test
    fun `To Core activity - eBiking`() {
        val activityType = ActivityType(id = 176, key = "e_bike_fitness")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.EBiking)
    }

    @Test
    fun `To Core activity - yoga`() {
        val activityType = ActivityType(id = 163, key = "yoga")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Yoga)
    }

    @Test
    fun `To Core activity - breathwork`() {
        val activityType = ActivityType(id = 164, key = "breathwork")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Breathwork)
    }

    @Test
    fun `To Core activity - meditation`() {
        val activityType = ActivityType(id = 202, key = "meditation")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Meditation)
    }

    @Test
    fun `To Core activity - rugby`() {
        val activityType = ActivityType(id = 208, key = "rugby")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Rugby)
    }


    @Test
    fun `To Core activity - soccer`() {
        val activityType = ActivityType(id = 215, key = "soccer")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Soccer)
    }

    @Test
    fun `To Core activity - jump rope`() {
        val activityType = ActivityType(id = 254, key = "jump_rope")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.JumpRope)
    }

    @Test
    fun `To Core activity - unknown`() {
        val activityType = ActivityType(id = 999, key = "unknown")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Unknown)
    }
}
