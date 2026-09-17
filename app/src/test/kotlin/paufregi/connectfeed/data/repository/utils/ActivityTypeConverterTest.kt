package paufregi.connectfeed.data.repository.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.data.api.garmin.models.ActivityType as GarminActivityType

class ActivityTypeConverterTest {

	@Test
	fun `garmin converts Running`() {
		val type = GarminActivityType(1L, "running")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.Running)
	}

	@Test
	fun `garmin converts TrackRunning`() {
		val type = GarminActivityType(8L, "track_running")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.TrackRunning)
	}

	@Test
	fun `garmin converts TrailRunning`() {
		val type = GarminActivityType(6L, "trail_running")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.TrailRunning)
	}

	@Test
	fun `garmin converts TreadmillRunning`() {
		val type = GarminActivityType(18L, "treadmill_running")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.TreadmillRunning)
	}

	@Test
	fun `garmin converts UltraRun`() {
		val type = GarminActivityType(181L, "ultra_run")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.UltraRun)
	}

	@Test
	fun `garmin converts Cycling`() {
		val type = GarminActivityType(2L, "cycling")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.Cycling)
	}

	@Test
	fun `garmin converts DownhillBiking`() {
		val type = GarminActivityType(20L, "downhill_biking")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.DownhillBiking)
	}

	@Test
	fun `garmin converts EBiking`() {
		val type = GarminActivityType(176L, "e_bike_fitness")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.EBiking)
	}

	@Test
	fun `garmin converts EBikingMountain`() {
		val type = GarminActivityType(175L, "e_bike_mountain")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.EBikingMountain)
	}

	@Test
	fun `garmin converts GravelCycling`() {
		val type = GarminActivityType(143L, "gravel_cycling")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.GravelCycling)
	}

	@Test
	fun `garmin converts MountainBiking`() {
		val type = GarminActivityType(5L, "mountain_biking")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.MountainBiking)
	}

	@Test
	fun `garmin converts RoadBiking`() {
		val type = GarminActivityType(10L, "road_biking")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.RoadBiking)
	}

	@Test
	fun `garmin converts IndoorRide`() {
		val type = GarminActivityType(25L, "indoor_cycling")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.IndoorRide)
	}

	@Test
	fun `garmin converts VirtualRide`() {
		val type = GarminActivityType(152L, "virtual_ride")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.VirtualRide)
	}

	@Test
	fun `garmin converts HIIT`() {
		val type = GarminActivityType(180L, "hiit")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.HIIT)
	}

	@Test
	fun `garmin converts Breathwork`() {
		val type = GarminActivityType(164L, "breathwork")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.Breathwork)
	}

	@Test
	fun `garmin converts Cardio`() {
		val type = GarminActivityType(11L, "indoor_cardio")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.Cardio)
	}

	@Test
	fun `garmin converts JumpRope`() {
		val type = GarminActivityType(254L, "jump_rope")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.JumpRope)
	}

	@Test
	fun `garmin converts StrengthTraining`() {
		val type = GarminActivityType(13L, "strength_training")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.StrengthTraining)
	}

	@Test
	fun `garmin converts Yoga`() {
		val type = GarminActivityType(163L, "yoga")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.Yoga)
	}

	@Test
	fun `garmin converts PoolSwimming`() {
		val type = GarminActivityType(27L, "lap_swimming")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.PoolSwimming)
	}

	@Test
	fun `garmin converts OpenWaterSwimming`() {
		val type = GarminActivityType(28L, "open_water_swimming")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.OpenWaterSwimming)
	}

	@Test
	fun `garmin converts Swimming`() {
		val type = GarminActivityType(26L, "swimming")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.Swimming)
	}

	@Test
	fun `garmin converts Multisport`() {
		val type = GarminActivityType(89L, "multi_sport")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.Multisport)
	}

	@Test
	fun `garmin converts Walking`() {
		val type = GarminActivityType(9L, "walking")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.Walking)
	}

	@Test
	fun `garmin converts Hiking`() {
		val type = GarminActivityType(3L, "hiking")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.Hiking)
	}

	@Test
	fun `garmin converts Snowboarding`() {
		val type = GarminActivityType(252L, "resort_snowboarding")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.Snowboarding)
	}

	@Test
	fun `garmin converts Kayaking`() {
		val type = GarminActivityType(231L, "kayaking_v2")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.Kayaking)
	}

	@Test
	fun `garmin converts StandUpPaddling`() {
		val type = GarminActivityType(239L, "stand_up_paddleboarding_v2")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.StandUpPaddling)
	}

	@Test
	fun `garmin converts Surfing`() {
		val type = GarminActivityType(240L, "surfing_v2")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.Surfing)
	}

	@Test
	fun `garmin converts Windsurf`() {
		val type = GarminActivityType(242L, "windsurfing_v2")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.Windsurf)
	}

	@Test
	fun `garmin returns unknown for unsupported type id`() {
		val type = GarminActivityType(-1L, "unknown")
		assertThat(ActivityTypeConverter.garmin(type)).isEqualTo(ActivityType.Unknown)
	}

	@Test
	fun `strava converts Run`() {
		assertThat(ActivityTypeConverter.strava("Run")).isEqualTo(ActivityType.Running)
	}

	@Test
	fun `strava converts TrailRun`() {
		assertThat(ActivityTypeConverter.strava("TrailRun")).isEqualTo(ActivityType.Running)
	}

	@Test
	fun `strava converts VirtualRun`() {
		assertThat(ActivityTypeConverter.strava("VirtualRun")).isEqualTo(ActivityType.Running)
	}

	@Test
	fun `strava converts Ride`() {
		assertThat(ActivityTypeConverter.strava("Ride")).isEqualTo(ActivityType.Cycling)
	}

	@Test
	fun `strava converts MountainBikeRide`() {
		assertThat(ActivityTypeConverter.strava("MountainBikeRide")).isEqualTo(ActivityType.Cycling)
	}

	@Test
	fun `strava converts GravelRide`() {
		assertThat(ActivityTypeConverter.strava("GravelRide")).isEqualTo(ActivityType.Cycling)
	}

	@Test
	fun `strava converts EBikeRide`() {
		assertThat(ActivityTypeConverter.strava("EBikeRide")).isEqualTo(ActivityType.Cycling)
	}

	@Test
	fun `strava converts EMountainBikeRide`() {
		assertThat(ActivityTypeConverter.strava("EMountainBikeRide")).isEqualTo(ActivityType.Cycling)
	}

	@Test
	fun `strava converts VirtualRide`() {
		assertThat(ActivityTypeConverter.strava("VirtualRide")).isEqualTo(ActivityType.Cycling)
	}

	@Test
	fun `strava converts HighIntensityIntervalTraining`() {
		assertThat(ActivityTypeConverter.strava("HighIntensityIntervalTraining")).isEqualTo(ActivityType.Fitness)
	}

	@Test
	fun `strava converts Workout`() {
		assertThat(ActivityTypeConverter.strava("Workout")).isEqualTo(ActivityType.Fitness)
	}

	@Test
	fun `strava converts WeightTraining`() {
		assertThat(ActivityTypeConverter.strava("WeightTraining")).isEqualTo(ActivityType.Fitness)
	}

	@Test
	fun `strava converts Yoga`() {
		assertThat(ActivityTypeConverter.strava("Yoga")).isEqualTo(ActivityType.Fitness)
	}

	@Test
	fun `strava converts Swim`() {
		assertThat(ActivityTypeConverter.strava("Swim")).isEqualTo(ActivityType.Swimming)
	}

	@Test
	fun `strava converts Walk`() {
		assertThat(ActivityTypeConverter.strava("Walk")).isEqualTo(ActivityType.Other)
	}

	@Test
	fun `strava converts Hike`() {
		assertThat(ActivityTypeConverter.strava("Hike")).isEqualTo(ActivityType.Other)
	}

	@Test
	fun `strava converts Snowboard`() {
		assertThat(ActivityTypeConverter.strava("Snowboard")).isEqualTo(ActivityType.Other)
	}

	@Test
	fun `strava converts Kayaking`() {
		assertThat(ActivityTypeConverter.strava("Kayaking")).isEqualTo(ActivityType.Other)
	}

	@Test
	fun `strava converts StandUpPaddling`() {
		assertThat(ActivityTypeConverter.strava("StandUpPaddling")).isEqualTo(ActivityType.Other)
	}

	@Test
	fun `strava converts Surfing`() {
		assertThat(ActivityTypeConverter.strava("Surfing")).isEqualTo(ActivityType.Other)
	}

	@Test
	fun `strava converts Windsurf`() {
		assertThat(ActivityTypeConverter.strava("Windsurf")).isEqualTo(ActivityType.Other)
	}

	@Test
	fun `strava converts Soccer`() {
		assertThat(ActivityTypeConverter.strava("Soccer")).isEqualTo(ActivityType.Other)
	}

	@Test
	fun `strava returns unknown for unsupported sport type`() {
		assertThat(ActivityTypeConverter.strava("AlpineSki")).isEqualTo(ActivityType.Unknown)
	}
}



