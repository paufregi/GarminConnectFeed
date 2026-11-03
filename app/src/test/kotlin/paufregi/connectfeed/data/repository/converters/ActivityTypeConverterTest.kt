package paufregi.connectfeed.data.repository.converters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.data.api.garmin.models.ActivityTypeDto

class ActivityTypeConverterTest {

    @Test
    fun `DTO to core - running`() {
        val dto = ActivityTypeDto(id = 1, key = "running")

        assertThat(dto.toCore()).isEqualTo(ActivityType.Running)
    }

    @Test
    fun `DTO to core - track running`() {
        val dto = ActivityTypeDto(id = 8, key = "track_running")

        assertThat(dto.toCore()).isEqualTo(ActivityType.TrackRunning)
    }

    @Test
    fun `DTO to core - trail running`() {
        val dto = ActivityTypeDto(id = 6, key = "trail_running")

        assertThat(dto.toCore()).isEqualTo(ActivityType.TrailRunning)
    }

    @Test
    fun `DTO to core - treadmill running`() {
        val dto = ActivityTypeDto(id = 18, key = "treadmill_running")

        assertThat(dto.toCore()).isEqualTo(ActivityType.TreadmillRunning)
    }

    @Test
    fun `DTO to core - ultra run`() {
        val dto = ActivityTypeDto(id = 181, key = "ultra_run")

        assertThat(dto.toCore()).isEqualTo(ActivityType.UltraRun)
    }

    @Test
    fun `DTO to core - cycling`() {
        val dto = ActivityTypeDto(id = 2, key = "cycling")

        assertThat(dto.toCore()).isEqualTo(ActivityType.Cycling)
    }

    @Test
    fun `DTO to core - downhill biking`() {
        val dto = ActivityTypeDto(id = 20, key = "downhill_biking")

        assertThat(dto.toCore()).isEqualTo(ActivityType.DownhillBiking)
    }

    @Test
    fun `DTO to core - eBiking`() {
        val dto = ActivityTypeDto(id = 176, key = "e_bike_fitness")

        assertThat(dto.toCore()).isEqualTo(ActivityType.EBiking)
    }

    @Test
    fun `DTO to core - eBiking mountain`() {
        val dto = ActivityTypeDto(id = 175, key = "e_bike_mountain")

        assertThat(dto.toCore()).isEqualTo(ActivityType.EBikingMountain)
    }

    @Test
    fun `DTO to core - gravel cycling`() {
        val dto = ActivityTypeDto(id = 143, key = "gravel_cycling")

        assertThat(dto.toCore()).isEqualTo(ActivityType.GravelCycling)
    }

    @Test
    fun `DTO to core - mountain biking`() {
        val dto = ActivityTypeDto(id = 5, key = "mountain_biking")

        assertThat(dto.toCore()).isEqualTo(ActivityType.MountainBiking)
    }

    @Test
    fun `DTO to core - road biking`() {
        val dto = ActivityTypeDto(id = 10, key = "road_biking")

        assertThat(dto.toCore()).isEqualTo(ActivityType.RoadBiking)
    }

    @Test
    fun `DTO to core - indoor ride`() {
        val dto = ActivityTypeDto(id = 25, key = "indoor_cycling")

        assertThat(dto.toCore()).isEqualTo(ActivityType.IndoorRide)
    }

    @Test
    fun `DTO to core - virtual ride`() {
        val dto = ActivityTypeDto(id = 152, key = "virtual_ride")

        assertThat(dto.toCore()).isEqualTo(ActivityType.VirtualRide)
    }

    @Test
    fun `DTO to core - HIIT`() {
        val dto = ActivityTypeDto(id = 180, key = "hiit")

        assertThat(dto.toCore()).isEqualTo(ActivityType.HIIT)
    }

    @Test
    fun `DTO to core - breathwork`() {
        val dto = ActivityTypeDto(id = 164, key = "breathwork")

        assertThat(dto.toCore()).isEqualTo(ActivityType.Breathwork)
    }

    @Test
    fun `DTO to core - cardio`() {
        val dto = ActivityTypeDto(id = 11, key = "cardio")

        assertThat(dto.toCore()).isEqualTo(ActivityType.Cardio)
    }

    @Test
    fun `DTO to core - jump rope`() {
        val dto = ActivityTypeDto(id = 254, key = "jump_rope")

        assertThat(dto.toCore()).isEqualTo(ActivityType.JumpRope)
    }

    @Test
    fun `DTO to core - strength training`() {
        val dto = ActivityTypeDto(id = 13, key = "strength_training")

        assertThat(dto.toCore()).isEqualTo(ActivityType.StrengthTraining)
    }

    @Test
    fun `DTO to core - yoga`() {
        val dto = ActivityTypeDto(id = 163, key = "yoga")

        assertThat(dto.toCore()).isEqualTo(ActivityType.Yoga)
    }

    @Test
    fun `DTO to core - pool swimming`() {
        val dto = ActivityTypeDto(id = 27, key = "pool_swimming")

        assertThat(dto.toCore()).isEqualTo(ActivityType.PoolSwimming)
    }

    @Test
    fun `DTO to core - open water swimming`() {
        val dto = ActivityTypeDto(id = 28, key = "open_water_swimming")

        assertThat(dto.toCore()).isEqualTo(ActivityType.OpenWaterSwimming)
    }

    @Test
    fun `DTO to core - swimming`() {
        val dto = ActivityTypeDto(id = 26, key = "swimming")

        assertThat(dto.toCore()).isEqualTo(ActivityType.Swimming)
    }

    @Test
    fun `DTO to core - walking`() {
        val dto = ActivityTypeDto(id = 9, key = "walking")

        assertThat(dto.toCore()).isEqualTo(ActivityType.Walking)
    }

    @Test
    fun `DTO to core - hiking`() {
        val dto = ActivityTypeDto(id = 3, key = "hiking")

        assertThat(dto.toCore()).isEqualTo(ActivityType.Hiking)
    }

    @Test
    fun `DTO to core - multisport`() {
        val dto = ActivityTypeDto(id = 89, key = "multi_sport")

        assertThat(dto.toCore()).isEqualTo(ActivityType.Multisport)
    }

    @Test
    fun `DTO to core - snowboarding`() {
        val dto = ActivityTypeDto(id = 252, key = "snowboarding")

        assertThat(dto.toCore()).isEqualTo(ActivityType.Snowboarding)
    }

    @Test
    fun `DTO to core - kayaking`() {
        val dto = ActivityTypeDto(id = 231, key = "kayaking")

        assertThat(dto.toCore()).isEqualTo(ActivityType.Kayaking)
    }

    @Test
    fun `DTO to core - stand up paddling`() {
        val dto = ActivityTypeDto(id = 239, key = "stand_up_paddling")

        assertThat(dto.toCore()).isEqualTo(ActivityType.StandUpPaddling)
    }

    @Test
    fun `DTO to core - surfing`() {
        val dto = ActivityTypeDto(id = 240, key = "surfing")

        assertThat(dto.toCore()).isEqualTo(ActivityType.Surfing)
    }

    @Test
    fun `DTO to core - windsurf`() {
        val dto = ActivityTypeDto(id = 242, key = "windsurf")

        assertThat(dto.toCore()).isEqualTo(ActivityType.Windsurf)
    }

    @Test
    fun `DTO to core - unknown`() {
        val dto = ActivityTypeDto(id = 999, key = "unknown")

        assertThat(dto.toCore()).isEqualTo(ActivityType.Unknown)
    }
}
