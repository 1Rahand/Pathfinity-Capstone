package ui.components.checkbox

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MyCircularCheckbox(
   modifier: Modifier = Modifier,
   fillPercent: Float,
   size: Dp = 24.dp,
   color: Color = Color.Gray,
   strokeWidth: Dp = 1.dp,
   onClick : () -> Unit = {},
) {
   // Animation for the inner circle's radius
   val transitionProgress by animateFloatAsState(targetValue = fillPercent)

   Canvas(modifier = modifier
      .clip(CircleShape)
      .clickable { onClick() }

      .size(size)
   ) {
      val radius = (size.toPx() - strokeWidth.toPx()) / 2

      drawCircle(
         color = color,
         radius = radius,
         style = Stroke(width = strokeWidth.toPx()),
      )

      drawCircle(
         color = color,
         radius = radius * transitionProgress * 0.8f,  // Adjust to avoid full size
      )

   }
}
