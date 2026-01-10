package ui.components.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import domain.Lang
import ui.components.environment.MyLocalAppLang

@Composable
fun RtlLayout(
   content: @Composable () -> Unit
) {
   CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
      content()
   }
}

@Composable
fun LtrLayout(
   content: @Composable () -> Unit
) {
   CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
      content()
   }
}

@Composable
fun LtrAndEnglishLayout(
   content: @Composable () -> Unit
) {
   CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr , MyLocalAppLang provides Lang.Eng) {
      content()
   }
}