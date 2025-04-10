package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.utils.RenphoReader
import paufregi.connectfeed.core.utils.sameDay
import paufregi.connectfeed.data.repository.GarminRepository
import java.io.InputStream
import java.time.Instant
import javax.inject.Inject

class SyncStravaWeight @Inject constructor(
    private val garminRepository: GarminRepository,
) {
    suspend operator fun invoke(
        inputStream: InputStream,
        today: Instant = Instant.now()
    ): Result<Unit> {
        val weights = RenphoReader.read(inputStream)

        val weight = weights.maxByOrNull { it.timestamp }

        if (weight == null || !weight.timestamp.sameDay(today)) return Result.Success(Unit)

        return garminRepository.updateStravaProfile(weight.weight)
    }
}