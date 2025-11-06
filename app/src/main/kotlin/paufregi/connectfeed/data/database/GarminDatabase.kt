package paufregi.connectfeed.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import paufregi.connectfeed.data.database.coverters.ActivityTypeConverter
import paufregi.connectfeed.data.database.coverters.EventTypeConverter
import paufregi.connectfeed.data.database.coverters.ProfileTypeConverter
import paufregi.connectfeed.data.database.entities.ProfileEntity

@Database(
    entities = [ProfileEntity::class],
    version = 1,
    autoMigrations = []
)
@TypeConverters(ActivityTypeConverter::class, ProfileTypeConverter::class, EventTypeConverter::class)
abstract class GarminDatabase : RoomDatabase() {
    abstract fun garminDao(): GarminDao
}