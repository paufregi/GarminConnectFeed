package paufregi.connectfeed.data.api.strava.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import paufregi.connectfeed.data.api.strava.models.SportType

object SportTypeSerializer : KSerializer<SportType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("SportType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: SportType) =
        encoder.encodeString(value.key)

    override fun deserialize(decoder: Decoder): SportType =
        when (decoder.decodeString()) {
            "Run" -> SportType.Run
            "TrailRun" -> SportType.TrailRun
            "Ride" -> SportType.Ride
            "MountainBikeRide" -> SportType.MountainBikeRide
            "GravelRide" -> SportType.GravelRide
            "EBikeRide" -> SportType.EBikeRide
            "EMountainBikeRide" -> SportType.EMountainBikeRide
            "VirtualRide" -> SportType.VirtualRide
            "HighIntensityIntervalTraining" -> SportType.HIIT
            "Workout" -> SportType.Workout
            "WeightTraining" -> SportType.WeightTraining
            "Yoga" -> SportType.Yoga
            "Swim" -> SportType.Swim
            "Walk" -> SportType.Walk
            "Hike" -> SportType.Hike
            "Snowboard" -> SportType.Snowboard
            "Kayaking" -> SportType.Kayaking
            "StandUpPaddling" -> SportType.StandUpPaddling
            "Surfing" -> SportType.Surfing
            "Windsurf" -> SportType.Windsurf
            "Soccer" -> SportType.Football
            else -> SportType.Unknown
        }
}