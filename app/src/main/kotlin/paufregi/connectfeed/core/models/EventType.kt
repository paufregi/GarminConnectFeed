package paufregi.connectfeed.core.models

sealed class EventType(val id: Long, val key: String, val name: String, val order: Int, val commute: Boolean = false) {
    data object Race: EventType(1, "race", "Race", 5, false)
    data object Recreation: EventType(2, "recreation", "Recreation", 4, false)
    data object SpecialEvent: EventType(3, "specialEvent", "Special Event", 6, false)
    data object Training: EventType(4, "training", "Training", 7, false)
    data object Transportation: EventType(5, "transportation", "Transportation", 8, true)
    data object Touring: EventType(6, "touring", "Touring", 9, false)
    data object Geocaching: EventType(7, "geocaching", "Geocaching", 2, false)
    data object Fitness: EventType(8, "fitness", "Fitness", 3, false)
    data object Uncategorized: EventType(9, "uncategorized", "Uncategorized", 10, false)
}