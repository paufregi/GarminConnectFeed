package paufregi.connectfeed.data.database.coverters

import androidx.room.TypeConverter
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.ProfileType

class ProfileTypeConverter {
    @TypeConverter
    fun fromName(type: String?): ProfileType = when (type) {
        ProfileType.Any::class.simpleName -> ProfileType.Any
        ProfileType.Running::class.simpleName -> ProfileType.Running
        ProfileType.Cycling::class.simpleName -> ProfileType.Cycling
        ProfileType.Swimming::class.simpleName -> ProfileType.Swimming
        ProfileType.Strength::class.simpleName -> ProfileType.Strength
        ProfileType.Fitness::class.simpleName -> ProfileType.Fitness
        ProfileType.Other::class.simpleName -> ProfileType.Any
        else -> ProfileType.Other
    }

    @TypeConverter
    fun toName(type: ProfileType?): String? = type?.javaClass?.simpleName

}
