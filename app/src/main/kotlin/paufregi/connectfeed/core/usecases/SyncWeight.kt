package paufregi.connectfeed.core.usecases

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import paufregi.connectfeed.core.models.Weight
import paufregi.connectfeed.core.utils.FitWriter
import paufregi.connectfeed.core.utils.Formatter
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.core.utils.runCatchingResult
import paufregi.connectfeed.core.utils.sameDay
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.data.repository.StravaRepository
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject
import javax.inject.Named

class SyncWeight @Inject constructor(
    private val isStravaConnected: IsStravaConnected,
    private val garminRepo: GarminRepository,
    private val stravaRepo: StravaRepository,
    @param:Named("tempFolder") val folder: File
) {
    suspend operator fun invoke(weights: List<Weight>, today: Date = Date()): Result<Unit> = coroutineScope {
        val garminDeferred = async {
            val dateFormatter = Formatter.dateTimeForFilename(ZoneId.systemDefault())
            val filename = "ws_${dateFormatter.format(Instant.now())}.fit"
            val file = File(folder, filename)
            FitWriter.weights(file, weights)
            val res = garminRepo.uploadFile(file)
            file.delete()
            res
        }

        val stravaDeferred = async {
            isStravaConnected().firstOrNull()?.let {
                weights.find { it.timestamp.sameDay(today) }
                    ?.let { stravaRepo.updateAthlete(it.weight) }
            } ?: Result.success(Unit)
        }

        val garminResult = runCatchingResult { garminDeferred.await() }
        val stravaResult = runCatchingResult { stravaDeferred.await() }

        if(garminResult.isFailure && stravaResult.isSuccess) return@coroutineScope Result.failure("Couldn't update Garmin")
        if(garminResult.isSuccess && stravaResult.isFailure) return@coroutineScope Result.failure("Couldn't update Strava")
        if(garminResult.isFailure && stravaResult.isFailure) return@coroutineScope Result.failure("Couldn't update Garmin & Strava")

        Result.success(Unit)
    }
}
