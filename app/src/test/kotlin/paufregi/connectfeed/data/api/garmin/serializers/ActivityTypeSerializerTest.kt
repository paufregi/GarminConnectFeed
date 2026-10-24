package paufregi.connectfeed.data.api.garmin.serializers

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import org.junit.Test
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.data.api.garmin.models.ActivityTypeDto

class ActivityTypeSerializerTest {

    private val json = Json { encodeDefaults = true }

    @Test
    fun `Serialize ActivityType Running to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(1L, "running", ActivityType.Running))

        assertThat(res).isEqualTo("{\"typeId\":1,\"typeKey\":\"running\"}")
    }

    @Test
    fun `Serialize ActivityType TrackRunning to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(8L, "track_running", ActivityType.TrackRunning))

        assertThat(res).isEqualTo("{\"typeId\":8,\"typeKey\":\"track_running\"}")
    }

    @Test
    fun `Serialize ActivityType TrailRunning to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(6L, "trail_running", ActivityType.TrailRunning))

        assertThat(res).isEqualTo("{\"typeId\":6,\"typeKey\":\"trail_running\"}")
    }

    @Test
    fun `Serialize ActivityType TreadmillRunning to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(18L, "treadmill_running", ActivityType.TreadmillRunning))

        assertThat(res).isEqualTo("{\"typeId\":18,\"typeKey\":\"treadmill_running\"}")
    }

    @Test
    fun `Serialize ActivityType UltraRun to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(181L, "ultra_run", ActivityType.UltraRun))

        assertThat(res).isEqualTo("{\"typeId\":181,\"typeKey\":\"ultra_run\"}")
    }

    @Test
    fun `Serialize ActivityType Cycling to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(2L, "cycling", ActivityType.Cycling))

        assertThat(res).isEqualTo("{\"typeId\":2,\"typeKey\":\"cycling\"}")
    }

    @Test
    fun `Serialize ActivityType DownhillBiking to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(20L, "downhill_biking", ActivityType.DownhillBiking))

        assertThat(res).isEqualTo("{\"typeId\":20,\"typeKey\":\"downhill_biking\"}")
    }

    @Test
    fun `Serialize ActivityType EBiking to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(176L, "e_bike_fitness", ActivityType.EBiking))

        assertThat(res).isEqualTo("{\"typeId\":176,\"typeKey\":\"e_bike_fitness\"}")
    }

    @Test
    fun `Serialize ActivityType EBikingMountain to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(175L, "e_bike_mountain", ActivityType.EBikingMountain))

        assertThat(res).isEqualTo("{\"typeId\":175,\"typeKey\":\"e_bike_mountain\"}")
    }

    @Test
    fun `Serialize ActivityType GravelCycling to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(143L, "gravel_cycling", ActivityType.GravelCycling))

        assertThat(res).isEqualTo("{\"typeId\":143,\"typeKey\":\"gravel_cycling\"}")
    }

    @Test
    fun `Serialize ActivityType MountainBiking to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(5L, "mountain_biking", ActivityType.MountainBiking))

        assertThat(res).isEqualTo("{\"typeId\":5,\"typeKey\":\"mountain_biking\"}")
    }

    @Test
    fun `Serialize ActivityType RoadBiking to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(10L, "road_biking", ActivityType.RoadBiking))

        assertThat(res).isEqualTo("{\"typeId\":10,\"typeKey\":\"road_biking\"}")
    }

    @Test
    fun `Serialize ActivityType IndoorRide to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(25L, "indoor_cycling", ActivityType.IndoorRide))

        assertThat(res).isEqualTo("{\"typeId\":25,\"typeKey\":\"indoor_cycling\"}")
    }

    @Test
    fun `Serialize ActivityType VirtualRide to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(152L, "virtual_ride", ActivityType.VirtualRide))

        assertThat(res).isEqualTo("{\"typeId\":152,\"typeKey\":\"virtual_ride\"}")
    }

    @Test
    fun `Serialize ActivityType HIIT to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(180L, "hiit", ActivityType.HIIT))

        assertThat(res).isEqualTo("{\"typeId\":180,\"typeKey\":\"hiit\"}")
    }

    @Test
    fun `Serialize ActivityType Breathwork to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(164L, "breathwork", ActivityType.Breathwork))

        assertThat(res).isEqualTo("{\"typeId\":164,\"typeKey\":\"breathwork\"}")
    }

    @Test
    fun `Serialize ActivityType Cardio to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(11L, "indoor_cardio", ActivityType.Cardio))

        assertThat(res).isEqualTo("{\"typeId\":11,\"typeKey\":\"indoor_cardio\"}")
    }

    @Test
    fun `Serialize ActivityType JumpRope to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(254L, "jump_rope", ActivityType.JumpRope))

        assertThat(res).isEqualTo("{\"typeId\":254,\"typeKey\":\"jump_rope\"}")
    }

    @Test
    fun `Serialize ActivityType StrengthTraining to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(13L, "strength_training", ActivityType.StrengthTraining))

        assertThat(res).isEqualTo("{\"typeId\":13,\"typeKey\":\"strength_training\"}")
    }

    @Test
    fun `Serialize ActivityType Yoga to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(163L, "yoga", ActivityType.Yoga))

        assertThat(res).isEqualTo("{\"typeId\":163,\"typeKey\":\"yoga\"}")
    }

    @Test
    fun `Serialize ActivityType PoolSwimming to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(27L, "lap_swimming", ActivityType.PoolSwimming))

        assertThat(res).isEqualTo("{\"typeId\":27,\"typeKey\":\"lap_swimming\"}")
    }

    @Test
    fun `Serialize ActivityType OpenWaterSwimming to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(28L, "open_water_swimming", ActivityType.OpenWaterSwimming))

        assertThat(res).isEqualTo("{\"typeId\":28,\"typeKey\":\"open_water_swimming\"}")
    }

    @Test
    fun `Serialize ActivityType Swimming to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(26L, "swimming", ActivityType.Swimming))

        assertThat(res).isEqualTo("{\"typeId\":26,\"typeKey\":\"swimming\"}")
    }

    @Test
    fun `Serialize ActivityType Multisport to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(89L, "multi_sport", ActivityType.Multisport))

        assertThat(res).isEqualTo("{\"typeId\":89,\"typeKey\":\"multi_sport\"}")
    }

    @Test
    fun `Serialize ActivityType Walking to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(9L, "walking", ActivityType.Walking))

        assertThat(res).isEqualTo("{\"typeId\":9,\"typeKey\":\"walking\"}")
    }

    @Test
    fun `Serialize ActivityType Hiking to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(3L, "hiking", ActivityType.Hiking))

        assertThat(res).isEqualTo("{\"typeId\":3,\"typeKey\":\"hiking\"}")
    }

    @Test
    fun `Serialize ActivityType Snowboarding to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(252L, "resort_snowboarding", ActivityType.Snowboarding))

        assertThat(res).isEqualTo("{\"typeId\":252,\"typeKey\":\"resort_snowboarding\"}")
    }

    @Test
    fun `Serialize ActivityType Kayaking to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(231L, "kayaking_v2", ActivityType.Kayaking))

        assertThat(res).isEqualTo("{\"typeId\":231,\"typeKey\":\"kayaking_v2\"}")
    }

    @Test
    fun `Serialize ActivityType StandUpPaddling to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(239L, "stand_up_paddleboarding_v2", ActivityType.StandUpPaddling))

        assertThat(res).isEqualTo("{\"typeId\":239,\"typeKey\":\"stand_up_paddleboarding_v2\"}")
    }

    @Test
    fun `Serialize ActivityType Surfing to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(240L, "surfing_v2", ActivityType.Surfing))

        assertThat(res).isEqualTo("{\"typeId\":240,\"typeKey\":\"surfing_v2\"}")
    }

    @Test
    fun `Serialize ActivityType Windsurf to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(242L, "windsurfing_v2", ActivityType.Windsurf))

        assertThat(res).isEqualTo("{\"typeId\":242,\"typeKey\":\"windsurfing_v2\"}")
    }

    @Test
    fun `Serialize ActivityType Unknown to JSON`() {
        val res = json.encodeToString(ActivityTypeSerializer, ActivityTypeDto(-1L, "unknown", ActivityType.Unknown))

        assertThat(res).isEqualTo("{\"typeId\":-1,\"typeKey\":\"unknown\"}")
    }

    @Test
    fun `Deserialize JSON to ActivityType Running`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":1,\"typeKey\":\"running\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(1L, "running", ActivityType.Running))
    }

    @Test
    fun `Deserialize JSON to ActivityType TrackRunning`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":8,\"typeKey\":\"track_running\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(8L, "track_running", ActivityType.TrackRunning))
    }

    @Test
    fun `Deserialize JSON to ActivityType TrailRunning`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":6,\"typeKey\":\"trail_running\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(6L, "trail_running", ActivityType.TrailRunning))
    }

    @Test
    fun `Deserialize JSON to ActivityType TreadmillRunning`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":18,\"typeKey\":\"treadmill_running\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(18L, "treadmill_running", ActivityType.TreadmillRunning))
    }

    @Test
    fun `Deserialize JSON to ActivityType UltraRun`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":181,\"typeKey\":\"ultra_run\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(181L, "ultra_run", ActivityType.UltraRun))
    }

    @Test
    fun `Deserialize JSON to ActivityType Cycling`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":2,\"typeKey\":\"cycling\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(2L, "cycling", ActivityType.Cycling))
    }

    @Test
    fun `Deserialize JSON to ActivityType DownhillBiking`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":20,\"typeKey\":\"downhill_biking\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(20L, "downhill_biking", ActivityType.DownhillBiking))
    }

    @Test
    fun `Deserialize JSON to ActivityType EBiking`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":176,\"typeKey\":\"e_bike_fitness\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(176L, "e_bike_fitness", ActivityType.EBiking))
    }

    @Test
    fun `Deserialize JSON to ActivityType EBikingMountain`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":175,\"typeKey\":\"e_bike_mountain\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(175L, "e_bike_mountain", ActivityType.EBikingMountain))
    }

    @Test
    fun `Deserialize JSON to ActivityType GravelCycling`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":143,\"typeKey\":\"gravel_cycling\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(143L, "gravel_cycling", ActivityType.GravelCycling))
    }

    @Test
    fun `Deserialize JSON to ActivityType MountainBiking`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":5,\"typeKey\":\"mountain_biking\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(5L, "mountain_biking", ActivityType.MountainBiking))
    }

    @Test
    fun `Deserialize JSON to ActivityType RoadBiking`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":10,\"typeKey\":\"road_biking\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(10L, "road_biking", ActivityType.RoadBiking))
    }

    @Test
    fun `Deserialize JSON to ActivityType IndoorRide`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":25,\"typeKey\":\"indoor_cycling\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(25L, "indoor_cycling", ActivityType.IndoorRide))
    }

    @Test
    fun `Deserialize JSON to ActivityType VirtualRide`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":152,\"typeKey\":\"virtual_ride\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(152L, "virtual_ride", ActivityType.VirtualRide))
    }

    @Test
    fun `Deserialize JSON to ActivityType HIIT`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":180,\"typeKey\":\"hiit\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(180L, "hiit", ActivityType.HIIT))
    }

    @Test
    fun `Deserialize JSON to ActivityType Breathwork`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":164,\"typeKey\":\"breathwork\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(164L, "breathwork", ActivityType.Breathwork))
    }

    @Test
    fun `Deserialize JSON to ActivityType Cardio`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":11,\"typeKey\":\"indoor_cardio\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(11L, "indoor_cardio", ActivityType.Cardio))
    }

    @Test
    fun `Deserialize JSON to ActivityType JumpRope`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":254,\"typeKey\":\"jump_rope\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(254L, "jump_rope", ActivityType.JumpRope))
    }

    @Test
    fun `Deserialize JSON to ActivityType StrengthTraining`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":13,\"typeKey\":\"strength_training\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(13L, "strength_training", ActivityType.StrengthTraining))
    }

    @Test
    fun `Deserialize JSON to ActivityType Yoga`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":163,\"typeKey\":\"yoga\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(163L, "yoga", ActivityType.Yoga))
    }

    @Test
    fun `Deserialize JSON to ActivityType PoolSwimming`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":27,\"typeKey\":\"lap_swimming\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(27L, "lap_swimming", ActivityType.PoolSwimming))
    }

    @Test
    fun `Deserialize JSON to ActivityType OpenWaterSwimming`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":28,\"typeKey\":\"open_water_swimming\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(28L, "open_water_swimming", ActivityType.OpenWaterSwimming))
    }

    @Test
    fun `Deserialize JSON to ActivityType Swimming`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":26,\"typeKey\":\"swimming\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(26L, "swimming", ActivityType.Swimming))
    }

    @Test
    fun `Deserialize JSON to ActivityType Multisport`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":89,\"typeKey\":\"multi_sport\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(89L, "multi_sport", ActivityType.Multisport))
    }

    @Test
    fun `Deserialize JSON to ActivityType Walking`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":9,\"typeKey\":\"walking\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(9L, "walking", ActivityType.Walking))
    }

    @Test
    fun `Deserialize JSON to ActivityType Hiking`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":3,\"typeKey\":\"hiking\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(3L, "hiking", ActivityType.Hiking))
    }

    @Test
    fun `Deserialize JSON to ActivityType Snowboarding`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":252,\"typeKey\":\"resort_snowboarding\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(252L, "resort_snowboarding", ActivityType.Snowboarding))
    }

    @Test
    fun `Deserialize JSON to ActivityType Kayaking`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":231,\"typeKey\":\"kayaking_v2\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(231L, "kayaking_v2", ActivityType.Kayaking))
    }

    @Test
    fun `Deserialize JSON to ActivityType StandUpPaddling`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":239,\"typeKey\":\"stand_up_paddleboarding_v2\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(239L, "stand_up_paddleboarding_v2", ActivityType.StandUpPaddling))
    }

    @Test
    fun `Deserialize JSON to ActivityType Surfing`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":240,\"typeKey\":\"surfing_v2\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(240L, "surfing_v2", ActivityType.Surfing))
    }

    @Test
    fun `Deserialize JSON to ActivityType Windsurf`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":242,\"typeKey\":\"windsurfing_v2\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(242L, "windsurfing_v2", ActivityType.Windsurf))
    }

    @Test
    fun `Deserialize unknown JSON to ActivityType Unknown`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":-1,\"typeKey\":\"unknown\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(-1L, "unknown", ActivityType.Unknown))
    }

    @Test
    fun `Deserialize unsupported JSON to ActivityType Unknown`() {
        val res = json.decodeFromString(ActivityTypeSerializer, "{\"typeId\":999,\"typeKey\":\"nope\"}")

        assertThat(res).isEqualTo(ActivityTypeDto(999L, "nope", ActivityType.Unknown))
    }
}

