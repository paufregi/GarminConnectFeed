package paufregi.connectfeed.core.utils

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.Profile
import java.time.Instant
import java.time.temporal.ChronoUnit
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

fun Date.sameDay(other: Instant): Boolean =
    this.toInstant().truncatedTo(ChronoUnit.DAYS) == other.truncatedTo(ChronoUnit.DAYS)