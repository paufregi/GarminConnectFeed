package paufregi.connectfeed.core.utils

import org.apache.commons.csv.CSVFormat
import paufregi.connectfeed.core.models.Weight
import java.io.InputStream
import java.util.Locale

object RenphoReader {
    fun read(inputStream: InputStream): List<Weight> {
        return CSVFormat.Builder.create(CSVFormat.DEFAULT).apply {
            setIgnoreSurroundingSpaces(true)
        }.get().parse(inputStream.reader())
            .drop(1)
            .mapNotNull { record ->
                val timestamp = Formatter.dateTimeForImport(Locale.getDefault()).parse("${record[0]} ${record[1]}")
                timestamp?.let {
                    Weight(
                        timestamp = it,
                        weight = record[2].toFloat(),
                        bmi = record[3].toFloat(),
                        fat = record[4].toFloat(),
                        visceralFat = record[8].toFloat().toInt().toShort(),
                        water = record[9].toFloat(),
                        muscle = record[10].toFloat(),
                        bone = record[11].toFloat(),
                        basalMet = record[13].toFloat(),
                        metabolicAge = record[14].toShort(),
                    )
                }
            }
    }
}