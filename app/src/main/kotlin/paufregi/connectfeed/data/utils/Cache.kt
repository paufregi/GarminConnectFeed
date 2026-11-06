package paufregi.connectfeed.data.utils

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

class Cache<T>(
    private val ttl: Duration = 5.minutes
) {
    private var data: T? = null
    private var timestamp: Instant? = null
    private val mutex = Mutex()

    suspend fun get(force: Boolean = false, fetch: suspend () -> T): T =
        mutex.withLock {
            val now = Clock.System.now()
            val current = data
            val stamp = timestamp
            if (force || current == null || stamp == null || (now - stamp) > ttl) {
                fetch().also {
                    data = it
                    timestamp = now
                }
            } else {
                current
            }
        }

    suspend fun invalidate() {
        mutex.withLock {
            data = null
            timestamp = null
        }
    }
}
