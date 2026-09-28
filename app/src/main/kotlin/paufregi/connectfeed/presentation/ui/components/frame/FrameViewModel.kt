package paufregi.connectfeed.presentation.ui.components.frame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import paufregi.connectfeed.core.usecases.GetUser
import javax.inject.Inject

@HiltViewModel
class FrameViewModel @Inject constructor(
    getUser: GetUser
) : ViewModel() {

    val user = getUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
}
