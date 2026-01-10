package ui.utility

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import domain.Gender
import org.jetbrains.compose.resources.painterResource
import pathfinity.composeapp.generated.resources.Res
import pathfinity.composeapp.generated.resources.female
import pathfinity.composeapp.generated.resources.male

@Composable
fun GenderIcon(modifier: Modifier = Modifier, gender: Gender, isActive: Boolean, onClick: () -> Unit) {
   val icon = gender.getIcon()
   val backgroundColor by animateColorAsState(if (isActive) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.background)
   val borderColor by animateColorAsState(
      if (isActive) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground.copy(
         alpha = 0.2f
      )
   )
   val borderWidth by animateDpAsState(if (isActive) 2.dp else 1.dp)

   Image(
      painter = painterResource(icon),
      contentDescription = null,
      modifier = modifier
         .heightIn(max = 100.dp)
         .background(backgroundColor, shape = RoundedCornerShape(8.dp))
         .clip(RoundedCornerShape(8.dp))
         .border(borderWidth, borderColor, shape = RoundedCornerShape(8.dp))
         .clickable { onClick() }
         .padding(top = 8.dp)
   )
}