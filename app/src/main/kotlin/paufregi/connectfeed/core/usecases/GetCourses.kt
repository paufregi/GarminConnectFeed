package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class GetCourses @Inject constructor(private val garminRepository: GarminRepository) {
    suspend operator fun invoke(force: Boolean = false): Result<List<Course>> =
        garminRepository.getCourses(force = force).map { data ->
            data.sortedWith(compareBy({ it.type.profileType.order }, { it.name }))
        }
}