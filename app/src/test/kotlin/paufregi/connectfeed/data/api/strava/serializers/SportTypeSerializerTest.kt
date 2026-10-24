package paufregi.connectfeed.data.api.strava.serializers

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import org.junit.Test
import paufregi.connectfeed.data.api.strava.models.SportType

class SportTypeSerializerTest {

    val json = Json { encodeDefaults = true }

    @Test
    fun `Serialize SportType Run to JSON`() {
        val res = json.encodeToString(SportTypeSerializer, SportType.Run)

        assertThat(res).isEqualTo("\"Run\"")
    }

    @Test
    fun `Serialize SportType TrailRun to JSON`() {
        val res = json.encodeToString(SportTypeSerializer, SportType.TrailRun)

        assertThat(res).isEqualTo("\"TrailRun\"")
    }

    @Test
    fun `Serialize SportType Ride to JSON`() {
        val res = json.encodeToString(SportTypeSerializer, SportType.Ride)

        assertThat(res).isEqualTo("\"Ride\"")
    }

    @Test
    fun `Serialize SportType MountainBikeRide to JSON`() {
        val res = json.encodeToString(SportTypeSerializer, SportType.MountainBikeRide)

        assertThat(res).isEqualTo("\"MountainBikeRide\"")
    }

    @Test
    fun `Serialize SportType GravelRide to JSON`() {
        val res = json.encodeToString(SportTypeSerializer, SportType.GravelRide)

        assertThat(res).isEqualTo("\"GravelRide\"")
    }

    @Test
    fun `Serialize SportType EBikeRide to JSON`() {
        val res = json.encodeToString(SportTypeSerializer, SportType.EBikeRide)

        assertThat(res).isEqualTo("\"EBikeRide\"")
    }

    @Test
    fun `Serialize SportType EMountainBikeRide to JSON`() {
        val res = json.encodeToString(SportTypeSerializer, SportType.EMountainBikeRide)

        assertThat(res).isEqualTo("\"EMountainBikeRide\"")
    }

    @Test
    fun `Serialize SportType VirtualRide to JSON`() {
        val res = json.encodeToString(SportTypeSerializer, SportType.VirtualRide)

        assertThat(res).isEqualTo("\"VirtualRide\"")
    }

    @Test
    fun `Serialize SportType HIIT to JSON`() {
        val res = json.encodeToString(SportTypeSerializer, SportType.HIIT)

        assertThat(res).isEqualTo("\"HighIntensityIntervalTraining\"")
    }

    @Test
    fun `Serialize SportType Workout to JSON`() {
        val res = json.encodeToString(SportTypeSerializer, SportType.Workout)

        assertThat(res).isEqualTo("\"Workout\"")
    }

    @Test
    fun `Serialize SportType WeightTraining to JSON`() {
        val res = json.encodeToString(SportTypeSerializer, SportType.WeightTraining)

        assertThat(res).isEqualTo("\"WeightTraining\"")
    }

    @Test
    fun `Serialize SportType Yoga to JSON`() {
        val res = json.encodeToString(SportTypeSerializer, SportType.Yoga)

        assertThat(res).isEqualTo("\"Yoga\"")
    }

    @Test
    fun `Serialize SportType Swim to JSON`() {
        val res = json.encodeToString(SportTypeSerializer, SportType.Swim)

        assertThat(res).isEqualTo("\"Swim\"")
    }

    @Test
    fun `Serialize SportType Walk to JSON`() {
        val res = json.encodeToString(SportTypeSerializer, SportType.Walk)

        assertThat(res).isEqualTo("\"Walk\"")
    }

    @Test
    fun `Serialize SportType Hike to JSON`() {
        val res = json.encodeToString(SportTypeSerializer, SportType.Hike)

        assertThat(res).isEqualTo("\"Hike\"")
    }

    @Test
    fun `Serialize SportType Snowboard to JSON`() {
        val res = json.encodeToString(SportTypeSerializer, SportType.Snowboard)

        assertThat(res).isEqualTo("\"Snowboard\"")
    }

    @Test
    fun `Serialize SportType Kayaking to JSON`() {
        val res = json.encodeToString(SportTypeSerializer, SportType.Kayaking)

        assertThat(res).isEqualTo("\"Kayaking\"")
    }

    @Test
    fun `Serialize SportType StandUpPaddling to JSON`() {
        val res = json.encodeToString(SportTypeSerializer, SportType.StandUpPaddling)

        assertThat(res).isEqualTo("\"StandUpPaddling\"")
    }

    @Test
    fun `Serialize SportType Surfing to JSON`() {
        val res = json.encodeToString(SportTypeSerializer, SportType.Surfing)

        assertThat(res).isEqualTo("\"Surfing\"")
    }

    @Test
    fun `Serialize SportType Windsurf to JSON`() {
        val res = json.encodeToString(SportTypeSerializer, SportType.Windsurf)

        assertThat(res).isEqualTo("\"Windsurf\"")
    }

    @Test
    fun `Serialize SportType Soccer to JSON`() {
        val res = json.encodeToString(SportTypeSerializer, SportType.Soccer)

        assertThat(res).isEqualTo("\"Soccer\"")
    }

    @Test
    fun `Serialize SportType Unknown to JSON`() {
        val res = json.encodeToString(SportTypeSerializer, SportType.Unknown)

        assertThat(res).isEqualTo("\"Unknown\"")
    }

    @Test
    fun `Deserialize JSON to SportType Run`() {
        val res = json.decodeFromString(SportTypeSerializer, "\"Run\"")

        assertThat(res).isEqualTo(SportType.Run)
    }

    @Test
    fun `Deserialize JSON to SportType TrailRun`() {
        val res = json.decodeFromString(SportTypeSerializer, "\"TrailRun\"")

        assertThat(res).isEqualTo(SportType.TrailRun)
    }

    @Test
    fun `Deserialize JSON to SportType Ride`() {
        val res = json.decodeFromString(SportTypeSerializer, "\"Ride\"")

        assertThat(res).isEqualTo(SportType.Ride)
    }

    @Test
    fun `Deserialize JSON to SportType MountainBikeRide`() {
        val res = json.decodeFromString(SportTypeSerializer, "\"MountainBikeRide\"")

        assertThat(res).isEqualTo(SportType.MountainBikeRide)
    }

    @Test
    fun `Deserialize JSON to SportType GravelRide`() {
        val res = json.decodeFromString(SportTypeSerializer, "\"GravelRide\"")

        assertThat(res).isEqualTo(SportType.GravelRide)
    }

    @Test
    fun `Deserialize JSON to SportType EBikeRide`() {
        val res = json.decodeFromString(SportTypeSerializer, "\"EBikeRide\"")

        assertThat(res).isEqualTo(SportType.EBikeRide)
    }

    @Test
    fun `Deserialize JSON to SportType EMountainBikeRide`() {
        val res = json.decodeFromString(SportTypeSerializer, "\"EMountainBikeRide\"")

        assertThat(res).isEqualTo(SportType.EMountainBikeRide)
    }

    @Test
    fun `Deserialize JSON to SportType VirtualRide`() {
        val res = json.decodeFromString(SportTypeSerializer, "\"VirtualRide\"")

        assertThat(res).isEqualTo(SportType.VirtualRide)
    }

    @Test
    fun `Deserialize JSON to SportType HIIT`() {
        val res = json.decodeFromString(SportTypeSerializer, "\"HighIntensityIntervalTraining\"")

        assertThat(res).isEqualTo(SportType.HIIT)
    }

    @Test
    fun `Deserialize JSON to SportType Workout`() {
        val res = json.decodeFromString(SportTypeSerializer, "\"Workout\"")

        assertThat(res).isEqualTo(SportType.Workout)
    }

    @Test
    fun `Deserialize JSON to SportType WeightTraining`() {
        val res = json.decodeFromString(SportTypeSerializer, "\"WeightTraining\"")

        assertThat(res).isEqualTo(SportType.WeightTraining)
    }

    @Test
    fun `Deserialize JSON to SportType Yoga`() {
        val res = json.decodeFromString(SportTypeSerializer, "\"Yoga\"")

        assertThat(res).isEqualTo(SportType.Yoga)
    }

    @Test
    fun `Deserialize JSON to SportType Swim`() {
        val res = json.decodeFromString(SportTypeSerializer, "\"Swim\"")

        assertThat(res).isEqualTo(SportType.Swim)
    }

    @Test
    fun `Deserialize JSON to SportType Walk`() {
        val res = json.decodeFromString(SportTypeSerializer, "\"Walk\"")

        assertThat(res).isEqualTo(SportType.Walk)
    }

    @Test
    fun `Deserialize JSON to SportType Hike`() {
        val res = json.decodeFromString(SportTypeSerializer, "\"Hike\"")

        assertThat(res).isEqualTo(SportType.Hike)
    }

    @Test
    fun `Deserialize JSON to SportType Snowboard`() {
        val res = json.decodeFromString(SportTypeSerializer, "\"Snowboard\"")

        assertThat(res).isEqualTo(SportType.Snowboard)
    }

    @Test
    fun `Deserialize JSON to SportType Kayaking`() {
        val res = json.decodeFromString(SportTypeSerializer, "\"Kayaking\"")

        assertThat(res).isEqualTo(SportType.Kayaking)
    }

    @Test
    fun `Deserialize JSON to SportType StandUpPaddling`() {
        val res = json.decodeFromString(SportTypeSerializer, "\"StandUpPaddling\"")

        assertThat(res).isEqualTo(SportType.StandUpPaddling)
    }

    @Test
    fun `Deserialize JSON to SportType Surfing`() {
        val res = json.decodeFromString(SportTypeSerializer, "\"Surfing\"")

        assertThat(res).isEqualTo(SportType.Surfing)
    }

    @Test
    fun `Deserialize JSON to SportType Windsurf`() {
        val res = json.decodeFromString(SportTypeSerializer, "\"Windsurf\"")

        assertThat(res).isEqualTo(SportType.Windsurf)
    }

    @Test
    fun `Deserialize JSON to SportType Soccer`() {
        val res = json.decodeFromString(SportTypeSerializer, "\"Soccer\"")

        assertThat(res).isEqualTo(SportType.Soccer)
    }

    @Test
    fun `Deserialize unknown JSON to SportType Unknown`() {
        val res = json.decodeFromString(SportTypeSerializer, "\"Unknown\"")

        assertThat(res).isEqualTo(SportType.Unknown)
    }

    @Test
    fun `Deserialize unsupported JSON to SportType Unknown`() {
        val res = json.decodeFromString(SportTypeSerializer, "\"Nope\"")

        assertThat(res).isEqualTo(SportType.Unknown)
    }
}