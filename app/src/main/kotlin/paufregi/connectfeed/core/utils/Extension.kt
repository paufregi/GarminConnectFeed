package paufregi.connectfeed.core.utils

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityCategory
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.Profile
import retrofit2.Response
import java.util.Calendar
import java.util.Date
import java.util.concurrent.Semaphore

fun Activity?.getOrMatch(other: Activity, pool: List<Activity>): Activity? =
    if(this?.type != other.type) pool.find { it.match(other) } else this

fun Activity?.takeIfCompatible(type: ActivityCategory?): Activity? =
    if(this?.type != type && type != ActivityType.Any) null else this

fun Activity?.takeIfCompatible(profile: Profile?): Activity? =
    this.getOrNull(profile?.activityType)

fun Activity?.takeIfCompatible(course: Course?): Activity? =
    this.getOrNull(course?.type)

fun Profile?.getOrNull(activity: Activity): Profile? =
    if (this?.activityType?.match(activity.type) == true) this else null

fun Course?.takeIfCompatible(activity: Activity): Course? =
    this.takeIf { it != null && ActivityCategory.findCategory(activity).compatibleWith(it.type) }

fun Course?.takeIfCompatible(category: ActivityCategory): Course? =
    this.takeIf { it != null && category.allowCourseInProfile && category.compatibleWith(it.type) }


fun Float?.getOrNull(): Float? =
    if(this == 0f) null else this

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
