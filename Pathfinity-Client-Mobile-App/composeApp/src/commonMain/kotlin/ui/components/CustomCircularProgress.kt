package ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomCircularProgress(
   modifier: Modifier = Modifier,
   progress: Float, // 0f to 1f
   color: Color = MaterialTheme.colorScheme.primary,
   strokeWidth: Dp = 4.dp,
   animationDuration: Int = 1000, // optional
   centerContent: @Composable (() -> Unit)? = null
) {
   // 1. Animate the progress
   val animatedProgress by animateFloatAsState(
      targetValue = progress,
      animationSpec = tween(durationMillis = animationDuration)
   )

   val density = LocalDensity.current
   val strokeWidthInFloat = with(density) { strokeWidth.toPx() }

   Box(
      modifier = modifier.aspectRatio(1f),
      contentAlignment = Alignment.Center
   ) {
      Canvas(modifier = Modifier.fillMaxSize()) {
         val size = Size(size.minDimension, size.minDimension)
         val startAngle = -90f
         val sweepAngle = 360f * animatedProgress

         // Draw the background circle
         drawArc(
            color = color.copy(alpha = 0.2f),
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = Offset.Zero,
            size = size,
            style = Stroke(width = strokeWidthInFloat, cap = StrokeCap.Round)
         )

         // Draw the animated progress arc
         drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = Offset.Zero,
            size = size,
            style = Stroke(width = strokeWidthInFloat, cap = StrokeCap.Round)
         )
      }

      // Optional center content
      centerContent?.invoke()
   }
}