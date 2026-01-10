package ui.components.environment

import androidx.compose.runtime.staticCompositionLocalOf
import supabase.UserStudent


val MyLocalUserStudent = staticCompositionLocalOf {
   UserStudent.empty
}