package paufregi.connectfeed.data.api.garmin.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.ActivityType as CoreActivityType

class ActivityTypeTest {

    @Test
    fun `To Core activity type - running`() {
        val activityType = ActivityType(id = 1, key = "running")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Running)
    }

    @Test
    fun `To Core activity type - cycling`() {
        val activityType = ActivityType(id = 2, key = "cycling")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Cycling)
    }

    @Test
    fun `To Core activity type - hiking`() {
        val activityType = ActivityType(id = 3, key = "hiking")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Hiking)
    }

    @Test
    fun `To Core activity type - other`() {
        val activityType = ActivityType(id = 4, key = "other")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Other)
    }

    @Test
    fun `To Core activity type - trail running`() {
        val activityType = ActivityType(id = 6, key = "trail_running")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.TrailRunning)
    }

    @Test
    fun `To Core activity type - walking`() {
        val activityType = ActivityType(id = 9, key = "walking")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Walking)
    }

    @Test
    fun `To Core activity type - road cycling`() {
        val activityType = ActivityType(id = 10, key = "cycling")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Cycling)
    }

    @Test
    fun `To Core activity type - cardio`() {
        val activityType = ActivityType(id = 11, key = "indoor_cardio")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Fitness)
    }

    @Test
    fun `To Core activity type - strength`() {
        val activityType = ActivityType(id = 13, key = "strength_training")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Strength)
    }

    @Test
    fun `To Core activity type - treadmill running`() {
        val activityType = ActivityType(id = 18, key = "treadmill_running")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.TreadmillRunning)
    }

    @Test
    fun `To Core activity type - indoor cycling`() {
        val activityType = ActivityType(id = 25, key = "indoor_cycling")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.IndoorCycling)
    }

    @Test
    fun `To Core activity type - swimming`() {
        val activityType = ActivityType(id = 26, key = "swimming")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Swimming)
    }

    @Test
    fun `To Core activity type - lap swimming`() {
        val activityType = ActivityType(id = 27, key = "lap_swimming")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Swimming)
    }

    @Test
    fun `To Core activity type - open water swimming`() {
        val activityType = ActivityType(id = 28, key = "open_water_swimming")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.OpenWaterSwimming)
    }

    @Test
    fun `To Core activity type - eBiking`() {
        val activityType = ActivityType(id = 176, key = "e_bike_fitness")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.EBiking)
    }

    @Test
    fun `To Core activity type - yoga`() {
        val activityType = ActivityType(id = 163, key = "yoga")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Yoga)
    }

    @Test
    fun `To Core activity type - breathwork`() {
        val activityType = ActivityType(id = 164, key = "breathwork")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Breathwork)
    }

    @Test
    fun `To Core activity type - meditation`() {
        val activityType = ActivityType(id = 202, key = "meditation")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Meditation)
    }

    @Test
    fun `To Core activity type - rugby`() {
        val activityType = ActivityType(id = 208, key = "rugby")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Rugby)
    }


    @Test
    fun `To Core activity type - soccer`() {
        val activityType = ActivityType(id = 215, key = "soccer")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Soccer)
    }

    @Test
    fun `To Core activity type - jump rope`() {
        val activityType = ActivityType(id = 254, key = "jump_rope")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.JumpRope)
    }

    @Test
    fun `To Core activity type - unknown`() {
        val activityType = ActivityType(id = 999, key = "unknown")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Unknown)
    }
}
