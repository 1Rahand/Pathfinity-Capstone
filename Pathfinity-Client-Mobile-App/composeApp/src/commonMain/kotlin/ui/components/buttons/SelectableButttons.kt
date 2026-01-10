package ui.components.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SelectableCircleButton(
   modifier: Modifier = Modifier,
   isSelected: Boolean,
   text: String,
   onClick: (String) -> Unit
) {
   val backgroundColor by animateColorAsState(if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.background)
   val textColor by animateColorAsState(if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onBackground)

   OutlinedCard(
      modifier = modifier.size(40.dp),
      shape = CircleShape,
      onClick = {
         onClick(text)
      },
      colors = CardDefaults.outlinedCardColors().copy(containerColor = backgroundColor)
   ) {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
         Text(text = text, color = textColor)
      }
   }
}

@Composable
fun SelectableButton(
   modifier: Modifier = Modifier,
   isSelected: Boolean,
   text: String,
   onClick: (String) -> Unit
) {
   val backgroundColor by animateColorAsState(if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.background)
   val textColor by animateColorAsState(if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onBackground)

   OutlinedButton(
      modifier = modifier,
      onClick = {
         onClick(text)
      },
      colors = ButtonDefaults.outlinedButtonColors(contentColor = textColor, containerColor = backgroundColor)
   ) {
      Text(text = text, color = textColor)
   }
}