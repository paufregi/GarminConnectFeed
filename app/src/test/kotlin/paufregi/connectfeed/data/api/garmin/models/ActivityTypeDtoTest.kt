package paufregi.connectfeed.data.api.garmin.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.ActivityType as CoreActivityType

class ActivityTypeDtoTest {

    @Test
    fun `To Core activity type - running`() {
        val activityTypeDto = ActivityTypeDto(id = 1, key = "running")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.Running)
    }

    @Test
    fun `To Core activity type - track running`() {
        val activityTypeDto = ActivityTypeDto(id = 8, key = "track_running")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.TrackRunning)
    }

    @Test
    fun `To Core activity type - trail running`() {
        val activityTypeDto = ActivityTypeDto(id = 6, key = "trail_running")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.TrailRunning)
    }

    @Test
    fun `To Core activity type - treadmill running`() {
        val activityTypeDto = ActivityTypeDto(id = 18, key = "treadmill_running")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.TreadmillRunning)
    }

    @Test
    fun `To Core activity type - ultra run`() {
        val activityTypeDto = ActivityTypeDto(id = 181, key = "ultra_run")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.UltraRun)
    }

    @Test
    fun `To Core activity type - cycling`() {
        val activityTypeDto = ActivityTypeDto(id = 2, key = "cycling")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.Cycling)
    }

    @Test
    fun `To Core activity type - downhill biking`() {
        val activityTypeDto = ActivityTypeDto(id = 20, key = "downhill_biking")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.DownhillBiking)
    }

    @Test
    fun `To Core activity type - eBiking`() {
        val activityTypeDto = ActivityTypeDto(id = 176, key = "e_bike_fitness")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.EBiking)
    }

    @Test
    fun `To Core activity type - eBiking mountain`() {
        val activityTypeDto = ActivityTypeDto(id = 175, key = "e_bike_mountain")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.EBikingMountain)
    }

    @Test
    fun `To Core activity type - gravel cycling`() {
        val activityTypeDto = ActivityTypeDto(id = 143, key = "gravel_cycling")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.GravelCycling)
    }

    @Test
    fun `To Core activity type - mountain biking`() {
        val activityTypeDto = ActivityTypeDto(id = 5, key = "mountain_biking")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.MountainBiking)
    }

    @Test
    fun `To Core activity type - road biking`() {
        val activityTypeDto = ActivityTypeDto(id = 10, key = "road_biking")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.RoadBiking)
    }

    @Test
    fun `To Core activity type - indoor ride`() {
        val activityTypeDto = ActivityTypeDto(id = 25, key = "indoor_cycling")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.IndoorRide)
    }

    @Test
    fun `To Core activity type - virtual ride`() {
        val activityTypeDto = ActivityTypeDto(id = 152, key = "virtual_ride")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.VirtualRide)
    }

    @Test
    fun `To Core activity type - HIIT`() {
        val activityTypeDto = ActivityTypeDto(id = 180, key = "hiit")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.HIIT)
    }

    @Test
    fun `To Core activity type - breathwork`() {
        val activityTypeDto = ActivityTypeDto(id = 164, key = "breathwork")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.Breathwork)
    }

    @Test
    fun `To Core activity type - cardio`() {
        val activityTypeDto = ActivityTypeDto(id = 11, key = "cardio")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.Cardio)
    }

    @Test
    fun `To Core activity type - jump rope`() {
        val activityTypeDto = ActivityTypeDto(id = 254, key = "jump_rope")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.JumpRope)
    }

    @Test
    fun `To Core activity type - strength training`() {
        val activityTypeDto = ActivityTypeDto(id = 13, key = "strength_training")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.StrengthTraining)
    }

    @Test
    fun `To Core activity type - yoga`() {
        val activityTypeDto = ActivityTypeDto(id = 163, key = "yoga")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.Yoga)
    }

    @Test
    fun `To Core activity type - pool swimming`() {
        val activityTypeDto = ActivityTypeDto(id = 27, key = "pool_swimming")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.PoolSwimming)
    }

    @Test
    fun `To Core activity type - open water swimming`() {
        val activityTypeDto = ActivityTypeDto(id = 28, key = "open_water_swimming")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.OpenWaterSwimming)
    }

    @Test
    fun `To Core activity type - swimming`() {
        val activityTypeDto = ActivityTypeDto(id = 26, key = "swimming")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.Swimming)
    }

    @Test
    fun `To Core activity type - walking`() {
        val activityTypeDto = ActivityTypeDto(id = 9, key = "walking")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.Walking)
    }

    @Test
    fun `To Core activity type - hiking`() {
        val activityTypeDto = ActivityTypeDto(id = 3, key = "hiking")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.Hiking)
    }

    @Test
    fun `To Core activity type - multisport`() {
        val activityTypeDto = ActivityTypeDto(id = 89, key = "multi_sport")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.Multisport)
    }

    @Test
    fun `To Core activity type - snowboarding`() {
        val activityTypeDto = ActivityTypeDto(id = 252, key = "snowboarding")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.Snowboarding)
    }

    @Test
    fun `To Core activity type - kayaking`() {
        val activityTypeDto = ActivityTypeDto(id = 231, key = "kayaking")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.Kayaking)
    }

    @Test
    fun `To Core activity type - stand up paddling`() {
        val activityTypeDto = ActivityTypeDto(id = 239, key = "stand_up_paddling")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.StandUpPaddling)
    }

    @Test
    fun `To Core activity type - surfing`() {
        val activityTypeDto = ActivityTypeDto(id = 240, key = "surfing")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.Surfing)
    }

    @Test
    fun `To Core activity type - windsurf`() {
        val activityTypeDto = ActivityTypeDto(id = 242, key = "windsurf")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.Windsurf)
    }

    @Test
    fun `To Core activity type - unknown`() {
        val activityTypeDto = ActivityTypeDto(id = 999, key = "unknown")

        assertThat(activityTypeDto.toCore()).isEqualTo(CoreActivityType.Unknown)
    }
}
