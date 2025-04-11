package paufregi.connectfeed.core.utils

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.Profile
import java.util.Calendar
import java.util.Date

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
