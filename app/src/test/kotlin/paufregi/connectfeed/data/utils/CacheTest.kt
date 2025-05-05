package paufregi.connectfeed.data.utils

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.Instant

class CacheTest {

    private lateinit var cache: Cache<String>

    private val time1 = Instant.parse("2025-01-01T00:00:00Z")
    private val time2 = Instant.parse("2025-01-01T00:02:30Z")
    private val time3 = Instant.parse("2025-01-01T00:05:01Z")

    @Before
    fun setUp() {
        cache = Cache<String>()
        mockkStatic(Instant::class)
    }

    @After
    fun tearDown() {
        unmockkStatic(Instant::class)
    }

    @Test
    fun `Cache value`() = runTest {
        every { Instant.now() } returns time1
        val result = cache.get { "fetched data" }

        assertThat(result).isEqualTo("fetched data")
    }

    @Test
    fun `Retrieve cached data`() = runTest {
        every { Instant.now() } returns time1 andThen time2
        val result1 = cache.get { "fetched data" }
        val result2 = cache.get { "new data" }

        assertThat(result1).isEqualTo("fetched data")
        assertThat(result2).isEqualTo("fetched data")
    }

    @Test
    fun `Expired cache`() = runTest {
        every { Instant.now() } returns time1 andThen time3
        val result1 = cache.get { "fetched data" }
        val result2 = cache.get { "new data" }

        assertThat(result1).isEqualTo("fetched data")
        assertThat(result2).isEqualTo("new data")
    }

    @Test
    fun `Invalidate cache`() = runTest {
        every { Instant.now() } returns time1 andThen time2
        val result1 = cache.get { "fetched data" }
        cache.invalidate()
        val result2 = cache.get { "new data" }

        assertThat(result1).isEqualTo("fetched data")
        assertThat(result2).isEqualTo("new data")
    }




}