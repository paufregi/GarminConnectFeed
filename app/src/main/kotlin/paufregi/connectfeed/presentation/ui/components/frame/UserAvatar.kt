package paufregi.connectfeed.presentation.ui.components.frame

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import paufregi.connectfeed.core.models.User

@Composable
fun UserAvatar(user: User?) {
    user?.let {
        AsyncImage(
            model = it.profileImageUrl.takeIf { url -> url.isNotEmpty() },
            contentDescription = it.name,
            fallback = rememberVectorPainter(Icons.Default.Person),
            modifier = Modifier
                .padding(end = 14.dp)
                .size(46.dp)
                .clip(CircleShape)
        )
    }
}

