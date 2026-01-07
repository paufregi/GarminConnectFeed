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
    fun `To Core activity type - track running`() {
        val activityType = ActivityType(id = 8, key = "track_running")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.TrackRunning)
    }

    @Test
    fun `To Core activity type - trail running`() {
        val activityType = ActivityType(id = 6, key = "trail_running")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.TrailRunning)
    }

    @Test
    fun `To Core activity type - treadmill running`() {
        val activityType = ActivityType(id = 18, key = "treadmill_running")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.TreadmillRunning)
    }

    @Test
    fun `To Core activity type - ultra run`() {
        val activityType = ActivityType(id = 181, key = "ultra_run")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.UltraRun)
    }

    @Test
    fun `To Core activity type - cycling`() {
        val activityType = ActivityType(id = 2, key = "cycling")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Cycling)
    }

    @Test
    fun `To Core activity type - downhill biking`() {
        val activityType = ActivityType(id = 20, key = "downhill_biking")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.DownhillBiking)
    }

    @Test
    fun `To Core activity type - eBiking`() {
        val activityType = ActivityType(id = 176, key = "e_bike_fitness")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.EBiking)
    }

    @Test
    fun `To Core activity type - eBiking mountain`() {
        val activityType = ActivityType(id = 175, key = "e_bike_mountain")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.EBikingMountain)
    }

    @Test
    fun `To Core activity type - gravel cycling`() {
        val activityType = ActivityType(id = 143, key = "gravel_cycling")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.GravelCycling)
    }

    @Test
    fun `To Core activity type - mountain biking`() {
        val activityType = ActivityType(id = 5, key = "mountain_biking")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.MountainBiking)
    }

    @Test
    fun `To Core activity type - road biking`() {
        val activityType = ActivityType(id = 10, key = "road_biking")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.RoadBiking)
    }

    @Test
    fun `To Core activity type - indoor ride`() {
        val activityType = ActivityType(id = 25, key = "indoor_cycling")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.IndoorRide)
    }

    @Test
    fun `To Core activity type - virtual ride`() {
        val activityType = ActivityType(id = 152, key = "virtual_ride")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.VirtualRide)
    }

    @Test
    fun `To Core activity type - HIIT`() {
        val activityType = ActivityType(id = 180, key = "hiit")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.HIIT)
    }

    @Test
    fun `To Core activity type - breathwork`() {
        val activityType = ActivityType(id = 164, key = "breathwork")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Breathwork)
    }

    @Test
    fun `To Core activity type - cardio`() {
        val activityType = ActivityType(id = 11, key = "cardio")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Cardio)
    }

    @Test
    fun `To Core activity type - jump rope`() {
        val activityType = ActivityType(id = 254, key = "jump_rope")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.JumpRope)
    }

    @Test
    fun `To Core activity type - strength training`() {
        val activityType = ActivityType(id = 13, key = "strength_training")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.StrengthTraining)
    }

    @Test
    fun `To Core activity type - yoga`() {
        val activityType = ActivityType(id = 163, key = "yoga")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Yoga)
    }

    @Test
    fun `To Core activity type - pool swimming`() {
        val activityType = ActivityType(id = 27, key = "pool_swimming")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.PoolSwimming)
    }

    @Test
    fun `To Core activity type - open water swimming`() {
        val activityType = ActivityType(id = 28, key = "open_water_swimming")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.OpenWaterSwimming)
    }

    @Test
    fun `To Core activity type - swimming`() {
        val activityType = ActivityType(id = 26, key = "swimming")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Swimming)
    }

    @Test
    fun `To Core activity type - walking`() {
        val activityType = ActivityType(id = 9, key = "walking")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Walking)
    }

    @Test
    fun `To Core activity type - hiking`() {
        val activityType = ActivityType(id = 3, key = "hiking")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Hiking)
    }

    @Test
    fun `To Core activity type - snowboarding`() {
        val activityType = ActivityType(id = 252, key = "snowboarding")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Snowboarding)
    }

    @Test
    fun `To Core activity type - kayaking`() {
        val activityType = ActivityType(id = 231, key = "kayaking")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Kayaking)
    }

    @Test
    fun `To Core activity type - stand up paddling`() {
        val activityType = ActivityType(id = 239, key = "stand_up_paddling")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.StandUpPaddling)
    }

    @Test
    fun `To Core activity type - surfing`() {
        val activityType = ActivityType(id = 240, key = "surfing")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Surfing)
    }

    @Test
    fun `To Core activity type - windsurf`() {
        val activityType = ActivityType(id = 242, key = "windsurf")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Windsurf)
    }

    @Test
    fun `To Core activity type - unknown`() {
        val activityType = ActivityType(id = 999, key = "unknown")

        assertThat(activityType.toCore()).isEqualTo(CoreActivityType.Unknown)
    }
}
