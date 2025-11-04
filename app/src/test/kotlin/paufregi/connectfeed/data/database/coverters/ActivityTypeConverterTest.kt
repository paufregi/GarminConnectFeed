package paufregi.connectfeed.data.database.coverters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.ActivityType

class ActivityTypeConverterTest {
    private val converter = ActivityTypeConverter()

    @Test
    fun `To Activity type - running`() {
        val name = converter.toName(ActivityType.Running)
        assertThat(name).isEqualTo(ActivityType.Running::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Running)
    }

    @Test
    fun `To Activity type - track running`() {
        val name = converter.toName(ActivityType.TrackRunning)
        assertThat(name).isEqualTo(ActivityType.TrackRunning::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.TrackRunning)
    }

    @Test
    fun `To Activity type - trail running`() {
        val name = converter.toName(ActivityType.TrailRunning)
        assertThat(name).isEqualTo(ActivityType.TrailRunning::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.TrailRunning)
    }

    @Test
    fun `To Activity type - treadmill running`() {
        val name = converter.toName(ActivityType.TreadmillRunning)
        assertThat(name).isEqualTo(ActivityType.TreadmillRunning::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.TreadmillRunning)
    }

    @Test
    fun `To Activity type - ultra run`() {
        val name = converter.toName(ActivityType.UltraRun)
        assertThat(name).isEqualTo(ActivityType.UltraRun::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.UltraRun)
    }

    @Test
    fun `To Activity type - cycling`() {
        val name = converter.toName(ActivityType.Cycling)
        assertThat(name).isEqualTo(ActivityType.Cycling::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Cycling)
    }

    @Test
    fun `To Activity type - downhill biking`() {
        val name = converter.toName(ActivityType.DownhillBiking)
        assertThat(name).isEqualTo(ActivityType.DownhillBiking::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.DownhillBiking)
    }

    @Test
    fun `To Activity type - eBiking`() {
        val name = converter.toName(ActivityType.EBiking)
        assertThat(name).isEqualTo(ActivityType.EBiking::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.EBiking)
    }

    @Test
    fun `To Activity type - eBiking mountain`() {
        val name = converter.toName(ActivityType.EBikingMountain)
        assertThat(name).isEqualTo(ActivityType.EBikingMountain::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.EBikingMountain)
    }

    @Test
    fun `To Activity type - gravel cycling`() {
        val name = converter.toName(ActivityType.GravelCycling)
        assertThat(name).isEqualTo(ActivityType.GravelCycling::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.GravelCycling)
    }

    @Test
    fun `To Activity type - mountain biking`() {
        val name = converter.toName(ActivityType.MountainBiking)
        assertThat(name).isEqualTo(ActivityType.MountainBiking::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.MountainBiking)
    }

    @Test
    fun `To Activity type - road biking`() {
        val name = converter.toName(ActivityType.RoadBiking)
        assertThat(name).isEqualTo(ActivityType.RoadBiking::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.RoadBiking)
    }

    @Test
    fun `To Activity type - virtual ride`() {
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
    fun `To Activity type - breathwork`() {
        val name = converter.toName(ActivityType.Breathwork)
        assertThat(name).isEqualTo(ActivityType.Breathwork::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Breathwork)
    }

    @Test
    fun `To Activity type - cardio`() {
        val name = converter.toName(ActivityType.Cardio)
        assertThat(name).isEqualTo(ActivityType.Cardio::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Cardio)
    }

    @Test
    fun `To Activity type - jump rope`() {
        val name = converter.toName(ActivityType.JumpRope)
        assertThat(name).isEqualTo(ActivityType.JumpRope::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.JumpRope)
    }

    @Test
    fun `To Activity type - strength training`() {
        val name = converter.toName(ActivityType.StrengthTraining)
        assertThat(name).isEqualTo(ActivityType.StrengthTraining::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StrengthTraining)
    }

    @Test
    fun `To Activity type - yoga`() {
        val name = converter.toName(ActivityType.Yoga)
        assertThat(name).isEqualTo(ActivityType.Yoga::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Yoga)
    }

    @Test
    fun `To Activity type - swimming`() {
        val name = converter.toName(ActivityType.Swimming)
        assertThat(name).isEqualTo(ActivityType.Swimming::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Swimming)
    }

    @Test
    fun `To Activity type - pool swimming`() {
        val name = converter.toName(ActivityType.PoolSwimming)
        assertThat(name).isEqualTo(ActivityType.PoolSwimming::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.PoolSwimming)
    }

    @Test
    fun `To Activity type - open water swimming`() {
        val name = converter.toName(ActivityType.OpenWaterSwimming)
        assertThat(name).isEqualTo(ActivityType.OpenWaterSwimming::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.OpenWaterSwimming)
    }

    @Test
    fun `To Activity type - walking`() {
        val name = converter.toName(ActivityType.Walking)
        assertThat(name).isEqualTo(ActivityType.Walking::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Walking)
    }

    @Test
    fun `To Activity type - hiking`() {
        val name = converter.toName(ActivityType.Hiking)
        assertThat(name).isEqualTo(ActivityType.Hiking::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Hiking)
    }

    @Test
    fun `To Activity type - snowboarding`() {
        val name = converter.toName(ActivityType.Snowboarding)
        assertThat(name).isEqualTo(ActivityType.Snowboarding::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Snowboarding)
    }

    @Test
    fun `To Activity type - kayaking`() {
        val name = converter.toName(ActivityType.Kayaking)
        assertThat(name).isEqualTo(ActivityType.Kayaking::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Kayaking)
    }

    @Test
    fun `To Activity type - stand up paddling`() {
        val name = converter.toName(ActivityType.StandUpPaddling)
        assertThat(name).isEqualTo(ActivityType.StandUpPaddling::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StandUpPaddling)
    }

    @Test
    fun `To Activity type - surfing`() {
        val name = converter.toName(ActivityType.Surfing)
        assertThat(name).isEqualTo(ActivityType.Surfing::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Surfing)
    }

    @Test
    fun `To Activity type - windsurf`() {
        val name = converter.toName(ActivityType.Windsurf)
        assertThat(name).isEqualTo(ActivityType.Windsurf::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Windsurf)
    }

    @Test
    fun `To Activity type - Strava running`() {
        val name = converter.toName(ActivityType.StravaRunning)
        assertThat(name).isEqualTo(ActivityType.StravaRunning::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaRunning)
    }

    @Test
    fun `To Activity type - Strava trail run`() {
        val name = converter.toName(ActivityType.StravaTrailRun)
        assertThat(name).isEqualTo(ActivityType.StravaTrailRun::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaTrailRun)
    }

    @Test
    fun `To Activity type - Strava ride`() {
        val name = converter.toName(ActivityType.StravaRide)
        assertThat(name).isEqualTo(ActivityType.StravaRide::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaRide)
    }

    @Test
    fun `To Activity type - Strava mountain bike ride`() {
        val name = converter.toName(ActivityType.StravaMountainBikeRide)
        assertThat(name).isEqualTo(ActivityType.StravaMountainBikeRide::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaMountainBikeRide)
    }

    @Test
    fun `To Activity type - Strava gravel ride`() {
        val name = converter.toName(ActivityType.StravaGravelRide)
        assertThat(name).isEqualTo(ActivityType.StravaGravelRide::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaGravelRide)
    }

    @Test
    fun `To Activity type - Strava eBike ride`() {
        val name = converter.toName(ActivityType.StravaEBikeRide)
        assertThat(name).isEqualTo(ActivityType.StravaEBikeRide::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaEBikeRide)
    }

    @Test
    fun `To Activity type - Strava eMountain bike ride`() {
        val name = converter.toName(ActivityType.StravaEMountainBikeRide)
        assertThat(name).isEqualTo(ActivityType.StravaEMountainBikeRide::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaEMountainBikeRide)
    }

    @Test
    fun `To Activity type - Strava virtual ride`() {
        val name = converter.toName(ActivityType.StravaVirtualRide)
        assertThat(name).isEqualTo(ActivityType.StravaVirtualRide::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaVirtualRide)
    }

    @Test
    fun `To Activity type - Strava HIIT`() {
        val name = converter.toName(ActivityType.StravaHIIT)
        assertThat(name).isEqualTo(ActivityType.StravaHIIT::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaHIIT)
    }

    @Test
    fun `To Activity type - Strava workout`() {
        val name = converter.toName(ActivityType.StravaWorkout)
        assertThat(name).isEqualTo(ActivityType.StravaWorkout::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaWorkout)
    }

    @Test
    fun `To Activity type - Strava weight training`() {
        val name = converter.toName(ActivityType.StravaWeightTraining)
        assertThat(name).isEqualTo(ActivityType.StravaWeightTraining::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaWeightTraining)
    }

    @Test
    fun `To Activity type - Strava yoga`() {
        val name = converter.toName(ActivityType.StravaYoga)
        assertThat(name).isEqualTo(ActivityType.StravaYoga::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaYoga)
    }

    @Test
    fun `To Activity type - Strava swim`() {
        val name = converter.toName(ActivityType.StravaSwim)
        assertThat(name).isEqualTo(ActivityType.StravaSwim::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaSwim)
    }

    @Test
    fun `To Activity type - Strava walk`() {
        val name = converter.toName(ActivityType.StravaWalk)
        assertThat(name).isEqualTo(ActivityType.StravaWalk::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaWalk)
    }

    @Test
    fun `To Activity type - Strava hike`() {
        val name = converter.toName(ActivityType.StravaHike)
        assertThat(name).isEqualTo(ActivityType.StravaHike::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaHike)
    }

    @Test
    fun `To Activity type - Strava snowboard`() {
        val name = converter.toName(ActivityType.StravaSnowboard)
        assertThat(name).isEqualTo(ActivityType.StravaSnowboard::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaSnowboard)
    }

    @Test
    fun `To Activity type - Strava kayaking`() {
        val name = converter.toName(ActivityType.StravaKayaking)
        assertThat(name).isEqualTo(ActivityType.StravaKayaking::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaKayaking)
    }

    @Test
    fun `To Activity type - Strava stand up paddling`() {
        val name = converter.toName(ActivityType.StravaStandUpPaddling)
        assertThat(name).isEqualTo(ActivityType.StravaStandUpPaddling::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaStandUpPaddling)
    }

    @Test
    fun `To Activity type - Strava surfing`() {
        val name = converter.toName(ActivityType.StravaSurfing)
        assertThat(name).isEqualTo(ActivityType.StravaSurfing::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaSurfing)
    }

    @Test
    fun `To Activity type - Strava windsurf`() {
        val name = converter.toName(ActivityType.StravaWindsurf)
        assertThat(name).isEqualTo(ActivityType.StravaWindsurf::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaWindsurf)
    }

    @Test
    fun `To Activity type - Strava football`() {
        val name = converter.toName(ActivityType.StravaFootball)
        assertThat(name).isEqualTo(ActivityType.StravaFootball::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StravaFootball)
    }

    @Test
    fun `To Activity type - unknown`() {
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
}