package ui.components.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ColumnC(
   modifier: Modifier = Modifier,
   spacing: Dp = 16.dp,
   content: @Composable ColumnScope.() -> Unit
) {
   Column(
      modifier = modifier,
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(spacing, alignment = Alignment.CenterVertically),
      content = { content() }
   )
}