package paufregi.connectfeed.presentation.strava

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.usecases.SaveStravaCode
import paufregi.connectfeed.core.usecases.SyncWeight
import paufregi.connectfeed.presentation.syncweight.SyncWeightState
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class StravaViewModel @Inject constructor(
    private val saveStravaCode: SaveStravaCode
) : ViewModel() {

    fun saveCode(code: String) = viewModelScope.launch {
        saveStravaCode(code)
    }
}
