package ui.platform

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun LiveView(
   modifier: Modifier,
   channelName: String,
   uid: String
) {
   Text("Live View is not implemented for iOS yet.")
}