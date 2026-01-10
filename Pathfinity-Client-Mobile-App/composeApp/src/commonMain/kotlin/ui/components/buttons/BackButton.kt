package ui.components.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BackButton(
   modifier: Modifier = Modifier,
   enabled: Boolean = true,
   color: Color = MaterialTheme.colorScheme.onBackground,
   onClick: () -> Unit
) {
   val scope = rememberCoroutineScope()
   var isEnabled by remember { mutableStateOf(false) }
   LaunchedEffect(Unit) {
      delay(100)
      isEnabled = enabled
   }
   IconButton(
      onClick = {
         scope.launch {
            isEnabled = false
         }
         onClick()
      },
      content = {
         Icon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
         )
      },
      modifier = modifier,
      enabled = isEnabled,
      colors = IconButtonDefaults.iconButtonColors(contentColor = color)
   )
}

@Preview
@Composable
fun BackButtonPreview(){
   BackButton(onClick = {})
}