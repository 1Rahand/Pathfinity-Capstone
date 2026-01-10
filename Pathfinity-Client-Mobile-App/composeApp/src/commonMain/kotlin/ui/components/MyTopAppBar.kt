package ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import ui.components.buttons.BackButton
import ui.components.environment.currentAppLang

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
   modifier: Modifier = Modifier,
   title: String,
   actions: @Composable RowScope.() -> Unit = {},
   onBackClick: () -> Unit,
) {
   TopAppBar(
      modifier = modifier,
      title = {
         Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(textDirection = currentAppLang().textDirection()),
            fontWeight = FontWeight.Bold
         )
      },
      navigationIcon = {
         BackButton { onBackClick() }
      },
      colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
      actions = actions
   )
}