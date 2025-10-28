package paufregi.connectfeed.presentation.ui.utils

import androidx.compose.ui.graphics.vector.ImageVector
import paufregi.connectfeed.core.models.ActivityCategory
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.presentation.ui.icons.ConnectIcons
import paufregi.connectfeed.presentation.ui.icons.Cycling
import paufregi.connectfeed.presentation.ui.icons.Fitness
import paufregi.connectfeed.presentation.ui.icons.Other
import paufregi.connectfeed.presentation.ui.icons.Running
import paufregi.connectfeed.presentation.ui.icons.StrengthTraining
import paufregi.connectfeed.presentation.ui.icons.Swimming

object Icon {

    fun forActivityCategory(category: ActivityCategory): ImageVector? =
        when(category) {
            is ActivityCategory.Running -> ConnectIcons.Running
            is ActivityCategory.Cycling -> ConnectIcons.Cycling
            is ActivityCategory.Swimming -> ConnectIcons.Swimming
            is ActivityCategory.Strength -> ConnectIcons.StrengthTraining
            is ActivityCategory.Fitness -> ConnectIcons.Fitness
            is ActivityCategory.Other -> ConnectIcons.Other
            else -> null
        }

    fun forActivityType(type: ActivityType): ImageVector? =
        when(type) {
            is ActivityType.Running -> ConnectIcons.Running
            is ActivityType.Cycling -> ConnectIcons.Cycling
            is ActivityType.Swimming -> ConnectIcons.Swimming
            else -> null
        }

}