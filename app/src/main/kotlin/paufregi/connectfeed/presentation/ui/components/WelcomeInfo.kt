package paufregi.connectfeed.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import paufregi.connectfeed.core.models.User

@Composable
@ExperimentalMaterial3Api
fun WelcomeInfo(
    user: User?,
    action: () -> Unit = { },
    paddingValues: PaddingValues = PaddingValues()
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .testTag("welcome")
    ) {
        AsyncImage(
            model = user?.profileImageUrl ?: "",
            contentDescription = null,
            modifier = Modifier
                .scale(2f)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.size(42.dp))
        Text(text = "Welcome ${user?.name ?: ""}", fontSize = 24.sp)
        Spacer(modifier = Modifier.size(24.dp))
        Button(text = "Ok", onClick = action)
    }
}