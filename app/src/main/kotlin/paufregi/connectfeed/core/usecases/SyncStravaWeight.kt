package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.flow.first
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.models.Weight
import paufregi.connectfeed.core.utils.sameDay
import paufregi.connectfeed.data.repository.GarminRepository
import java.util.Date
import javax.inject.Inject

class SyncStravaWeight @Inject constructor(
    private val garminRepository: GarminRepository,
    private val isStravaLoggedIn: IsStravaLoggedIn
) {
    suspend operator fun invoke(
        weights: List<Weight>,
        today: Date = Date()
    ): Result<Unit> {
        if (!isStravaLoggedIn().first()) return Result.Success(Unit)

        val weight = weights.maxByOrNull { it.timestamp }

        if (weight == null || !weight.timestamp.sameDay(today)) return Result.Success(Unit)

        return garminRepository.updateStravaProfile(weight.weight)
    }
}