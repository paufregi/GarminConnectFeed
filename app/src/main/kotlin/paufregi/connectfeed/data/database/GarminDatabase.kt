package paufregi.connectfeed.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import paufregi.connectfeed.data.database.converters.ActivityTypeConverter
import paufregi.connectfeed.data.database.converters.EventTypeConverter
import paufregi.connectfeed.data.database.converters.GearTypeConverter
import paufregi.connectfeed.data.database.entities.GearEntity
import paufregi.connectfeed.data.database.entities.ProfileEntity

@Database(
    entities = [ProfileEntity::class, GearEntity::class],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
@TypeConverters(ActivityTypeConverter::class, EventTypeConverter::class, GearTypeConverter::class)
abstract class GarminDatabase : RoomDatabase() {
    abstract fun garminDao(): GarminDao
}