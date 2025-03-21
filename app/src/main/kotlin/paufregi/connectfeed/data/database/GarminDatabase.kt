package paufregi.connectfeed.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import paufregi.connectfeed.data.database.coverters.ActivityTypeConverter
import paufregi.connectfeed.data.database.coverters.EventTypeConverter
import paufregi.connectfeed.data.database.entities.ProfileEntity

@Database(entities = [ProfileEntity::class], version = 4)
@TypeConverters(ActivityTypeConverter::class, EventTypeConverter::class)
abstract class GarminDatabase : RoomDatabase() {
    abstract fun garminDao(): GarminDao
}