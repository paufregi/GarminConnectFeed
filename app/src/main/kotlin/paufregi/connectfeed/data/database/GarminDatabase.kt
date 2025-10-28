package paufregi.connectfeed.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import paufregi.connectfeed.data.database.coverters.ActivityCategoryConverter
import paufregi.connectfeed.data.database.coverters.EventTypeConverter
import paufregi.connectfeed.data.database.entities.ProfileEntity

@Database(entities = [ProfileEntity::class], version = 6)
@TypeConverters(ActivityCategoryConverter::class, EventTypeConverter::class)
abstract class GarminDatabase : RoomDatabase() {
    abstract fun garminDao(): GarminDao
}