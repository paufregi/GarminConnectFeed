package paufregi.connectfeed.presentation.ui.components.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import paufregi.connectfeed.core.models.User

@Composable
@ExperimentalMaterial3Api
fun ExternalScreen(
    user: User?,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Connect Feed") },
                actions = {
                    user?.let {
                        AsyncImage(
                            model = it.profileImageUrl,
                            contentDescription = it.name,
                            modifier = Modifier
                                .padding(end = 14.dp)
                                .size(46.dp)
                                .clip(CircleShape)
                        )
                    }
                }
            )
        },
        content = { padding -> content(padding)}
    )
}
