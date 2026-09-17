package paufregi.connectfeed.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.database.GarminDao
import paufregi.connectfeed.data.database.coverters.toCore
import paufregi.connectfeed.data.database.coverters.toEntity
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val garminDao: GarminDao,
) {
    fun getAllProfiles(user: User): Flow<List<Profile>> =
        garminDao.getAllProfiles(user.id).map { profiles -> profiles.map { it.toCore() } }

    suspend fun getProfile(id: Long): Profile? =
        garminDao.getProfile(id)?.toCore()

    suspend fun saveProfile(user: User, profile: Profile) =
        garminDao.saveProfile(profile.toEntity(user.id))

    suspend fun deleteProfile(user: User, profile: Profile) =
        garminDao.deleteProfile(profile.toEntity(user.id))

    fun getAllGears(user: User): Flow<List<Gear>> =
        garminDao.getAllGears(user.id).map { gears -> gears.map { it.toCore() } }

    suspend fun getGear(id: String): Gear? =
        garminDao.getGear(id)?.toCore()

    suspend fun saveGear(user: User, gear: Gear) =
        garminDao.saveGear(gear.toEntity(user.id))

    suspend fun deleteGear(user: User, gear: Gear) =
        garminDao.deleteGear(gear.toEntity(user.id))
}

