package paufregi.connectfeed.data.api.garmin.serializers

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import org.junit.Test
import paufregi.connectfeed.data.api.garmin.models.ActivityType
import paufregi.connectfeed.core.models.ActivityType as CoreActivityType

class ActivityTypeSerializerTest {

    private val json = Json { encodeDefaults = true }

    @Test
    fun `Serialize ActivityType Running to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(1L, "running", CoreActivityType.Running))

        assertThat(res).isEqualTo("{\"typeId\":1,\"typeKey\":\"running\"}")
    }

    @Test
    fun `Serialize ActivityType TrackRunning to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(8L, "track_running", CoreActivityType.TrackRunning))

        assertThat(res).isEqualTo("{\"typeId\":8,\"typeKey\":\"track_running\"}")
    }

    @Test
    fun `Serialize ActivityType TrailRunning to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(6L, "trail_running", CoreActivityType.TrailRunning))

        assertThat(res).isEqualTo("{\"typeId\":6,\"typeKey\":\"trail_running\"}")
    }

    @Test
    fun `Serialize ActivityType TreadmillRunning to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(18L, "treadmill_running", CoreActivityType.TreadmillRunning))

        assertThat(res).isEqualTo("{\"typeId\":18,\"typeKey\":\"treadmill_running\"}")
    }

    @Test
    fun `Serialize ActivityType UltraRun to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(181L, "ultra_run", CoreActivityType.UltraRun))

        assertThat(res).isEqualTo("{\"typeId\":181,\"typeKey\":\"ultra_run\"}")
    }

    @Test
    fun `Serialize ActivityType Cycling to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(2L, "cycling", CoreActivityType.Cycling))

        assertThat(res).isEqualTo("{\"typeId\":2,\"typeKey\":\"cycling\"}")
    }

    @Test
    fun `Serialize ActivityType DownhillBiking to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(20L, "downhill_biking", CoreActivityType.DownhillBiking))

        assertThat(res).isEqualTo("{\"typeId\":20,\"typeKey\":\"downhill_biking\"}")
    }

    @Test
    fun `Serialize ActivityType EBiking to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(176L, "e_bike_fitness", CoreActivityType.EBiking))

        assertThat(res).isEqualTo("{\"typeId\":176,\"typeKey\":\"e_bike_fitness\"}")
    }

    @Test
    fun `Serialize ActivityType EBikingMountain to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(175L, "e_bike_mountain", CoreActivityType.EBikingMountain))

        assertThat(res).isEqualTo("{\"typeId\":175,\"typeKey\":\"e_bike_mountain\"}")
    }

    @Test
    fun `Serialize ActivityType GravelCycling to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(143L, "gravel_cycling", CoreActivityType.GravelCycling))

        assertThat(res).isEqualTo("{\"typeId\":143,\"typeKey\":\"gravel_cycling\"}")
    }

    @Test
    fun `Serialize ActivityType MountainBiking to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(5L, "mountain_biking", CoreActivityType.MountainBiking))

        assertThat(res).isEqualTo("{\"typeId\":5,\"typeKey\":\"mountain_biking\"}")
    }

    @Test
    fun `Serialize ActivityType RoadBiking to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(10L, "road_biking", CoreActivityType.RoadBiking))

        assertThat(res).isEqualTo("{\"typeId\":10,\"typeKey\":\"road_biking\"}")
    }

    @Test
    fun `Serialize ActivityType IndoorRide to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(25L, "indoor_cycling", CoreActivityType.IndoorRide))

        assertThat(res).isEqualTo("{\"typeId\":25,\"typeKey\":\"indoor_cycling\"}")
    }

    @Test
    fun `Serialize ActivityType VirtualRide to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(152L, "virtual_ride", CoreActivityType.VirtualRide))

        assertThat(res).isEqualTo("{\"typeId\":152,\"typeKey\":\"virtual_ride\"}")
    }

    @Test
    fun `Serialize ActivityType HIIT to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(180L, "hiit", CoreActivityType.HIIT))

        assertThat(res).isEqualTo("{\"typeId\":180,\"typeKey\":\"hiit\"}")
    }

    @Test
    fun `Serialize ActivityType Breathwork to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(164L, "breathwork", CoreActivityType.Breathwork))

        assertThat(res).isEqualTo("{\"typeId\":164,\"typeKey\":\"breathwork\"}")
    }

    @Test
    fun `Serialize ActivityType Cardio to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(11L, "indoor_cardio", CoreActivityType.Cardio))

        assertThat(res).isEqualTo("{\"typeId\":11,\"typeKey\":\"indoor_cardio\"}")
    }

    @Test
    fun `Serialize ActivityType JumpRope to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(254L, "jump_rope", CoreActivityType.JumpRope))

        assertThat(res).isEqualTo("{\"typeId\":254,\"typeKey\":\"jump_rope\"}")
    }

    @Test
    fun `Serialize ActivityType StrengthTraining to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(13L, "strength_training", CoreActivityType.StrengthTraining))

        assertThat(res).isEqualTo("{\"typeId\":13,\"typeKey\":\"strength_training\"}")
    }

    @Test
    fun `Serialize ActivityType Yoga to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(163L, "yoga", CoreActivityType.Yoga))

        assertThat(res).isEqualTo("{\"typeId\":163,\"typeKey\":\"yoga\"}")
    }

    @Test
    fun `Serialize ActivityType PoolSwimming to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(27L, "lap_swimming", CoreActivityType.PoolSwimming))

        assertThat(res).isEqualTo("{\"typeId\":27,\"typeKey\":\"lap_swimming\"}")
    }

    @Test
    fun `Serialize ActivityType OpenWaterSwimming to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(28L, "open_water_swimming", CoreActivityType.OpenWaterSwimming))

        assertThat(res).isEqualTo("{\"typeId\":28,\"typeKey\":\"open_water_swimming\"}")
    }

    @Test
    fun `Serialize ActivityType Swimming to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(26L, "swimming", CoreActivityType.Swimming))

        assertThat(res).isEqualTo("{\"typeId\":26,\"typeKey\":\"swimming\"}")
    }

    @Test
    fun `Serialize ActivityType Multisport to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(89L, "multi_sport", CoreActivityType.Multisport))

        assertThat(res).isEqualTo("{\"typeId\":89,\"typeKey\":\"multi_sport\"}")
    }

    @Test
    fun `Serialize ActivityType Walking to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(9L, "walking", CoreActivityType.Walking))

        assertThat(res).isEqualTo("{\"typeId\":9,\"typeKey\":\"walking\"}")
    }

    @Test
    fun `Serialize ActivityType Hiking to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(3L, "hiking", CoreActivityType.Hiking))

        assertThat(res).isEqualTo("{\"typeId\":3,\"typeKey\":\"hiking\"}")
    }

    @Test
    fun `Serialize ActivityType Snowboarding to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(252L, "resort_snowboarding", CoreActivityType.Snowboarding))

        assertThat(res).isEqualTo("{\"typeId\":252,\"typeKey\":\"resort_snowboarding\"}")
    }

    @Test
    fun `Serialize ActivityType Kayaking to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(231L, "kayaking_v2", CoreActivityType.Kayaking))

        assertThat(res).isEqualTo("{\"typeId\":231,\"typeKey\":\"kayaking_v2\"}")
    }

    @Test
    fun `Serialize ActivityType StandUpPaddling to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(239L, "stand_up_paddleboarding_v2", CoreActivityType.StandUpPaddling))

        assertThat(res).isEqualTo("{\"typeId\":239,\"typeKey\":\"stand_up_paddleboarding_v2\"}")
    }

    @Test
    fun `Serialize ActivityType Surfing to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(240L, "surfing_v2", CoreActivityType.Surfing))

        assertThat(res).isEqualTo("{\"typeId\":240,\"typeKey\":\"surfingGear\"}")
    }

    @Test
    fun `Serialize ActivityType Windsurf to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(242L, "windsurfing_v2", CoreActivityType.Windsurf))

        assertThat(res).isEqualTo("{\"typeId\":242,\"typeKey\":\"windsurfingGear\"}")
    }

    @Test
    fun `Serialize ActivityType Unknown to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityType(-1L, "unknown", CoreActivityType.Unknown))

        assertThat(res).isEqualTo("{\"typeId\":-1,\"typeKey\":\"unknown\"}")
    }

    @Test
    fun `Deserialize JSON to ActivityType Running`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":1,\"typeKey\":\"running\"}")

        assertThat(res).isEqualTo(ActivityType(1L, "running", CoreActivityType.Running))
    }

    @Test
    fun `Deserialize JSON to ActivityType TrackRunning`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":8,\"typeKey\":\"track_running\"}")

        assertThat(res).isEqualTo(ActivityType(8L, "track_running", CoreActivityType.TrackRunning))
    }

    @Test
    fun `Deserialize JSON to ActivityType TrailRunning`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":6,\"typeKey\":\"trail_running\"}")

        assertThat(res).isEqualTo(ActivityType(6L, "trail_running", CoreActivityType.TrailRunning))
    }

    @Test
    fun `Deserialize JSON to ActivityType TreadmillRunning`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":18,\"typeKey\":\"treadmill_running\"}")

        assertThat(res).isEqualTo(ActivityType(18L, "treadmill_running", CoreActivityType.TreadmillRunning))
    }

    @Test
    fun `Deserialize JSON to ActivityType UltraRun`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":181,\"typeKey\":\"ultra_run\"}")

        assertThat(res).isEqualTo(ActivityType(181L, "ultra_run", CoreActivityType.UltraRun))
    }

    @Test
    fun `Deserialize JSON to ActivityType Cycling`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":2,\"typeKey\":\"cycling\"}")

        assertThat(res).isEqualTo(ActivityType(2L, "cycling", CoreActivityType.Cycling))
    }

    @Test
    fun `Deserialize JSON to ActivityType DownhillBiking`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":20,\"typeKey\":\"downhill_biking\"}")

        assertThat(res).isEqualTo(ActivityType(20L, "downhill_biking", CoreActivityType.DownhillBiking))
    }

    @Test
    fun `Deserialize JSON to ActivityType EBiking`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":176,\"typeKey\":\"e_bike_fitness\"}")

        assertThat(res).isEqualTo(ActivityType(176L, "e_bike_fitness", CoreActivityType.EBiking))
    }

    @Test
    fun `Deserialize JSON to ActivityType EBikingMountain`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":175,\"typeKey\":\"e_bike_mountain\"}")

        assertThat(res).isEqualTo(ActivityType(175L, "e_bike_mountain", CoreActivityType.EBikingMountain))
    }

    @Test
    fun `Deserialize JSON to ActivityType GravelCycling`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":143,\"typeKey\":\"gravel_cycling\"}")

        assertThat(res).isEqualTo(ActivityType(143L, "gravel_cycling", CoreActivityType.GravelCycling))
    }

    @Test
    fun `Deserialize JSON to ActivityType MountainBiking`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":5,\"typeKey\":\"mountain_biking\"}")

        assertThat(res).isEqualTo(ActivityType(5L, "mountain_biking", CoreActivityType.MountainBiking))
    }

    @Test
    fun `Deserialize JSON to ActivityType RoadBiking`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":10,\"typeKey\":\"road_biking\"}")

        assertThat(res).isEqualTo(ActivityType(10L, "road_biking", CoreActivityType.RoadBiking))
    }

    @Test
    fun `Deserialize JSON to ActivityType IndoorRide`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":25,\"typeKey\":\"indoor_cycling\"}")

        assertThat(res).isEqualTo(ActivityType(25L, "indoor_cycling", CoreActivityType.IndoorRide))
    }

    @Test
    fun `Deserialize JSON to ActivityType VirtualRide`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":152,\"typeKey\":\"virtual_ride\"}")

        assertThat(res).isEqualTo(ActivityType(152L, "virtual_ride", CoreActivityType.VirtualRide))
    }

    @Test
    fun `Deserialize JSON to ActivityType HIIT`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":180,\"typeKey\":\"hiit\"}")

        assertThat(res).isEqualTo(ActivityType(180L, "hiit", CoreActivityType.HIIT))
    }

    @Test
    fun `Deserialize JSON to ActivityType Breathwork`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":164,\"typeKey\":\"breathwork\"}")

        assertThat(res).isEqualTo(ActivityType(164L, "breathwork", CoreActivityType.Breathwork))
    }

    @Test
    fun `Deserialize JSON to ActivityType Cardio`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":11,\"typeKey\":\"indoor_cardio\"}")

        assertThat(res).isEqualTo(ActivityType(11L, "indoor_cardio", CoreActivityType.Cardio))
    }

    @Test
    fun `Deserialize JSON to ActivityType JumpRope`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":254,\"typeKey\":\"jump_rope\"}")

        assertThat(res).isEqualTo(ActivityType(254L, "jump_rope", CoreActivityType.JumpRope))
    }

    @Test
    fun `Deserialize JSON to ActivityType StrengthTraining`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":13,\"typeKey\":\"strength_training\"}")

        assertThat(res).isEqualTo(ActivityType(13L, "strength_training", CoreActivityType.StrengthTraining))
    }

    @Test
    fun `Deserialize JSON to ActivityType Yoga`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":163,\"typeKey\":\"yoga\"}")

        assertThat(res).isEqualTo(ActivityType(163L, "yoga", CoreActivityType.Yoga))
    }

    @Test
    fun `Deserialize JSON to ActivityType PoolSwimming`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":27,\"typeKey\":\"lap_swimming\"}")

        assertThat(res).isEqualTo(ActivityType(27L, "lap_swimming", CoreActivityType.PoolSwimming))
    }

    @Test
    fun `Deserialize JSON to ActivityType OpenWaterSwimming`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":28,\"typeKey\":\"open_water_swimming\"}")

        assertThat(res).isEqualTo(ActivityType(28L, "open_water_swimming", CoreActivityType.OpenWaterSwimming))
    }

    @Test
    fun `Deserialize JSON to ActivityType Swimming`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":26,\"typeKey\":\"swimming\"}")

        assertThat(res).isEqualTo(ActivityType(26L, "swimming", CoreActivityType.Swimming))
    }

    @Test
    fun `Deserialize JSON to ActivityType Multisport`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":89,\"typeKey\":\"multi_sport\"}")

        assertThat(res).isEqualTo(ActivityType(89L, "multi_sport", CoreActivityType.Multisport))
    }

    @Test
    fun `Deserialize JSON to ActivityType Walking`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":9,\"typeKey\":\"walking\"}")

        assertThat(res).isEqualTo(ActivityType(9L, "walking", CoreActivityType.Walking))
    }

    @Test
    fun `Deserialize JSON to ActivityType Hiking`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":3,\"typeKey\":\"hiking\"}")

        assertThat(res).isEqualTo(ActivityType(3L, "hiking", CoreActivityType.Hiking))
    }

    @Test
    fun `Deserialize JSON to ActivityType Snowboarding`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":252,\"typeKey\":\"resort_snowboarding\"}")

        assertThat(res).isEqualTo(ActivityType(252L, "resort_snowboarding", CoreActivityType.Snowboarding))
    }

    @Test
    fun `Deserialize JSON to ActivityType Kayaking`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":231,\"typeKey\":\"kayaking_v2\"}")

        assertThat(res).isEqualTo(ActivityType(231L, "kayaking_v2", CoreActivityType.Kayaking))
    }

    @Test
    fun `Deserialize JSON to ActivityType StandUpPaddling`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":239,\"typeKey\":\"stand_up_paddleboarding_v2\"}")

        assertThat(res).isEqualTo(ActivityType(239L, "stand_up_paddleboarding_v2", CoreActivityType.StandUpPaddling))
    }

    @Test
    fun `Deserialize JSON to ActivityType Surfing`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":240,\"typeKey\":\"surfing_v2\"}")

        assertThat(res).isEqualTo(ActivityType(240L, "surfing_v2", CoreActivityType.Surfing))
    }

    @Test
    fun `Deserialize JSON to ActivityType Windsurf`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":242,\"typeKey\":\"windsurfing_v2\"}")

        assertThat(res).isEqualTo(ActivityType(242L, "windsurfing_v2", CoreActivityType.Windsurf))
    }

    @Test
    fun `Deserialize unknown JSON to ActivityType Unknown`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":-1,\"typeKey\":\"unknown\"}")

        assertThat(res).isEqualTo(ActivityType(-1L, "unknown", CoreActivityType.Unknown))
    }

    @Test
    fun `Deserialize unsupported JSON to ActivityType Unknown`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":999,\"typeKey\":\"nope\"}")

        assertThat(res).isEqualTo(ActivityType(999L, "nope", CoreActivityType.Unknown))
    }
}

