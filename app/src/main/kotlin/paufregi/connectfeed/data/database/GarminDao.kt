package paufregi.connectfeed.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import paufregi.connectfeed.data.database.entities.ProfileEntity

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
}
