package ui.components.checkbox

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ui.theme.animationDuration

@Composable
fun SquareCheckbox(
   modifier: Modifier = Modifier,
   fillPercent: Float = 1.0f,
   color: Color,
   size: Dp = 34.dp,
   onClick: () -> Unit = {}
) {
   val fillPercentAnimated by animateFloatAsState(
      targetValue = fillPercent,
      animationSpec = tween(MaterialTheme.animationDuration)
   )



   Box(
      modifier = modifier
         .clip(RoundedCornerShape(2.dp))
         .clickable {
            onClick()
         }
         .rotate(45f),
      contentAlignment = Alignment.Center
   ) {
      Canvas(modifier = Modifier.size(size)) {
         drawPath(
            path = Path().apply {
               polygon(4, this@Canvas.size.maxDimension / 2, center)
            },
            color = color,
            style = Stroke(
               width = 2.dp.toPx(),
               pathEffect = PathEffect.cornerPathEffect(4.dp.toPx())
            )
         )
      }

      Canvas(modifier = Modifier.size(((size - 8.dp) * fillPercentAnimated))) {
         drawPath(
            path = Path().apply {
               polygon(4, (this@Canvas.size.maxDimension / 2), center)
            },
            color = color,
            style = Stroke(
               width = 1.dp.toPx(),
               pathEffect = PathEffect.cornerPathEffect(1.dp.toPx())
            )
         )
         drawPath(
            path = Path().apply {
               polygon(4, (this@Canvas.size.maxDimension / 2), center)
            },
            color = color,
         )
      }
   }
}
