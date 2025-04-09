package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.utils.FitWriter
import paufregi.connectfeed.core.utils.Formatter
import paufregi.connectfeed.core.utils.RenphoReader
import paufregi.connectfeed.data.repository.GarminRepository
import java.io.File
import java.io.InputStream
import java.time.Instant
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject
import javax.inject.Named

class SyncStravaWeight @Inject constructor(
    private val garminRepository: GarminRepository,
    @Named("tempFolder") val folder: File
) {
    suspend operator fun invoke(inputStream: InputStream, today: Instant = Instant.now()): Result<Unit> {
        val weights = RenphoReader.read(inputStream)

        val weight = weights.maxByOrNull { it.timestamp }


        if (weight == null) return Result.Success(Unit)

        return garminRepository.updateStravaProfile(weight.weight)
    }
}