package paufregi.connectfeed.data.api.garmin.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import paufregi.connectfeed.core.models.GearType

object GearTypeSerializer : KSerializer<GearType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("GearType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: GearType) =
        encoder.encodeString(value.name.uppercase())

    override fun deserialize(decoder: Decoder): GearType =
        when (decoder.decodeString()) {
            "SHOE" -> GearType.Shoe
            "BIKE" -> GearType.Bike
            else -> GearType.Unknown
        }
}