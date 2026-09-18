package paufregi.connectfeed.core.utils

import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object Formatter {
    val dateTimeForFilename = { zoneId: ZoneId ->
        DateTimeFormatter
            .ofPattern("yyyyMMdd_hhmmss")
            .withZone(zoneId)
    }

    val dateTimeForImport = { locale: Locale -> SimpleDateFormat("yyyy.MM.dd HH:mm:ss", locale) }

    fun distance(distance: Double): String =
        "%,.2f".format(Locale.getDefault(), distance / 1000)

    fun distance(distance: Int): String =
        "%,d".format(Locale.getDefault(), distance / 1000)

    fun garminDescription(workout: String?) =
        workout?.let { "Workout: ${it.lowercase().vo2max()}" }

    fun stravaDescription(
        description: String?,
        trainingEffect: String?,
        workout: String?,
    ): String =
        buildList {
            description?.let {
                add(it)
                if (workout != null || trainingEffect != null) add("")
            }

            workout?.let { add("Workout: ${it.lowercase().vo2max()}") }
            trainingEffect?.let {
                val label = when (it) {
                    "SPEED" -> "sprint"
                    "ANAEROBIC_CAPACITY" -> "anaerobic capacity"
                    "LACTATE_THRESHOLD" -> "threshold"
                    "AEROBIC_BASE" -> "base"
                    else -> it
                }
                add("Benefit: ${label.lowercase().vo2max()}")
            }
        }.joinToString("\n")
}