package ui.components.checkbox

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import ui.theme.animationDuration

@Composable
fun HexagonCheckbox(
   modifier: Modifier = Modifier,
   fillPercent: Float = 1.0f,
   color: Color,
   onClick: () -> Unit = {}
) {
   val fillPercentAnimated by animateFloatAsState(
      targetValue = fillPercent,
      animationSpec = tween(MaterialTheme.animationDuration), label = ""
   )
   val strokeRoundness by animateIntAsState(
      targetValue = (1 * fillPercent).toInt(),
      animationSpec = tween(MaterialTheme.animationDuration), label = ""
   )


   Box(
      modifier = modifier
         .clickable { onClick() }
         .rotate(90f),
      contentAlignment = Alignment.Center
   ) {
      Canvas(modifier = Modifier.size(24.dp)) {
         drawPath(
            path = Path().apply {
               polygon(6, size.maxDimension / 2, center)
            },
            color = color,
            style = Stroke(
               width = 2.dp.toPx(),
               pathEffect = PathEffect.cornerPathEffect(2.dp.toPx())
            )
         )
      }

      Canvas(modifier = Modifier.size((16 * fillPercentAnimated).dp)) {
         drawPath(
            path = Path().apply {
               polygon(6, (size.maxDimension / 2), center)
            },
            color = color,
            style = Stroke(
               width = 1.dp.toPx(),
               pathEffect = PathEffect.cornerPathEffect(strokeRoundness.dp.toPx())
            )
         )
         drawPath(
            path = Path().apply {
               polygon(6, (size.maxDimension / 2), center)
            },
            color = color,
         )
      }

   }


}

