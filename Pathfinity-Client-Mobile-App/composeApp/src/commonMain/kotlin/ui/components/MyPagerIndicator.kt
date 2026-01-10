package ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ui.theme.green

data class Dot(val isCompleted: Boolean)

@Composable
fun OnboardingDotsIndicator(
   totalDots: List<Dot>,
   selectedIndex: Int,
   defaultColor: Color = Color.Gray,
   modifier: Modifier = Modifier,
   dotSize: Dp = 8.dp,
   selectedDotWidth: Dp = 40.dp,
   dotSpacing: Dp = 4.dp
) {
   Row(
      modifier = modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
   ) {
      // Create one dot for each page
      for (i in totalDots.indices) {
         val isSelected = i == selectedIndex
         // Animate the width so that the selected dot expands
         val width by animateDpAsState(
            targetValue = if (isSelected) selectedDotWidth else dotSize,
            animationSpec = tween(durationMillis = 300)
         )

         val color = if (totalDots[i].isCompleted) MaterialTheme.colorScheme.green else defaultColor

         Box(
            modifier = Modifier
               .padding(horizontal = dotSpacing)
               .height(dotSize)
               .width(width)
               .clip(RoundedCornerShape(percent = 50))
               .background(color)
         )
      }
   }
}