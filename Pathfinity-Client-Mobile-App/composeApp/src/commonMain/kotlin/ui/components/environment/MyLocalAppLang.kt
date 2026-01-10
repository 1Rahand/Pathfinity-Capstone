package ui.components.environment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import domain.Lang

val MyLocalAppLang = staticCompositionLocalOf {
   Lang.Krd
}

@Composable
fun currentAppLang(): Lang {
   return MyLocalAppLang.current
}