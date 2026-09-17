package paufregi.connectfeed.data.api.garmin.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import paufregi.connectfeed.core.models.EventType

object EventTypeSerializer : KSerializer<EventType> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("EventType") {
        element<Long>("typeId")
        element<String>("typeKey")
    }

    override fun serialize(encoder: Encoder, value: EventType) =
        encoder.encodeStructure(descriptor) {
            encodeLongElement(descriptor, 0, value.id)
            encodeStringElement(descriptor, 1, value.key)
        }

    override fun deserialize(decoder: Decoder): EventType =
        decoder.decodeStructure(descriptor) {
            var id = 0L

            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> id = decodeLongElement(descriptor, 0)
                    1 -> decodeStringElement(descriptor, 1)
                    -1 -> break
                    else -> error("Unexpected index: $index")
                }
            }

            when (id) {
                1L -> EventType.Race
                2L -> EventType.Recreation
                3L -> EventType.SpecialEvent
                4L -> EventType.Training
                5L -> EventType.Transportation
                6L -> EventType.Touring
                7L -> EventType.Geocaching
                8L -> EventType.Fitness
                9L -> EventType.Uncategorized
                else -> EventType.Uncategorized
            }
        }
}