package paufregi.connectfeed.core.utils

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.Profile

fun Activity?.getOrMatch(other: Activity, pool: List<Activity>): Activity? =
    if(this == null || this.type != other.type) pool.find { it.match(other) } else this


fun Profile?.getOrNull(activity: Activity): Profile? =
    if(this?.activityType != activity.type) null else this

fun Course?.getOrNull(activity: Activity): Course? =
    if(this?.type != activity.type) null else this