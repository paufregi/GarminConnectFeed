package paufregi.connectfeed.data.datastore.serializers

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import paufregi.connectfeed.data.datastore.models.Auth
import java.io.InputStream
import java.io.OutputStream

object AuthSerializer : Serializer<Auth> {
    override val defaultValue: Auth = Auth()

    override suspend fun readFrom(input: InputStream): Auth =
        runCatching {
            Json.decodeFromString(
                Auth.serializer(),
                input.readBytes().decodeToString()
            )
        }.getOrDefault(defaultValue)

    override suspend fun writeTo(t: Auth, output: OutputStream) =
        withContext(Dispatchers.IO) {
                output.write(Json.encodeToString(Auth.serializer(), t).toByteArray())
        }
}