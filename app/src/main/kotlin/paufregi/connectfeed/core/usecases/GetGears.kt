package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class GetGears @Inject constructor(private val garminRepository: GarminRepository) {
    suspend operator fun invoke(): Result<List<Gear>> =
        garminRepository.getGears().map { data ->
            data.sortedWith(compareBy({ it.type }, { it.name }
            ))
        }
}