package paufregi.connectfeed.core.models

import paufregi.connectfeed.core.models.ActivityType.Cycling
import paufregi.connectfeed.core.models.ActivityType.Hiking
import paufregi.connectfeed.core.models.ActivityType.Running
import paufregi.connectfeed.core.models.ActivityType.Walking

enum class GearType(order: Int) {
    Bike(1),
    Shoe(2),
    Unknown(100);

    fun compatible(activityType: ActivityType): Boolean =
        when (this) {
            Bike -> activityType.compatible(Cycling)
            Shoe -> activityType.compatible(Running) || activityType == Walking || activityType == Hiking
            Unknown -> true
        }
}