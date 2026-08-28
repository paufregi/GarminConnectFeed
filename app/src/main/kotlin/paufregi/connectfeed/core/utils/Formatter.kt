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

    fun workout(workout: String?) =
        workout?.let { "Workout: ${it.lowercase().vo2max()}" }

    fun description(
        description: String?,
        trainingEffect: String?,
        trainingEffectFlag: Boolean,
        workout: String? = null,
    ): String? {
        val details = buildList {
            workout?.let { add(workout(it)) }
            trainingEffect?.let { if (trainingEffectFlag) add("Benefit: $it") }
        }

        if (details.isEmpty()) return description
        if (description.isNullOrEmpty()) return details.joinToString("\n")
        return "$description\n\n${details.joinToString("\n")}"
    }
}