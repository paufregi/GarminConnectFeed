package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.ActivityCategory
import javax.inject.Inject

class GetActivityCategories @Inject constructor() {
    operator fun invoke(): List<ActivityCategory> {
        return listOf(
            ActivityCategory.Any,
            ActivityCategory.Running,
            ActivityCategory.Cycling,
            ActivityCategory.Swimming,
            ActivityCategory.Strength,
            ActivityCategory.Fitness,
        ).sortedBy { it.order }
    }
}