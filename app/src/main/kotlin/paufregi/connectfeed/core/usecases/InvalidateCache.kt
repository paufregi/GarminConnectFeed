package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.data.repository.GarminRepository
import javax.inject.Inject

class InvalidateCache @Inject constructor(private val repository: GarminRepository) {
    operator fun invoke(): Unit = repository.invalidateCaches()
}