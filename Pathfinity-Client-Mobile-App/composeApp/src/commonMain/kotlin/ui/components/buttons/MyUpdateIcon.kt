package ui.components.buttons

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import ui.components.modifier.myBackground

@Composable
fun MyUpdateIconButton(
   res: DrawableResource,
   modifier: Modifier = Modifier,
   isLoading: Boolean = false,
   onClick: () -> Unit = {},
   tint: Color = MaterialTheme.colorScheme.onSurface,
   containerColor: Color = MaterialTheme.colorScheme.surface,
) {
   val rotationAnimatable = remember { Animatable(0f) }
   var animationCounter by remember { mutableStateOf(0) }

   LaunchedEffect(animationCounter) {
      if (animationCounter != 0) {
         val currentValue = rotationAnimatable.value
         rotationAnimatable.animateTo(
            targetValue = currentValue + 360f,
            animationSpec = tween(1000)
         )
      }
   }

   LaunchedEffect(isLoading){
      if (isLoading){
         while (true){
            animationCounter++
            delay(1000)
         }
      }
   }

   CompositionLocalProvider(LocalContentColor provides tint) {
      Box(
         modifier
            .myBackground(containerColor = containerColor, shape = RoundedCornerShape(8.dp), onClick = onClick)
            .padding(12.dp)
            .size(20.dp),
         contentAlignment = Alignment.Center
      ) {
         Icon(
            painter = painterResource(res),
            contentDescription = null,
            modifier = Modifier
               .size(20.dp)
               .rotate(rotationAnimatable.value % 360),
         )
      }
   }
}