package ui.components.environment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

val MyLocalAppearance = staticCompositionLocalOf {
   domain.Appearance.Light
}

@Composable
fun currentAppearance(): domain.Appearance {
   return MyLocalAppearance.current
}