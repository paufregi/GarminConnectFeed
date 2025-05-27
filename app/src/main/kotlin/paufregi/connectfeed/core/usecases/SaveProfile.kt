package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.flow.firstOrNull
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.data.repository.AuthRepository
import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class SaveProfile @Inject constructor(
    private val authRepository: AuthRepository,
    private val garminRepository: GarminRepository
) {
    suspend operator fun invoke(profile: Profile): Result<Unit> {
        val user = authRepository.getUser().firstOrNull()
        if (user == null) return Result.failure(Exception("User must be logged in"))
        if (profile.name.isBlank()) return Result.failure(Exception("Name cannot be empty"))
        if (profile.course != null) {
            if (!profile.activityType.allowCourseInProfile) return Result.failure(Exception("Can't have course for ${profile.activityType.name} activity type"))
            if (profile.course.type != profile.activityType) return Result.failure(Exception("Course must match activity type"))
        }

        garminRepository.saveProfile(user, profile)
        return Result.success(Unit)
    }
}