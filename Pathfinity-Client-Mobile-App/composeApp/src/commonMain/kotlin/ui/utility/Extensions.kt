package ui.utility

import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection

fun LayoutDirection.getTextDir(): TextDirection {
   return if (this == LayoutDirection.Rtl) TextDirection.Rtl
   else TextDirection.Ltr
}