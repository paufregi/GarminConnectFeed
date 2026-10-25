package paufregi.connectfeed.presentation.ui.components.notification

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    notificationManager: NotificationManager,
) : ViewModel() {
    val messages = notificationManager.messages
}