package paufregi.connectfeed.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import paufregi.connectfeed.data.database.entities.GearEntity
import paufregi.connectfeed.data.database.entities.ProfileEntity

@Dao
interface GarminDao {

    @Upsert
    suspend fun saveProfile(profile: ProfileEntity)

    @Delete
    suspend fun deleteProfile(profile: ProfileEntity)

    @Query("SELECT * FROM profiles WHERE userId = :userId ORDER BY type, name")
    fun getAllProfiles(userId: Long): Flow<List<ProfileEntity>>

    @Query("SELECT * FROM profiles WHERE id = :id AND userId = :userId")
    suspend fun getProfile(userId: Long, id: Long): ProfileEntity?

    @Upsert
    suspend fun saveGear(gear: GearEntity)

    @Upsert
    suspend fun saveGears(gears: List<GearEntity>)

    @Delete
    suspend fun deleteGear(gear: GearEntity)

    @Query("DELETE FROM gears WHERE userId = :userId")
    suspend fun deleteGears(userId: Long)

    /** Atomically replaces the user's gear list so observers never see a partially synced table. */
    @Transaction
    suspend fun replaceGears(userId: Long, gears: List<GearEntity>) {
        deleteGears(userId)
        saveGears(gears)
    }

    @Query("SELECT * FROM gears WHERE userId = :userId ORDER BY type, name")
    fun getAllGears(userId: Long): Flow<List<GearEntity>>
}
