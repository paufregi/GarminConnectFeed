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
            .setAllowMissingColumnNames(true)
            .setIgnoreHeaderCase(true)
            .get()
            .parse(inputStream.reader())
            .mapNotNull { record ->
                val timestamp = formatter.parse("${record["Date"]} ${record["Time"]}")
                timestamp?.let {
                    Weight(
                        timestamp = it,
                        weight = record["Weight(kg)"].toFloat(),
                        bmi = record["BMI"].toFloat(),
                        fat = record["Body Fat Percentage(%)"].toFloat(),
                        visceralFat = record["Visceral Fat"].toFloat().toInt().toShort(),
                        water = record["Body Water Percentage(%)"].toFloat(),
                        muscle = record["Muscle Mass(kg)"].toFloat(),
                        bone = record["Bone Mass(kg)"].toFloat(),
                        basalMet = record["BMR(kcal)"].toFloat(),
                        metabolicAge = record["Metabolic Age"].toShort(),
                    )
                }
            }
    }
}