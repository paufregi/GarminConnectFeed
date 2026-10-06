package paufregi.connectfeed.presentation.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import paufregi.connectfeed.core.usecases.GetUser
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    val getUser: GetUser,
) : ViewModel() {

    val state = getUser().map { user -> user?.let { AppState.Authenticated(it) } ?: AppState.NotAuthenticated }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), AppState.Loading)
}
