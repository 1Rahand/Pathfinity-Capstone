package ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import domain.Lang
import ui.components.environment.currentAppLang

@Composable
fun RotativeChevron(
   modifier: Modifier = Modifier,
   isExpanded: Boolean = false,
   onClick: (() -> Unit)? = null,
   color : Color = MaterialTheme.colorScheme.onBackground
) {
   val rotationOff = if (currentAppLang() == Lang.Eng) 0f else 180f
   val rotationOn = if (currentAppLang() == Lang.Eng) 90f else 90f

   val rotation by animateFloatAsState(if (isExpanded) rotationOn else rotationOff, label = "")
   if (onClick == null) {
      Icon(
         imageVector = Icons.Rounded.ChevronRight,
         contentDescription = null,
         modifier = modifier
            .rotate(rotation),
         tint = color
      )
   } else {
      IconButton(
         modifier = modifier,
         onClick = onClick,
         colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.Transparent,
            contentColor = color
         ),
      ) {
         Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            modifier = Modifier
               .rotate(rotation)
         )
      }
   }
}