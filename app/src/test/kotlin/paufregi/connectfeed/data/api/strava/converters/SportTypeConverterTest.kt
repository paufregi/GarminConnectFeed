package paufregi.connectfeed.data.api.strava.converters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.ActivityType

class SportTypeConverterTest {
    @Test
    fun `To Strava activity type - Run`() {
        val activityType = SportTypeConverter.toActivityType("Run")
        assertThat(activityType).isEqualTo(ActivityType.StravaRunning)
    }

    @Test
    fun `To Strava activity type - TrailRun`() {
        val activityType = SportTypeConverter.toActivityType("TrailRun")
        assertThat(activityType).isEqualTo(ActivityType.StravaTrailRun)
    }

    @Test
    fun `To Strava activity type - VirtualRun`() {
        val activityType = SportTypeConverter.toActivityType("VirtualRun")
        assertThat(activityType).isEqualTo(ActivityType.StravaRunning)
    }

    @Test
    fun `To Strava activity type - Ride`() {
        val activityType = SportTypeConverter.toActivityType("Ride")
        assertThat(activityType).isEqualTo(ActivityType.StravaRide)
    }

    @Test
    fun `To Strava activity type - MountainBikeRide`() {
        val activityType = SportTypeConverter.toActivityType("MountainBikeRide")
        assertThat(activityType).isEqualTo(ActivityType.StravaMountainBikeRide)
    }

    @Test
    fun `To Strava activity type - GravelRide`() {
        val activityType = SportTypeConverter.toActivityType("GravelRide")
        assertThat(activityType).isEqualTo(ActivityType.StravaGravelRide)
    }

    @Test
    fun `To Strava activity type - EBikeRide`() {
        val activityType = SportTypeConverter.toActivityType("EBikeRide")
        assertThat(activityType).isEqualTo(ActivityType.StravaEBikeRide)
    }

    @Test
    fun `To Strava activity type - EMountainBikeRide`() {
        val activityType = SportTypeConverter.toActivityType("EMountainBikeRide")
        assertThat(activityType).isEqualTo(ActivityType.StravaEMountainBikeRide)
    }

    @Test
    fun `To Strava activity type - VirtualRide`() {
        val activityType = SportTypeConverter.toActivityType("VirtualRide")
        assertThat(activityType).isEqualTo(ActivityType.StravaVirtualRide)
    }

    @Test
    fun `To Strava activity type - HighIntensityIntervalTraining`() {
        val activityType = SportTypeConverter.toActivityType("HighIntensityIntervalTraining")
        assertThat(activityType).isEqualTo(ActivityType.StravaHIIT)
    }

    @Test
    fun `To Strava activity type - Workout`() {
        val activityType = SportTypeConverter.toActivityType("Workout")
        assertThat(activityType).isEqualTo(ActivityType.StravaWorkout)
    }

    @Test
    fun `To Strava activity type - WeightTraining`() {
        val activityType = SportTypeConverter.toActivityType("WeightTraining")
        assertThat(activityType).isEqualTo(ActivityType.StravaWeightTraining)
    }

    @Test
    fun `To Strava activity type - Yoga`() {
        val activityType = SportTypeConverter.toActivityType("Yoga")
        assertThat(activityType).isEqualTo(ActivityType.StravaYoga)
    }

    @Test
    fun `To Strava activity type - Swim`() {
        val activityType = SportTypeConverter.toActivityType("Swim")
        assertThat(activityType).isEqualTo(ActivityType.StravaSwim)
    }

    @Test
    fun `To Strava activity type - Walk`() {
        val activityType = SportTypeConverter.toActivityType("Walk")
        assertThat(activityType).isEqualTo(ActivityType.StravaWalk)
    }

    @Test
    fun `To Strava activity type - Hike`() {
        val activityType = SportTypeConverter.toActivityType("Hike")
        assertThat(activityType).isEqualTo(ActivityType.StravaHike)
    }

    @Test
    fun `To Strava activity type - Snowboard`() {
        val activityType = SportTypeConverter.toActivityType("Snowboard")
        assertThat(activityType).isEqualTo(ActivityType.StravaSnowboard)
    }

    @Test
    fun `To Strava activity type - Kayaking`() {
        val activityType = SportTypeConverter.toActivityType("Kayaking")
        assertThat(activityType).isEqualTo(ActivityType.StravaKayaking)
    }

    @Test
    fun `To Strava activity type - StandUpPaddling`() {
        val activityType = SportTypeConverter.toActivityType("StandUpPaddling")
        assertThat(activityType).isEqualTo(ActivityType.StravaStandUpPaddling)
    }

    @Test
    fun `To Strava activity type - Surfing`() {
        val activityType = SportTypeConverter.toActivityType("Surfing")
        assertThat(activityType).isEqualTo(ActivityType.StravaSurfing)
    }

    @Test
    fun `To Strava activity type - Windsurf`() {
        val activityType = SportTypeConverter.toActivityType("Windsurf")
        assertThat(activityType).isEqualTo(ActivityType.StravaWindsurf)
    }

    @Test
    fun `To Strava activity type - Soccer`() {
        val activityType = SportTypeConverter.toActivityType("Soccer")
        assertThat(activityType).isEqualTo(ActivityType.StravaFootball)
    }

    @Test
    fun `To Strava activity type - Unknown`() {
        val activityType = SportTypeConverter.toActivityType("Unknown")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

}