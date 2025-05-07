package paufregi.connectfeed.core.utils

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.Profile
import retrofit2.Response
import java.util.Calendar
import java.util.Date
import java.util.concurrent.Semaphore

fun Activity?.getOrMatch(other: Activity, pool: List<Activity>): Activity? =
    if(this?.type != other.type) pool.find { it.match(other) } else this

fun Activity?.getOrNull(type: ActivityType?): Activity? =
    if(this?.type != type) null else this

fun Activity?.getOrNull(profile: Profile?): Activity? =
    this.getOrNull(profile?.activityType)

fun Activity?.getOrNull(course: Course?): Activity? =
    this.getOrNull(course?.type)

fun Profile?.getOrNull(activity: Activity): Profile? =
    if(this?.activityType != activity.type) null else this

fun Course?.getOrNull(type: ActivityType): Course? =
    if(!type.allowCourseInProfile || this?.type != type) null else this

fun Course?.getOrNull(activity: Activity): Course? =
    this.getOrNull(activity.type)

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

inline fun <T> Semaphore.withPermit(action: () -> T): T {
    acquire()
    return try {
        action()
    } finally {
        release()
    }
}


