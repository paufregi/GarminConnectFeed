package paufregi.connectfeed.data.database.coverters

import androidx.room.TypeConverter
import paufregi.connectfeed.core.models.ActivityType

class ActivityTypeConverter {
    @TypeConverter
    fun fromName(type: String?): ActivityType = when (type) {
        ActivityType.Any::class.simpleName -> ActivityType.Any
        ActivityType.Running::class.simpleName -> ActivityType.Running
        ActivityType.Cycling::class.simpleName -> ActivityType.Cycling
        ActivityType.Swimming::class.simpleName -> ActivityType.Swimming
        ActivityType.StrengthTraining::class.simpleName -> ActivityType.StrengthTraining
        ActivityType.Fitness::class.simpleName -> ActivityType.Fitness
        ActivityType.Other::class.simpleName -> ActivityType.Other
        else -> ActivityType.Unknown
    }

    @TypeConverter
    fun toName(type: ActivityType?): String? = type?.javaClass?.simpleName
}
