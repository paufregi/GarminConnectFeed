package paufregi.connectfeed.core.models

sealed class EventType(val id: Long, val key: String, val name: String, val order: Int) {
    data object Race: EventType(1, "race", "Race", 5)
    data object Recreation: EventType(2, "recreation", "Recreation", 4)
    data object SpecialEvent: EventType(3, "specialEvent", "Special Event", 6)
    data object Training: EventType(4, "training", "Training", 7)
    data object Transportation: EventType(5, "transportation", "Transportation", 8)
    data object Touring: EventType(6, "touring", "Touring", 9)
    data object Geocaching: EventType(7, "geocaching", "Geocaching", 2)
    data object Fitness: EventType(8, "fitness", "Fitness", 3)
    data object Uncategorized: EventType(9, "uncategorized", "Uncategorized", 10)
}