package ui.platform

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ui.components.layout.ColumnC

@Composable
actual fun LiveView(
   modifier: Modifier,
   channelName: String,
   uid: String
) {
   ColumnC(Modifier.fillMaxSize()) {
      Text(
         "Live View is not implemented for desktop yet."
      )
   }
}