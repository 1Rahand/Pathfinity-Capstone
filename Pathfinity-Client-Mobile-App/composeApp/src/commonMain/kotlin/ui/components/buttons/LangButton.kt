package ui.components.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import ui.components.layout.RowC

@Composable
fun LangButton(
   modifier: Modifier = Modifier,
   isActive: Boolean,
   flag: DrawableResource,
   text: String,
   onClick: () -> Unit,
) {
   val engButtonBackground by animateColorAsState(
      targetValue = if (isActive) MaterialTheme.colorScheme.secondary else Color.Transparent,
      animationSpec = tween(500)
   )

   val engButtonForeground by animateColorAsState(
      targetValue = if (isActive) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onBackground,
      animationSpec = tween(500)
   )

   OutlinedButton(
      modifier = modifier,
      onClick = onClick,
      content = {
         RowC {
            Image(
               painter = painterResource(resource = flag),
               contentDescription = null,
               modifier = Modifier
                  .width(32.dp)
                  .height(22.dp)
                  .clip(RoundedCornerShape(4.dp)),
               contentScale = ContentScale.Crop
            )
            Text(text = text)
         }
      },
      shape = RoundedCornerShape(8.dp),
      colors = ButtonDefaults.outlinedButtonColors(
         contentColor = engButtonForeground,
         containerColor = engButtonBackground
      ),
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f))
   )
}