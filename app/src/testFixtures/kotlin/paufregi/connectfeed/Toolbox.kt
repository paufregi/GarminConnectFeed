package paufregi.connectfeed

import mockwebserver3.RecordedRequest
import paufregi.connectfeed.core.utils.truncatedToSecond
import java.net.URLDecoder
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

val today: Instant = Clock.System.now().truncatedToSecond()
val tomorrow: Instant = today + 1.days
val yesterday: Instant = today - 1.days

fun RecordedRequest.getFields(): Map<String, String> {
    val body = body?.utf8() ?: ""
    val contentType = headers["Content-Type"] ?: ""
    if (body.isEmpty() || contentType != "application/x-www-form-urlencoded") return emptyMap()
    val items = body.split("&")
    return items.associate {
        val (key, value) = it.split("=")
        URLDecoder.decode(key, Charsets.UTF_8) to URLDecoder.decode(value, Charsets.UTF_8)
    }
}
