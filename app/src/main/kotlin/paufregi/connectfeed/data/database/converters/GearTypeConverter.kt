package paufregi.connectfeed.data.database.converters

import androidx.room.TypeConverter
import paufregi.connectfeed.core.models.GearType

class GearTypeConverter {
    @TypeConverter
    fun fromName(type: String): GearType = when (type) {
        GearType.Shoe.name -> GearType.Shoe
        GearType.Bike.name -> GearType.Bike
        else -> GearType.Unknown
    }

    @TypeConverter
    fun toName(type: GearType): String = type.name
}