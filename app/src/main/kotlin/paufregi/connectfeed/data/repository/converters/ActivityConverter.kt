package paufregi.connectfeed.data.repository.converters

import paufregi.connectfeed.core.models.Activity
import kotlin.math.round
import kotlin.time.Instant
import paufregi.connectfeed.data.api.garmin.models.ActivityDto as GarminActivity
import paufregi.connectfeed.data.api.strava.models.ActivityDto as StravaActivity

fun GarminActivity.toCore() = Activity(
    id = id,
    name = name,
    type = type.toCore(),
    eventType = eventType?.toCore(),
    distance = round(distance),
    trainingEffect = when (trainingEffectLabel) {
        "SPEED" -> "sprint"
        "ANAEROBIC_CAPACITY" -> "anaerobic capacity"
        "VO2MAX" -> "VO₂ max"
        "LACTATE_THRESHOLD" -> "threshold"
        "TEMPO" -> "tempo"
        "AEROBIC_BASE" -> "base"
        "RECOVERY" -> "recovery"
        else -> null
    },
    date = beginTimestamp?.let { Instant.fromEpochMilliseconds(it) },
    workoutId = workoutId
)

fun StravaActivity.toCore() = Activity(
    id = id,
    name = name,
    distance = round(this.distance),
    type = sportType.toActivityType(),
    date = this.startDate?.let { Instant.parse(it) }
)
