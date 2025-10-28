package paufregi.connectfeed.data.database.coverters

import androidx.room.TypeConverter
import paufregi.connectfeed.core.models.ActivityCategory

class ActivityCategoryConverter {
    @TypeConverter
    fun fromName(type: String?): ActivityCategory = when (type) {
        ActivityCategory.Any.name -> ActivityCategory.Any
        ActivityCategory.Running.name -> ActivityCategory.Running
        ActivityCategory.Cycling.name -> ActivityCategory.Cycling
        ActivityCategory.Swimming.name -> ActivityCategory.Swimming
        ActivityCategory.Strength.name -> ActivityCategory.Strength
        ActivityCategory.Fitness.name -> ActivityCategory.Fitness
        ActivityCategory.Other.name -> ActivityCategory.Other
        else -> ActivityCategory.Other
    }

    @TypeConverter
    fun toName(type: ActivityCategory?): String? = type?.name
}
