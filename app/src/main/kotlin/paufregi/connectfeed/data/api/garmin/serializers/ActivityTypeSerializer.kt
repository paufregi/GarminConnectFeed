package paufregi.connectfeed.data.api.garmin.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.data.api.garmin.models.ActivityTypeDto

object ActivityTypeSerializer : KSerializer<ActivityTypeDto> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ActivityType") {
        element<Long>("typeId")
        element<String>("typeKey")
    }

    override fun serialize(encoder: Encoder, value: ActivityTypeDto) =
        encoder.encodeStructure(descriptor) {
            encodeLongElement(descriptor, 0, value.id)
            encodeStringElement(descriptor, 1, value.key)
        }

    override fun deserialize(decoder: Decoder): ActivityTypeDto =
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
                1L -> ActivityType.Running
                8L -> ActivityType.TrackRunning
                6L -> ActivityType.TrailRunning
                18L -> ActivityType.TreadmillRunning
                181L -> ActivityType.UltraRun
                2L -> ActivityType.Cycling
                20L -> ActivityType.DownhillBiking
                176L -> ActivityType.EBiking
                175L -> ActivityType.EBikingMountain
                143L -> ActivityType.GravelCycling
                5L -> ActivityType.MountainBiking
                10L -> ActivityType.RoadBiking
                25L -> ActivityType.IndoorRide
                152L -> ActivityType.VirtualRide
                180L -> ActivityType.HIIT
                164L -> ActivityType.Breathwork
                11L -> ActivityType.Cardio
                254L -> ActivityType.JumpRope
                13L -> ActivityType.StrengthTraining
                163L -> ActivityType.Yoga
                27L -> ActivityType.PoolSwimming
                28L -> ActivityType.OpenWaterSwimming
                26L -> ActivityType.Swimming
                89L -> ActivityType.Multisport
                9L -> ActivityType.Walking
                3L -> ActivityType.Hiking
                252L -> ActivityType.Snowboarding
                231L -> ActivityType.Kayaking
                239L -> ActivityType.StandUpPaddling
                240L -> ActivityType.Surfing
                242L -> ActivityType.Windsurf

                else -> ActivityType.Unknown
            }

            ActivityTypeDto(id = id, key = key, type = coreType)
        }
}