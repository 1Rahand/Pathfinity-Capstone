package ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun LiveView(
   modifier: Modifier = Modifier,
   channelName: String,
   uid: String = "0" // Default to "0" for local user
)