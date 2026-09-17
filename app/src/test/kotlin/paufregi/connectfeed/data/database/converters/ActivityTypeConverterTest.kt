package paufregi.connectfeed.data.database.converters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.ActivityType

class ActivityTypeConverterTest {
    private val converter = ActivityTypeConverter()

    @Test
    fun `To Activity type - Any`() {
        val name = converter.toName(ActivityType.Any)
        assertThat(name).isEqualTo(ActivityType.Any::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Any)
    }

    @Test
    fun `To Activity type - Running`() {
        val name = converter.toName(ActivityType.Running)
        assertThat(name).isEqualTo(ActivityType.Running::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Running)
    }

    @Test
    fun `To Activity type - Cycling`() {
        val name = converter.toName(ActivityType.Cycling)
        assertThat(name).isEqualTo(ActivityType.Cycling::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Cycling)
    }

    @Test
    fun `To Activity type - Swimming`() {
        val name = converter.toName(ActivityType.Swimming)
        assertThat(name).isEqualTo(ActivityType.Swimming::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Swimming)
    }

    @Test
    fun `To Activity type - StrengthTraining`() {
        val name = converter.toName(ActivityType.StrengthTraining)
        assertThat(name).isEqualTo(ActivityType.StrengthTraining::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StrengthTraining)
    }

    @Test
    fun `To Activity type - Fitness`() {
        val name = converter.toName(ActivityType.Fitness)
        assertThat(name).isEqualTo(ActivityType.Fitness::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Fitness)
    }

    @Test
    fun `To Activity type - Other`() {
        val name = converter.toName(ActivityType.Other)
        assertThat(name).isEqualTo(ActivityType.Other::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Other)
    }

    @Test
    fun `To Activity type - Unknown`() {
        val name = converter.toName(ActivityType.Unknown)
        assertThat(name).isEqualTo(ActivityType.Unknown::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Activity type - invalid name returns unknown`() {
        val type = converter.fromName("nope")
        assertThat(type).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Activity type - TrackRunning`() {
        val name = converter.toName(ActivityType.TrackRunning)
        assertThat(name).isEqualTo(ActivityType.TrackRunning::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.TrackRunning)
    }

    @Test
    fun `To Activity type - TrailRunning`() {
        val name = converter.toName(ActivityType.TrailRunning)
        assertThat(name).isEqualTo(ActivityType.TrailRunning::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.TrailRunning)
    }

    @Test
    fun `To Activity type - TreadmillRunning`() {
        val name = converter.toName(ActivityType.TreadmillRunning)
        assertThat(name).isEqualTo(ActivityType.TreadmillRunning::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.TreadmillRunning)
    }

    @Test
    fun `To Activity type - UltraRun`() {
        val name = converter.toName(ActivityType.UltraRun)
        assertThat(name).isEqualTo(ActivityType.UltraRun::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.UltraRun)
    }

    @Test
    fun `To Activity type - DownhillBiking`() {
        val name = converter.toName(ActivityType.DownhillBiking)
        assertThat(name).isEqualTo(ActivityType.DownhillBiking::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.DownhillBiking)
    }

    @Test
    fun `To Activity type - EBiking`() {
        val name = converter.toName(ActivityType.EBiking)
        assertThat(name).isEqualTo(ActivityType.EBiking::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.EBiking)
    }

    @Test
    fun `To Activity type - EBikingMountain`() {
        val name = converter.toName(ActivityType.EBikingMountain)
        assertThat(name).isEqualTo(ActivityType.EBikingMountain::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.EBikingMountain)
    }

    @Test
    fun `To Activity type - GravelCycling`() {
        val name = converter.toName(ActivityType.GravelCycling)
        assertThat(name).isEqualTo(ActivityType.GravelCycling::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.GravelCycling)
    }

    @Test
    fun `To Activity type - MountainBiking`() {
        val name = converter.toName(ActivityType.MountainBiking)
        assertThat(name).isEqualTo(ActivityType.MountainBiking::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.MountainBiking)
    }

    @Test
    fun `To Activity type - RoadBiking`() {
        val name = converter.toName(ActivityType.RoadBiking)
        assertThat(name).isEqualTo(ActivityType.RoadBiking::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.RoadBiking)
    }

    @Test
    fun `To Activity type - VirtualRide`() {
        val name = converter.toName(ActivityType.VirtualRide)
        assertThat(name).isEqualTo(ActivityType.VirtualRide::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.VirtualRide)
    }

    @Test
    fun `To Activity type - HIIT`() {
        val name = converter.toName(ActivityType.HIIT)
        assertThat(name).isEqualTo(ActivityType.HIIT::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.HIIT)
    }

    @Test
    fun `To Activity type - Breathwork`() {
        val name = converter.toName(ActivityType.Breathwork)
        assertThat(name).isEqualTo(ActivityType.Breathwork::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Breathwork)
    }

    @Test
    fun `To Activity type - Cardio`() {
        val name = converter.toName(ActivityType.Cardio)
        assertThat(name).isEqualTo(ActivityType.Cardio::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Cardio)
    }

    @Test
    fun `To Activity type - JumpRope`() {
        val name = converter.toName(ActivityType.JumpRope)
        assertThat(name).isEqualTo(ActivityType.JumpRope::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.JumpRope)
    }

    @Test
    fun `To Activity type - Yoga`() {
        val name = converter.toName(ActivityType.Yoga)
        assertThat(name).isEqualTo(ActivityType.Yoga::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Yoga)
    }

    @Test
    fun `To Activity type - PoolSwimming`() {
        val name = converter.toName(ActivityType.PoolSwimming)
        assertThat(name).isEqualTo(ActivityType.PoolSwimming::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.PoolSwimming)
    }
    @Test
    fun `To Activity type - OpenWaterSwimming`() {
        val name = converter.toName(ActivityType.OpenWaterSwimming)
        assertThat(name).isEqualTo(ActivityType.OpenWaterSwimming::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.OpenWaterSwimming)
    }

    @Test
    fun `To Activity type - Walking`() {
        val name = converter.toName(ActivityType.Walking)
        assertThat(name).isEqualTo(ActivityType.Walking::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Walking)
    }

    @Test
    fun `To Activity type - Hiking`() {
        val name = converter.toName(ActivityType.Hiking)
        assertThat(name).isEqualTo(ActivityType.Hiking::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Hiking)
    }

    @Test
    fun `To Activity type - Snowboarding`() {
        val name = converter.toName(ActivityType.Snowboarding)
        assertThat(name).isEqualTo(ActivityType.Snowboarding::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Snowboarding)
    }

    @Test
    fun `To Activity type - Kayaking`() {
        val name = converter.toName(ActivityType.Kayaking)
        assertThat(name).isEqualTo(ActivityType.Kayaking::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Kayaking)
    }

    @Test
    fun `To Activity type - StandUpPaddling`() {
        val name = converter.toName(ActivityType.StandUpPaddling)
        assertThat(name).isEqualTo(ActivityType.StandUpPaddling::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StandUpPaddling)
    }

    @Test
    fun `To Activity type - Surfing`() {
        val name = converter.toName(ActivityType.Surfing)
        assertThat(name).isEqualTo(ActivityType.Surfing::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Surfing)
    }

    @Test
    fun `To Activity type - Windsurf`() {
        val name = converter.toName(ActivityType.Windsurf)
        assertThat(name).isEqualTo(ActivityType.Windsurf::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Windsurf)
    }
}