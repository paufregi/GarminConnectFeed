package paufregi.connectfeed.core.utils

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.SerializationException
import retrofit2.Response
import java.io.IOException
import java.util.Calendar
import java.util.Date
import kotlin.time.Instant

private val VO2MAX_REGEX = Regex("vo[2₂]\\s*max", RegexOption.IGNORE_CASE)
fun String.vo2max(): String = this.replace(VO2MAX_REGEX, "VO₂ max")

fun Instant.truncatedToSecond(): Instant =
    Instant.fromEpochSeconds(this.epochSeconds)

fun Date.sameDay(other: Date): Boolean {
    val calendar1 = Calendar.getInstance().apply { time = this@sameDay }
    val calendar2 = Calendar.getInstance().apply { time = other }

    return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
}

/**
 * Maps a Retrofit response to a [Result]. A successful response without a body resolves to
 * [emptyBody] when given, to [Unit] when `T` is `Unit`, and is otherwise a failure.
 */
inline fun <reified T> Response<T>.toResult(emptyBody: T? = null): Result<T> {
    if (!isSuccessful) return Result.failure(errorBody()?.string() ?: "Unknown error")

    val value = body() ?: emptyBody ?: if (T::class == Unit::class) Unit as T else null
    return value?.let { Result.success(it) } ?: Result.failure("Empty response body")
}

/**
 * Executes a Retrofit call and folds transport failures (no network, timeouts, malformed
 * payloads) into a [Result] so callers never need try/catch. Cancellation is rethrown.
 */
inline fun <reified T> apiCall(emptyBody: T? = null, call: () -> Response<T>): Result<T> =
    try {
        call().toResult(emptyBody)
    } catch (e: CancellationException) {
        throw e
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: SerializationException) {
        Result.failure(e)
    }

inline fun <T, R> Result<T>.andThen(block: (T) -> Result<R>): Result<R> =
    fold(onSuccess = block, onFailure = { Result.failure(it) })

inline fun <T> Result<T>.finally(block: () -> Unit): Result<T> =
    this.onSuccess { block() }.onFailure { block() }.map { it }

fun <T> Result.Companion.failure(cause: String): Result<T> = failure(Exception(cause))

/** Builds a failure whose message appends the underlying reasons, keeping the first as the cause. */
fun <T> Result.Companion.failure(message: String, vararg causes: Throwable?): Result<T> {
    val details = causes.mapNotNull { it?.message?.takeIf(String::isNotBlank) }.distinct()
    val fullMessage = if (details.isEmpty()) message else "$message: ${details.joinToString("; ")}"
    return failure(Exception(fullMessage, causes.firstNotNullOfOrNull { it }))
}

fun <T> Result<T>.mapFailure(transform: (exception: Throwable) -> Throwable): Result<T> =
    when (val exception = exceptionOrNull()) {
        null -> this
        else -> Result.failure(transform(exception))
    }

fun <R, T> Result<T>.mapOrFailure(transform: (value: T) -> R?): Result<R> {
    val res = this.map(transform).getOrNull()
    return when {
        res != null -> Result.success(res)
        this.isSuccess -> Result.failure("Transformation returned null")
        else -> Result.failure(this.exceptionOrNull() ?: Exception("Unknown error"))
    }
}


inline fun <T> MutableStateFlow<T>.updateIf(predicate: (T) -> Boolean, function: (T) -> T) {
    update { current -> if (predicate(current)) function(current) else current }
}
