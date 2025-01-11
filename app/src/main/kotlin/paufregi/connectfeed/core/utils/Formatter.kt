package paufregi.connectfeed.core.utils

import android.annotation.SuppressLint
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

    val dateTimeForImport = { locale: Locale -> SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale) }

    @SuppressLint("DefaultLocale")
    val distance = { distance: Double -> String.format("%.2f", distance/1000) }
}