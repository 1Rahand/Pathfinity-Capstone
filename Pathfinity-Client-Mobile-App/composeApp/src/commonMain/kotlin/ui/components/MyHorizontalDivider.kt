package ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

@Composable
fun MyHorizontalDivider() {
   HorizontalDivider(
      modifier = Modifier
         .padding(horizontal = 16.dp)
         .alpha(0.25F)
   )
}