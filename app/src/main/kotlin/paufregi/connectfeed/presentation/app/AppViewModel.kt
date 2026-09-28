package paufregi.connectfeed.presentation.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import paufregi.connectfeed.core.usecases.IsLoggedIn
import paufregi.connectfeed.presentation.ui.models.AuthState
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    val isLoggedIn: IsLoggedIn,
) : ViewModel() {

    val state = isLoggedIn().map { if (it) AuthState.Authenticated else AuthState.NotAuthenticated }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), AuthState.Loading)
}
