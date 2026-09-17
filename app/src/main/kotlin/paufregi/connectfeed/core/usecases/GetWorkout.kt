package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Workout
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class GetWorkout @Inject constructor(private val repo: GarminRepository) {
    suspend operator fun invoke(id: Long): Result<Workout> = repo.getWorkout(id)
}