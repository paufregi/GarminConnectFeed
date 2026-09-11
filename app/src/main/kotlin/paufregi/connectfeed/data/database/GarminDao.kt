package paufregi.connectfeed.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import paufregi.connectfeed.data.database.entities.GearEntity
import paufregi.connectfeed.data.database.entities.ProfileEntity
import paufregi.connectfeed.data.database.entities.StravaGearEntity

@Dao
interface GarminDao {

    @Upsert
    suspend fun saveProfile(profile: ProfileEntity)

    @Delete
    suspend fun deleteProfile(profile: ProfileEntity)

    @Query("SELECT * FROM profiles WHERE userId = :userId ORDER BY type, name")
    fun getAllProfiles(userId: Long): Flow<List<ProfileEntity>>

    @Query("SELECT * FROM profiles WHERE ID = :id")
    suspend fun getProfile(id: Long): ProfileEntity?

    @Upsert
    suspend fun saveGear(gear: GearEntity)

    @Delete
    suspend fun deleteGear(gear: GearEntity)

    @Query("SELECT * FROM gears WHERE userId = :userId ORDER BY type, name")
    fun getAllGears(userId: Long): Flow<List<GearEntity>>

    @Query("SELECT * FROM gears WHERE ID = :id")
    suspend fun getGear(id: String): GearEntity?

    @Upsert
    suspend fun saveStravaGear(gear: StravaGearEntity)

    @Delete
    suspend fun deleteStravaGear(gear: StravaGearEntity)

    @Query("SELECT * FROM strava_gears WHERE athleteId = :athleteId ORDER BY type, name")
    fun getAllStravaGears(athleteId: Long): Flow<List<StravaGearEntity>>

    @Query("SELECT * FROM strava_gears WHERE id = :id")
    suspend fun getStravaGear(id: String): StravaGearEntity?
}
