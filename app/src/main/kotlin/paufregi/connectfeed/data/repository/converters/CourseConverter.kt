package paufregi.connectfeed.data.repository.converters

import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.data.api.garmin.models.CourseDto
import kotlin.math.round

fun CourseDto.toCore() = Course(
    id = id,
    name = name,
    distance = round(distance),
    type = type.toCore()
)