package paufregi.connectfeed.data.utils

import paufregi.connectfeed.core.utils.withPermit
import java.util.concurrent.Semaphore
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

class Cache<T>(
    private val ttl: Duration = 5.minutes
) {
    private var data: T? = null
    private var timestamp: Instant? = null
    private val semaphore = Semaphore(1)

    suspend fun get(force: Boolean = false, fetch: suspend () -> T): T =
        semaphore.withPermit {
            val now = Clock.System.now()
            if (force || data == null || this.timestamp == null || (now - this.timestamp!!) > ttl) {
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
