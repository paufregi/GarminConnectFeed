package paufregi.connectfeed.data.utils

import paufregi.connectfeed.core.utils.withPermit
import java.time.Duration
import java.time.Instant
import java.util.concurrent.Semaphore

class Cache<T>(
    private val ttl: Duration = Duration.ofMinutes(5),
) {
    private var data: T? = null
    private var timestamp: Instant? = null
    private val semaphore = Semaphore(1)

    suspend fun get(fetch: suspend () -> T): T =
        semaphore.withPermit {
            val now = Instant.now()
            if (data == null || this.timestamp == null || Duration.between(this.timestamp, now) > ttl) {
                data = fetch()
                timestamp = now
            }

            return data!!
        }

    fun invalidate() =
        semaphore.withPermit {
            data = null
            timestamp = null
        }
}

suspend fun <T> withCache(c: Cache<T>, action: suspend () -> T): T = c.get { action() }
