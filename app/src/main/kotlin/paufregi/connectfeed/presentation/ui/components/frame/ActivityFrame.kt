package paufregi.connectfeed.presentation.ui.components.frame

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import paufregi.connectfeed.core.models.User

@Composable
@ExperimentalMaterial3Api
fun ActivityFrame(
    title: String,
    testTag: String,
    user: User? = null,
    bottomContent: @Composable () -> Unit = { },
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = Modifier.testTag(testTag),
        topBar = {
            TopAppBar(
                title = { Text(title, modifier = Modifier.padding(start = 20.dp)) },
                actions = {
                    user?.let {
                        AsyncImage(
                            model = user.profileImageUrl.takeIf { it.isNotEmpty() },
                            contentDescription = user.name,
                            fallback = rememberVectorPainter(Icons.Default.Person),
                            modifier = Modifier
                                .padding(end = 14.dp)
                                .size(46.dp)
                                .clip(CircleShape)
                        )
                    }
                }
            )
        },
        bottomBar = bottomContent,
        content = content,
    )
}
