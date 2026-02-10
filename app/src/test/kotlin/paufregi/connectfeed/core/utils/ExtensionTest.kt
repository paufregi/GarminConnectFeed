package paufregi.connectfeed.core.utils

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response
import java.util.Date
import java.util.concurrent.Semaphore
import kotlin.time.Instant
import java.time.Instant as JInstant

class ExtensionTest {

    @Test
    fun `Instant - truncatedToSecond`() {
        val instant = Instant.parse("2025-01-01T10:20:30.456Z")
        val expected = Instant.parse("2025-01-01T10:20:30Z")
        val truncated = instant.truncatedToSecond()
        assertThat(truncated).isEqualTo(expected)
    }

    @Test
    fun `Date - sameDay - same day`() {
        val date = Date.from(JInstant.parse("2025-01-01T10:20:30Z"))
        val other = Date.from(JInstant.parse("2025-01-01T10:22:33Z"))

        val result = date.sameDay(other)
        assertThat(result).isTrue()
    }

    @Test
    fun `Date - sameDay - different day`() {
        val date = Date.from(JInstant.parse("2025-01-01T10:20:30Z"))
        val other = Date.from(JInstant.parse("2025-01-03T10:20:30Z"))

        val result = date.sameDay(other)
        assertThat(result).isFalse()
    }

    @Test
    fun `Response - toResult - success`() {
        val resp = Response.success("Success")

        val result = resp.toResult()

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo("Success")
    }

    @Test
    fun `Response - toResult - success unit`() {
        val resp = Response.success(Unit)

        val result = resp.toResult()

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(Unit)
    }

    @Test
    fun `Response - toResult - failure`() {
        val resp = Response.error<String>(500, "error".toResponseBody())

        val result = resp.toResult()

        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("error")
    }

    @Test
    fun `Response - toResult with empty - success`() {
        val resp = Response.success<String>("body")

        val result = resp.toResult("empty")

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo("body")
    }

    @Test
    fun `Response - toResult with empty - success with null body`() {
        val resp = Response.success<String>(null)

        val result = resp.toResult("empty")

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo("empty")
    }

    @Test
    fun `Response - toResult with empty - failure`() {
        val resp = Response.error<String>(500, "error".toResponseBody())

        val result = resp.toResult()

        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("error")
    }

    @Test
    fun `Result - failure`() {
        val result = Result.failure<String>("error")

        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("error")
    }

    @Test
    fun `Result - mapFailure - success`() {
        val result = Result.success("ok").mapFailure { Exception("new error") }
        assertThat(result.isSuccess).isTrue()
        assertThat(result.exceptionOrNull()?.message).isNull()
    }

    @Test
    fun `Result - mapFailure - failure`() {
        val result = Result.failure<String>("error").mapFailure { Exception("new error") }

        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("new error")
    }

    @Test
    fun `Result - mapOrFailure - success`() {
        val result = Result.success("input").mapOrFailure { it.uppercase() }
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo("INPUT")
    }

    @Test
    fun `Result - mapOrFailure - success with null transform`() {
        val result = Result.success("input").mapOrFailure { null }
        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Transformation returned null")
    }

    @Test
    fun `Result - mapOrFailure - failure`() {
        val result = Result.failure<String>(Exception("fail")).mapOrFailure { it.uppercase() }
        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("fail")
    }

    @Test
    fun `runCatchingResult - success`() {
        val block: () -> Result<String> = { Result.success("Success") }

        val result = runCatchingResult { block() }

        assertThat(result.isSuccess).isTrue()
        assertThat(result).isEqualTo(Result.success("Success"))
    }

    @Test
    fun `runCatchingResult - failure`() {
        val block: () -> Result<String> = { Result.failure<String>("Fail") }

        val result = runCatchingResult { block() }

        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Fail")
    }

    @Test
    fun `runCatchingResult - exception`() {
        val block: () -> Result<String> = { throw Exception("Fail") }

        val result = runCatchingResult { block() }

        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Fail")
    }

    @Test
    fun `Semaphore - withPermit - executes action within permit`() {
        val semaphore = Semaphore(1)
        var result = 0

        semaphore.withPermit { result = 42 }

        assertThat(result).isEqualTo(42)
    }

    @Test
    fun `Semaphore - withPermit - blocks when no permits are available`() = runTest {
        val semaphore = Semaphore(1)
        var actionExecuted = false

        coroutineScope {
            semaphore.acquire()
            val action = async { semaphore.withPermit { actionExecuted = true } }

            assertThat(actionExecuted).isFalse()
            semaphore.release()
            action.await()
            assertThat(actionExecuted).isTrue()
        }
    }
}