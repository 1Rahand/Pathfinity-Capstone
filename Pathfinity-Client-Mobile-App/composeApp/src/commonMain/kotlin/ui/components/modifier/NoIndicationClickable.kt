package ui.components.modifier

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.noIndicationClickable(onClick: () -> Unit): Modifier {
   return this.pointerInput(Unit) {
      detectTapGestures(onTap = { onClick() })
   }
}

