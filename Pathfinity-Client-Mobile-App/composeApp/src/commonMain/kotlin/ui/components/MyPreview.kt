package ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import di.commonModule
import domain.Lang
import domain.Setting
import org.koin.compose.KoinApplication
import supabase.UserStudent
import ui.components.environment.*
import ui.theme.AppTheme

@Composable
fun MyPreview(lang: Lang, appearance: domain.Appearance = domain.Appearance.SystemDefault, composable: @Composable () -> Unit) {
   val darkTheme = when (appearance) {
      domain.Appearance.SystemDefault -> isSystemInDarkTheme()
      domain.Appearance.Light -> false
      domain.Appearance.Dark -> true
   }

//   KoinApplication(application = { modules(commonModule()) }) {
      AppTheme(darkTheme) {
         Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            CompositionLocalProvider(
               MyLocalAppLang provides lang,
               LocalLayoutDirection provides lang.direction(),
               MyLocalAppearance provides appearance,
               MyLocalDarkTheme provides darkTheme,
               MyLocalUserStudent provides UserStudent.empty,
               MyLocalSettings provides Setting()
            ) {
               composable()
            }
         }
      }
//   }
}

@Composable
fun MyPreview2(lang: Lang, appearance: domain.Appearance = domain.Appearance.SystemDefault, composable: @Composable () -> Unit) {
   val darkTheme = when (appearance) {
      domain.Appearance.SystemDefault -> isSystemInDarkTheme()
      domain.Appearance.Light -> false
      domain.Appearance.Dark -> true
   }

   KoinApplication(application = { modules(commonModule()) }) {
      AppTheme(darkTheme) {
         Surface() {
            CompositionLocalProvider(
               MyLocalAppLang provides lang,
               LocalLayoutDirection provides lang.direction(),
               MyLocalAppearance provides appearance
            ) {
               composable()
            }
         }
      }
   }
}

fun Lang.direction(): LayoutDirection {
   return when (this) {
      Lang.Eng -> LayoutDirection.Ltr
      Lang.Krd -> LayoutDirection.Rtl
   }
}

fun Lang.textDirection(): TextDirection {
   return when (this) {
      Lang.Eng -> TextDirection.Ltr
      Lang.Krd -> TextDirection.Rtl
   }
}