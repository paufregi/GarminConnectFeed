package paufregi.connectfeed.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import paufregi.connectfeed.core.usecases.IsLoggedIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    isLoggedIn: IsLoggedIn
) : ViewModel() {

    val state = isLoggedIn().map { MainState(it)}
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), MainState())
}