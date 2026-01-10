package ui.components.buttons

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pathfinity.composeapp.generated.resources.Res
import pathfinity.composeapp.generated.resources.outline_filter_alt_24
import pathfinity.composeapp.generated.resources.round_filter_alt_24


@Composable
fun FilterButton(
   modifier: Modifier = Modifier,
   isActive: Boolean,
   onClick: () -> Unit
) {

   MyIconButton(
      res = if(isActive) Res.drawable.round_filter_alt_24 else Res.drawable.outline_filter_alt_24,
      modifier = modifier,
      containerColor = if (isActive) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surface,
      contentColor = if (isActive) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurface,
      onClick = onClick
   )

}