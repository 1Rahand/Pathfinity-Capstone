package ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MyCard(
   modifier: Modifier = Modifier,
   containerColor: Color = MaterialTheme.colorScheme.surface,
   contentColor: Color = MaterialTheme.colorScheme.onSurface,
   spacing: Dp = 8.dp,
   content: @Composable () -> Unit
) {
   val containerColorAnimated by animateColorAsState(containerColor)
   val contentColorAnimated by animateColorAsState(contentColor)

   Card(
      modifier = modifier,
      colors = CardDefaults.cardColors(
         containerColor = containerColorAnimated,
         contentColor = contentColorAnimated
      )
   ) {
      Row(
         modifier = Modifier
            .heightIn(min = 56.dp)
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .align(Alignment.CenterHorizontally),
         horizontalArrangement = Arrangement.spacedBy(spacing, Alignment.CenterHorizontally),
         verticalAlignment = Alignment.CenterVertically
      ) {
         content()
      }
   }
}