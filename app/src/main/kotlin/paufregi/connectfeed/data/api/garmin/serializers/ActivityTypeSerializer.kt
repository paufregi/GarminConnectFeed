package paufregi.connectfeed.data.api.garmin.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import paufregi.connectfeed.data.api.garmin.models.ActivityType
import paufregi.connectfeed.core.models.ActivityType as CoreActivityType

object ActivityTypeSerializer : KSerializer<ActivityType> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ActivityType") {
        element<Long>("typeId")
        element<String>("typeKey")
    }

    override fun serialize(encoder: Encoder, value: ActivityType) =
        encoder.encodeStructure(descriptor) {
            encodeLongElement(descriptor, 0, value.id)
            encodeStringElement(descriptor, 1, value.key)
        }

    override fun deserialize(decoder: Decoder): ActivityType =
        decoder.decodeStructure(descriptor) {
            var id = 0L
            var key = ""

            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> id = decodeLongElement(descriptor, 0)
                    1 -> key = decodeStringElement(descriptor, 1)
                    -1 -> break
                    else -> error("Unexpected index: $index")
                }
            }

            val coreType = when (id) {
                1L -> CoreActivityType.Running
                8L -> CoreActivityType.TrackRunning
                6L -> CoreActivityType.TrailRunning
                18L -> CoreActivityType.TreadmillRunning
                181L -> CoreActivityType.UltraRun
                2L -> CoreActivityType.Cycling
                20L -> CoreActivityType.DownhillBiking
                176L -> CoreActivityType.EBiking
                175L -> CoreActivityType.EBikingMountain
                143L -> CoreActivityType.GravelCycling
                5L -> CoreActivityType.MountainBiking
                10L -> CoreActivityType.RoadBiking
                25L -> CoreActivityType.IndoorRide
                152L -> CoreActivityType.VirtualRide
                180L -> CoreActivityType.HIIT
                164L -> CoreActivityType.Breathwork
                11L -> CoreActivityType.Cardio
                254L -> CoreActivityType.JumpRope
                13L -> CoreActivityType.StrengthTraining
                163L -> CoreActivityType.Yoga
                27L -> CoreActivityType.PoolSwimming
                28L -> CoreActivityType.OpenWaterSwimming
                26L -> CoreActivityType.Swimming
                89L -> CoreActivityType.Multisport
                9L -> CoreActivityType.Walking
                3L -> CoreActivityType.Hiking
                252L -> CoreActivityType.Snowboarding
                231L -> CoreActivityType.Kayaking
                239L -> CoreActivityType.StandUpPaddling
                240L -> CoreActivityType.Surfing
                242L -> CoreActivityType.Windsurf

                else -> CoreActivityType.Unknown
            }

            ActivityType(id = id, key = key, type = coreType)
        }
}