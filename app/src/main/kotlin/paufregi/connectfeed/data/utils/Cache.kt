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

    suspend fun get(force: Boolean = false, fetch: suspend () -> T): T =
        semaphore.withPermit {
            val now = Instant.now()
            if (force || data == null || this.timestamp == null || Duration.between(this.timestamp, now) > ttl) {
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

suspend fun <T> withCache(cache: Cache<T>, force: Boolean = false, action: suspend () -> T): T = cache.get(force) { action() }
