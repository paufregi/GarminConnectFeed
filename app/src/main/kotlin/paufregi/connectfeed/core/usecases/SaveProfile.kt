package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.flow.firstOrNull
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.data.repository.AppRepository
import paufregi.connectfeed.data.repository.AuthRepository
import javax.inject.Inject

class SaveProfile @Inject constructor(
    private val authRepo: AuthRepository,
    private val repo: AppRepository
) {
    suspend operator fun invoke(profile: Profile): Result<Unit> =
        authRepo.getUser().firstOrNull()?.let { user ->
            if (profile.name.isBlank()) return Result.failure(Exception("Name cannot be empty"))
            profile.course?.let {
                if (!profile.type.allowCourse) return Result.failure(Exception("Can't have course for ${profile.type.name} activity type"))
                if (!profile.type.compatible(it.type)) return Result.failure(Exception("Course not compatible with profile"))
            }

            repo.saveProfile(user, profile)
            return Result.success(Unit)
        } ?: Result.failure(Exception("User must be logged in"))
}