package ui.components.environment

import androidx.compose.runtime.staticCompositionLocalOf
import domain.Setting

val MyLocalSettings = staticCompositionLocalOf {
   Setting()
}