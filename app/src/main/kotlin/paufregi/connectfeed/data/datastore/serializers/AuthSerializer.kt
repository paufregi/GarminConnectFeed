package paufregi.connectfeed.data.datastore.serializers

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import paufregi.connectfeed.data.datastore.models.Auth
import java.io.InputStream
import java.io.OutputStream

object AuthSerializer : Serializer<Auth> {
    // Defaults must be written: token issue times are derived on receipt and must survive restarts.
    private val json = Json { encodeDefaults = true }

    override val defaultValue: Auth = Auth()

    override suspend fun readFrom(input: InputStream): Auth =
        runCatching {
            json.decodeFromString(
                Auth.serializer(),
                input.readBytes().decodeToString()
            )
        }.getOrDefault(defaultValue)

    override suspend fun writeTo(t: Auth, output: OutputStream) =
        withContext(Dispatchers.IO) {
                output.write(json.encodeToString(Auth.serializer(), t).toByteArray())
        }
}