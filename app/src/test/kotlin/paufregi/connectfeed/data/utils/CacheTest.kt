package paufregi.connectfeed.data.utils

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.time.Clock
import kotlin.time.Instant

class CacheTest {

    private lateinit var cache: Cache<String>

    private val time1 = Instant.parse("2025-01-01T00:00:00Z")
    private val time2 = Instant.parse("2025-01-01T00:02:30Z")
    private val time3 = Instant.parse("2025-01-01T00:05:01Z")

    @Before
    fun setUp() {
        cache = Cache<String>()
        mockkObject(Clock.System)
    }

    @After
    fun tearDown() {
        cache.invalidate()
        unmockkObject(Clock.System)
    }

    @Test
    fun `Cache value`() = runTest {
        every { Clock.System.now() } returns time1
        val result = cache.get { "fetched data" }

        assertThat(result).isEqualTo("fetched data")
    }

    @Test
    fun `Retrieve cached data`() = runTest {
        every { Clock.System.now() } returns time1 andThen time2
        val result1 = cache.get { "fetched data" }
        val result2 = cache.get { "new data" }

        assertThat(result1).isEqualTo("fetched data")
        assertThat(result2).isEqualTo("fetched data")
    }

    @Test
    fun `Force refresh`() = runTest {
        every { Clock.System.now() } returns time1 andThen time2
        val result1 = cache.get { "fetched data" }
        val result2 = cache.get(true) { "new data" }

        assertThat(result1).isEqualTo("fetched data")
        assertThat(result2).isEqualTo("new data")
    }

    @Test
    fun `Expired cache`() = runTest {
        every { Clock.System.now() } returns time1 andThen time3
        val result1 = cache.get { "fetched data" }
        val result2 = cache.get { "new data" }

        assertThat(result1).isEqualTo("fetched data")
        assertThat(result2).isEqualTo("new data")
    }

    @Test
    fun `Invalidate cache`() = runTest {
        every { Clock.System.now() } returns time1 andThen time2
        val result1 = cache.get { "fetched data" }
        cache.invalidate()
        val result2 = cache.get { "new data" }

        assertThat(result1).isEqualTo("fetched data")
        assertThat(result2).isEqualTo("new data")
    }

    @Test
    fun `withCache utility`() = runTest {
        every { Clock.System.now() } returns time1 andThen time2
        val result1 = withCache(cache) { "fetched data" }
        val result2 = withCache(cache) { "new data" }

        assertThat(result1).isEqualTo("fetched data")
        assertThat(result2).isEqualTo("fetched data")
    }

    @Test
    fun `withCache utility - force refresh`() = runTest {
        every { Clock.System.now() } returns time1 andThen time2
        val result1 = withCache(cache) { "fetched data" }
        val result2 = withCache(cache, true) { "new data" }

        assertThat(result1).isEqualTo("fetched data")
        assertThat(result2).isEqualTo("new data")
    }
}
