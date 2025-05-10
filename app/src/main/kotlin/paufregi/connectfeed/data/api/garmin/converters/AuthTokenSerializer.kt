package paufregi.connectfeed.data.api.garmin.converters

import com.auth0.jwt.JWT
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import paufregi.connectfeed.data.api.garmin.models.AuthToken
import java.time.Duration

object AuthTokenSerializer : KSerializer<AuthToken> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("AuthToken") {
        element<String>("access_token")
        element<String>("refresh_token")
        element<Long>("expires_in")
        element<Long>("refresh_token_expires_in")
    }

    override fun deserialize(decoder: Decoder): AuthToken {
        if (decoder !is JsonDecoder) {
            throw SerializationException("This serializer only works with JSON")
        }

        val jsonObject = decoder.decodeJsonElement() as JsonObject
        val accessToken = jsonObject["access_token"]?.jsonPrimitive?.content
            ?: throw SerializationException("Missing access_token")
        val refreshToken = jsonObject["refresh_token"]?.jsonPrimitive?.content
            ?: throw SerializationException("Missing refresh_token")
        val expiresAtOffset = jsonObject["expires_in"]?.jsonPrimitive?.longOrNull
            ?: throw SerializationException("Missing expires_in")
        val refreshExpiresAtOffset = jsonObject["refresh_token_expires_in"]?.jsonPrimitive?.longOrNull
            ?: throw SerializationException("Missing refresh_token_expires_in")

        val issuedAt = JWT.decode(accessToken).issuedAtAsInstant
        return AuthToken(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresAt = issuedAt.plusMillis(expiresAtOffset),
            refreshExpiresAt = issuedAt.plusMillis(refreshExpiresAtOffset)
        )
    }

    override fun serialize(encoder: Encoder, value: AuthToken) {
        val issuedAt = JWT.decode(value.accessToken).issuedAtAsInstant
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.accessToken)
            encodeStringElement(descriptor, 1, value.refreshToken)
            encodeLongElement(descriptor, 2, (Duration.between(issuedAt, value.expiresAt).toMillis()))
            encodeLongElement(descriptor, 3, (Duration.between(issuedAt, value.refreshExpiresAt).toMillis()))
        }
    }
}
