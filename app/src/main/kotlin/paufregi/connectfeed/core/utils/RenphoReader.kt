package paufregi.connectfeed.core.utils

import org.apache.commons.csv.CSVFormat
import paufregi.connectfeed.core.models.Weight
import java.io.InputStream
import java.util.Locale

object RenphoReader {
    fun read(inputStream: InputStream): Result<List<Weight>> = runCatching {
        val formatter = Formatter.dateTimeForImport(Locale.getDefault())
        CSVFormat.Builder.create()
            .setHeader()
            .setSkipHeaderRecord(true)
            .setIgnoreSurroundingSpaces(true)
            .get()
            .parse(inputStream.reader())
            .mapNotNull { record ->
                val timestamp = formatter.parse("${record[1]} ${record[2]}")
                timestamp?.let {
                    Weight(
                        timestamp = it,
                        weight = record[3].toFloat(),
                        bmi = record[4].toFloat(),
                        fat = record[5].toFloat(),
                        visceralFat = record[19].toFloat().toInt().toShort(),
                        water = record[15].toFloat(),
                        muscle = record[8].toFloat(),
                        bone = record[12].toFloat(),
                        basalMet = record[20].toFloat(),
                        metabolicAge = record[21].toShort(),
                    )
                }
            }
    }
}