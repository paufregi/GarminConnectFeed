package paufregi.connectfeed.data.repository.converters

import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.data.api.garmin.models.ActivityDto
import kotlin.math.round
import kotlin.time.Instant

fun ActivityDto.toCore() = Activity(
    id = id,
    name = name,
    type = type.toCore(),
    eventType = eventType?.toCore(),
    distance = round(distance),
    trainingEffect = trainingEffectLabel,
    date = beginTimestamp?.let { Instant.fromEpochMilliseconds(it) },
    workoutId = workoutId
)