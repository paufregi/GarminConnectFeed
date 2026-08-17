package paufregi.connectfeed.data.api.garmin.converters

object TrainingEffectConverter {
    fun convert(trainingEffect: String?): String? = when (trainingEffect) {
        "SPEED" -> "sprint"
        "ANAEROBIC_CAPACITY" -> "anaerobic capacity"
        "VO2MAX" -> "VO₂ max"
        "LACTATE_THRESHOLD" -> "threshold"
        "TEMPO" -> "tempo"
        "AEROBIC_BASE" -> "base"
        "RECOVERY" -> "recovery"
        else -> null
    }
}