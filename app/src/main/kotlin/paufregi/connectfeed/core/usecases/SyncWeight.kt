package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Weight
import paufregi.connectfeed.core.utils.FitWriter
import paufregi.connectfeed.core.utils.Formatter
import paufregi.connectfeed.core.utils.mapFailure
import paufregi.connectfeed.data.repository.GarminRepository
import java.io.File
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Named

class SyncWeight @Inject constructor(
    private val garminRepository: GarminRepository,
    @param:Named("tempFolder") val folder: File
) {
    suspend operator fun invoke(weights: List<Weight>): Result<Unit> {
        val dateFormatter = Formatter.dateTimeForFilename(ZoneId.systemDefault())
        val filename = "ws_${dateFormatter.format(Instant.now())}.fit"
        val file = File(folder, filename)
        FitWriter.weights(file, weights)
        val res = garminRepository.uploadFile(file)
        file.delete()

        return res.mapFailure { Exception("Failed to upload file") }
    }
}