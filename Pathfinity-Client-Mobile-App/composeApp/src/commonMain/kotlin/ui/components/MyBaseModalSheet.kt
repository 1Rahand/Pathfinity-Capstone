package ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import ui.theme.surface2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBaseModalSheet(
   modifier: Modifier = Modifier,
   onDismissRequest: () -> Unit = {},
   sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
   content: @Composable () -> Unit
) {
   val scope = rememberCoroutineScope()


   ModalBottomSheet(
      modifier = modifier.windowInsetsPadding(WindowInsets.displayCutout).windowInsetsPadding(WindowInsets.statusBars),
      sheetState = sheetState,
      containerColor = MaterialTheme.colorScheme.surface2,
      contentColor = MaterialTheme.colorScheme.onBackground,
      onDismissRequest = {
         scope.launch {
            sheetState.hide()
            onDismissRequest()
         }
      }
   ) {

      content()
   }
}