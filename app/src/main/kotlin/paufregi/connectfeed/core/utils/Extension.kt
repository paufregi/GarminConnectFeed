package paufregi.connectfeed.core.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import retrofit2.Response
import java.util.Calendar
import java.util.Date
import java.util.concurrent.Semaphore
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

inline fun <reified T> Response<T>.toResult(): Result<T> =
    when (this.isSuccessful) {
        true -> Result.success(this.body() ?: Unit as T)
        false -> Result.failure(Exception(this.errorBody()?.string() ?: "Unknown error"))
    }

fun <T> Response<T>.toResult(emptyBody: T): Result<T> =
    when (this.isSuccessful) {
        true -> Result.success(this.body() ?: emptyBody)
        false -> Result.failure(Exception(this.errorBody()?.string() ?: "Unknown error"))
    }

inline fun <T, R> Result<T>.andThen(block: (T) -> Result<R>): Result<R> =
    fold(onSuccess = block, onFailure = { Result.failure(it) })

fun <T> Result.Companion.failure(cause: String): Result<T> = failure(Exception(cause))

fun <T> Result<T>.mapFailure(transform: (exception: Throwable) -> Throwable): Result<T> =
    when (val exception = exceptionOrNull()) {
        null -> this
        else -> Result.failure<T>(transform(exception))
    }

fun <R, T> Result<T>.mapOrFailure(transform: (value: T) -> R?): Result<R> {
    val res = this.map(transform).getOrNull()
    return when {
        res != null -> Result.success(res)
        this.isSuccess -> Result.failure(Exception("Transformation returned null"))
        else -> Result.failure(this.exceptionOrNull() ?: Exception("Unknown error"))
    }
}

inline fun <T, R> T.runCatchingResult(block: T.() -> Result<R>): Result<R> {
    return runCatching { block() }.fold(
        onSuccess = { it },
        onFailure = { Result.failure(it) }
    )
}

inline fun <T> Semaphore.withPermit(action: () -> T): T {
    acquire()
    return try {
        action()
    } finally {
        release()
    }
}

inline fun <T> MutableStateFlow<T>.updateIf(predicate: (T) -> Boolean, function: (T) -> T) {
    if (predicate(value)) {
        this.update(function)
    }
}
