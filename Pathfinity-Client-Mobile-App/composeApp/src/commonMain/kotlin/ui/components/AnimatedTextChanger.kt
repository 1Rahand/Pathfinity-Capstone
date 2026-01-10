package ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.delay

@Composable
fun AnimatedTextChanger(
   text: String,
   modifier: Modifier = Modifier,
   textStyle: TextStyle = MaterialTheme.typography.labelMedium,
   delayMillis: Long = 10L // Delay between character changes
) {
   var displayText by remember { mutableStateOf(text) }
   var currentText by remember { mutableStateOf(text) }
   var isErasing by remember { mutableStateOf(false) }

   LaunchedEffect(text) {
      if (text != currentText) {
         isErasing = true
         for (i in currentText.length downTo 0) {
            displayText = currentText.substring(0, i)
            delay(delayMillis)
         }
         isErasing = false
         currentText = text
         for (i in text.indices) {
            displayText = text.substring(0, i + 1)
            delay(delayMillis)
         }
      }
   }

   Text(text = displayText, style = textStyle, modifier = modifier)
}