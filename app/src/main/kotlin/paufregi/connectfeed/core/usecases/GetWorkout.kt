package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Workout
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class GetWorkout @Inject constructor(private val garminRepository: GarminRepository) {
    suspend operator fun invoke(id: Long, force: Boolean = false): Result<Workout> =
        garminRepository.getWorkout(id, force)
}
