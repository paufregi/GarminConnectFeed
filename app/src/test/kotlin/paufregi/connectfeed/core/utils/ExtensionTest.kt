package paufregi.connectfeed.core.utils

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.SerializationException
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response
import java.io.IOException
import java.util.Date
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
    fun `Response - toResult - empty body resolves to Unit`() {
        val resp = Response.success<Unit>(null)

        val result = resp.toResult()

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(Unit)
    }

    @Test
    fun `Response - toResult - empty body without default is failure`() {
        val resp = Response.success<String>(null)

        val result = resp.toResult()

        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Empty response body")
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
    fun `Result - andThen - success`() {
        val result = Result.success("input").andThen { Result.success(it.uppercase()) }

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo("INPUT")
    }

    @Test
    fun `Result - andThen - chained failure`() {
        val result = Result.success("input")
            .andThen { Result.failure<String>("andThen error") }

        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("andThen error")
    }

    @Test
    fun `Result - andThen - short circuit on failure`() {
        val result = Result.failure<String>("source error")
            .andThen { Result.success(it.uppercase()) }

        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("source error")
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
    fun `apiCall - success`() {
        val result = apiCall { Response.success("Success") }

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo("Success")
    }

    @Test
    fun `apiCall - http error`() {
        val result = apiCall<String> { Response.error(400, "fail".toResponseBody()) }

        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("fail")
    }

    @Test
    fun `apiCall - io exception becomes failure`() {
        val result = apiCall<String> { throw IOException("offline") }

        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()).isInstanceOf(IOException::class.java)
        assertThat(result.exceptionOrNull()?.message).isEqualTo("offline")
    }

    @Test
    fun `apiCall - serialization exception becomes failure`() {
        val result = apiCall<String> { throw SerializationException("bad json") }

        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("bad json")
    }

    @Test(expected = CancellationException::class)
    fun `apiCall - cancellation is rethrown`() {
        apiCall<String> { throw CancellationException("cancelled") }
    }

    @Test
    fun `apiCall - empty body - io exception becomes failure`() {
        val result = apiCall(emptyList<String>()) { throw IOException("offline") }

        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("offline")
    }

    @Test
    fun `apiCall - empty body - null body uses default`() {
        val result = apiCall(emptyList<String>()) { Response.success<List<String>>(null) }

        assertThat(result.getOrNull()).isEmpty()
    }

    @Test
    fun `Result - failure with causes - appends reasons`() {
        val result = Result.failure<Unit>("Couldn't update", Exception("garmin down"), Exception("strava down"))

        assertThat(result.exceptionOrNull()?.message).isEqualTo("Couldn't update: garmin down; strava down")
        assertThat(result.exceptionOrNull()?.cause?.message).isEqualTo("garmin down")
    }

    @Test
    fun `Result - failure with causes - skips null and blank`() {
        val result = Result.failure<Unit>("Couldn't update", null, Exception(""), Exception("strava down"))

        assertThat(result.exceptionOrNull()?.message).isEqualTo("Couldn't update: strava down")
        assertThat(result.exceptionOrNull()?.cause?.message).isEqualTo("")
    }

    @Test
    fun `Result - failure with causes - no details`() {
        val result = Result.failure<Unit>("Couldn't update", null)

        assertThat(result.exceptionOrNull()?.message).isEqualTo("Couldn't update")
        assertThat(result.exceptionOrNull()?.cause).isNull()
    }

    @Test
    fun `String - vo2max - lowercase`() {
        val result = "vo2max workout".vo2max()
        assertThat(result).isEqualTo("VO₂ max workout")
    }

    @Test
    fun `String - vo2max - uppercase`() {
        val result = "VO2MAX workout".vo2max()
        assertThat(result).isEqualTo("VO₂ max workout")
    }

    @Test
    fun `String - vo2max - mixed`() {
        val result = "Vo2Max workout".vo2max()
        assertThat(result).isEqualTo("VO₂ max workout")
    }

    @Test
    fun `String - vo2max - subscript unicode`() {
        val result = "vo₂max workout".vo2max()
        assertThat(result).isEqualTo("VO₂ max workout")
    }

    @Test
    fun `String - vo2max - with space`() {
        val result = "vo2 max workout".vo2max()
        assertThat(result).isEqualTo("VO₂ max workout")
    }

    @Test
    fun `String - vo2max - multiple occurrences`() {
        val result = "vo2max and VO2MAX".vo2max()
        assertThat(result).isEqualTo("VO₂ max and VO₂ max")
    }

    @Test
    fun `String - vo2max - no match unchanged`() {
        val result = "workout recovery".vo2max()
        assertThat(result).isEqualTo("workout recovery")
    }

    @Test
    fun `MutableStateFlow - updateIf - true`() {
        val state = MutableStateFlow(1)

        state.updateIf(
            predicate = { true },
            function = { it + 1 }
        )

        assertThat(state.value).isEqualTo(2)
    }

    @Test
    fun `MutableStateFlow - updateIf - false`() {
        val state = MutableStateFlow(1)

        state.updateIf(
            predicate = { false },
            function = { it + 1 }
        )

        assertThat(state.value).isEqualTo(1)
    }
}