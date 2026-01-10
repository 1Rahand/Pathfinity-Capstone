package ui.components.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun IpadBox(
   modifier : Modifier = Modifier,
   content : @Composable () -> Unit
){
   Box(
      modifier = modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
   ) {
      Box(
         modifier = modifier.widthIn(max = 600.dp),
         contentAlignment = Alignment.Center
      ) {
         content()
      }
   }
}