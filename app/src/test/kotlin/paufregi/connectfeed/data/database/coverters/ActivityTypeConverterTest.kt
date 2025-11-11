package paufregi.connectfeed.data.database.coverters

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

    @Test
    fun `To Activity type - StravaRunning`() {
        val name = converter.toName(ActivityType.StravaRunning)
        assertThat(name).isEqualTo(ActivityType.StravaRunning::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaRunning)
    }

    @Test
    fun `To Activity type - StravaTrailRun`() {
        val name = converter.toName(ActivityType.StravaTrailRun)
        assertThat(name).isEqualTo(ActivityType.StravaTrailRun::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaTrailRun)
    }

    @Test
    fun `To Activity type - StravaRide`() {
        val name = converter.toName(ActivityType.StravaRide)
        assertThat(name).isEqualTo(ActivityType.StravaRide::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaRide)
    }
    @Test
    fun `To Activity type - StravaMountainBikeRide`() {
        val name = converter.toName(ActivityType.StravaMountainBikeRide)
        assertThat(name).isEqualTo(ActivityType.StravaMountainBikeRide::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaMountainBikeRide)
    }

    @Test
    fun `To Activity type - StravaGravelRide`() {
        val name = converter.toName(ActivityType.StravaGravelRide)
        assertThat(name).isEqualTo(ActivityType.StravaGravelRide::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaGravelRide)
    }

    @Test
    fun `To Activity type - StravaEBikeRide`() {
        val name = converter.toName(ActivityType.StravaEBikeRide)
        assertThat(name).isEqualTo(ActivityType.StravaEBikeRide::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaEBikeRide)
    }

    @Test
    fun `To Activity type - StravaEMountainBikeRide`() {
        val name = converter.toName(ActivityType.StravaEMountainBikeRide)
        assertThat(name).isEqualTo(ActivityType.StravaEMountainBikeRide::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaEMountainBikeRide)
    }

    @Test
    fun `To Activity type - StravaVirtualRide`() {
        val name = converter.toName(ActivityType.StravaVirtualRide)
        assertThat(name).isEqualTo(ActivityType.StravaVirtualRide::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaVirtualRide)
    }

    @Test
    fun `To Activity type - StravaHIIT`() {
        val name = converter.toName(ActivityType.StravaHIIT)
        assertThat(name).isEqualTo(ActivityType.StravaHIIT::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaHIIT)
    }

    @Test
    fun `To Activity type - StravaWorkout`() {
        val name = converter.toName(ActivityType.StravaWorkout)
        assertThat(name).isEqualTo(ActivityType.StravaWorkout::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaWorkout)
    }

    @Test
    fun `To Activity type - StravaWeightTraining`() {
        val name = converter.toName(ActivityType.StravaWeightTraining)
        assertThat(name).isEqualTo(ActivityType.StravaWeightTraining::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaWeightTraining)
    }

    @Test
    fun `To Activity type - StravaYoga`() {
        val name = converter.toName(ActivityType.StravaYoga)
        assertThat(name).isEqualTo(ActivityType.StravaYoga::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaYoga)
    }

    @Test
    fun `To Activity type - StravaSwim`() {
        val name = converter.toName(ActivityType.StravaSwim)
        assertThat(name).isEqualTo(ActivityType.StravaSwim::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaSwim)
    }

    @Test
    fun `To Activity type - StravaWalk`() {
        val name = converter.toName(ActivityType.StravaWalk)
        assertThat(name).isEqualTo(ActivityType.StravaWalk::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaWalk)
    }

    @Test
    fun `To Activity type - StravaHike`() {
        val name = converter.toName(ActivityType.StravaHike)
        assertThat(name).isEqualTo(ActivityType.StravaHike::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaHike)
    }

    @Test
    fun `To Activity type - StravaSnowboard`() {
        val name = converter.toName(ActivityType.StravaSnowboard)
        assertThat(name).isEqualTo(ActivityType.StravaSnowboard::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaSnowboard)
    }

    @Test
    fun `To Activity type - StravaKayaking`() {
        val name = converter.toName(ActivityType.StravaKayaking)
        assertThat(name).isEqualTo(ActivityType.StravaKayaking::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaKayaking)
    }

    @Test
    fun `To Activity type - StravaStandUpPaddling`() {
        val name = converter.toName(ActivityType.StravaStandUpPaddling)
        assertThat(name).isEqualTo(ActivityType.StravaStandUpPaddling::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaStandUpPaddling)
    }

    @Test
    fun `To Activity type - StravaSurfing`() {
        val name = converter.toName(ActivityType.StravaSurfing)
        assertThat(name).isEqualTo(ActivityType.StravaSurfing::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaSurfing)
    }

    @Test
    fun `To Activity type - StravaWindsurf`() {
        val name = converter.toName(ActivityType.StravaWindsurf)
        assertThat(name).isEqualTo(ActivityType.StravaWindsurf::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaWindsurf)
    }

    @Test
    fun `To Activity type - StravaFootball`() {
        val name = converter.toName(ActivityType.StravaFootball)
        assertThat(name).isEqualTo(ActivityType.StravaFootball::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaFootball)
    }
}