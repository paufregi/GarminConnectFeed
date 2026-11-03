package paufregi.connectfeed.data.repository.converters

import paufregi.connectfeed.core.models.Workout
import paufregi.connectfeed.data.api.garmin.models.WorkoutDto

fun WorkoutDto.toCore() = Workout(
    id = id,
    name = name,
)