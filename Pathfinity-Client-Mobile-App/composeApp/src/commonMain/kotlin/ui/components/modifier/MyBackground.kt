package ui.components.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.myBackground(
   containerColor : Color = MaterialTheme.colorScheme.surface,
   shape : Shape = CardDefaults.shape,
   onClick: (() -> Unit)? = null,
   elevation : Dp = 5.dp,
   shadowColor : Color = Color.Black.copy(alpha = 0.30F),
   borderThickness : Dp = 0.dp,
   borderColor : Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.30F),
) : Modifier {
   return this
      .shadow(elevation , shape , spotColor = shadowColor)
      .then(if(borderThickness > 0.dp) Modifier.border(borderThickness, borderColor , shape) else Modifier)
      .clip(shape)
      .background(containerColor , shape)
      .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
}