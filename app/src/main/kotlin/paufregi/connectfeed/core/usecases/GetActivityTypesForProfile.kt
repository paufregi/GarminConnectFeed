package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.ActivityType
import javax.inject.Inject

class GetActivityTypesForProfile @Inject constructor() {
    operator fun invoke(): List<ActivityType> = listOf(
       ActivityType.Any,
       ActivityType.Running,
       ActivityType.Cycling,
       ActivityType.Swimming,
       ActivityType.StrengthTraining,
       ActivityType.Fitness,
       ActivityType.Other
    ).sortedBy { it.order }
}