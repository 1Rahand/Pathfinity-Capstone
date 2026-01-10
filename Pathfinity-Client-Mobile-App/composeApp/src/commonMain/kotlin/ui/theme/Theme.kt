package ui.theme

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import pathfinity.composeapp.generated.resources.*
import ui.components.environment.MyLocalDarkTheme

private val lightScheme = lightColorScheme(
   primary = primaryLight,
   onPrimary = onPrimaryLight,
   primaryContainer = primaryContainerLight,
   onPrimaryContainer = onPrimaryContainerLight,
   secondary = secondaryLight,
   onSecondary = onSecondaryLight,
   secondaryContainer = secondaryContainerLight,
   onSecondaryContainer = onSecondaryContainerLight,
   tertiary = tertiaryLight,
   onTertiary = onTertiaryLight,
   tertiaryContainer = tertiaryContainerLight,
   onTertiaryContainer = onTertiaryContainerLight,
   error = errorLight,
   onError = onErrorLight,
   errorContainer = errorContainerLight,
   onErrorContainer = onErrorContainerLight,
   background = backgroundLight,
   onBackground = onBackgroundLight,
   surface = surfaceLight,
   onSurface = onSurfaceLight,
   surfaceVariant = surfaceVariantLight,
   onSurfaceVariant = onSurfaceVariantLight,
   outline = outlineLight,
   outlineVariant = outlineVariantLight,
   scrim = scrimLight,
   inverseSurface = inverseSurfaceLight,
   inverseOnSurface = inverseOnSurfaceLight,
   inversePrimary = inversePrimaryLight,
   surfaceDim = surfaceDimLight,
   surfaceBright = surfaceBrightLight,
   surfaceContainerLowest = surfaceContainerLowestLight,
   surfaceContainerLow = surfaceContainerLowLight,
   surfaceContainer = surfaceContainerLight,
   surfaceContainerHigh = surfaceContainerHighLight,
   surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
   primary = primaryDark,
   onPrimary = onPrimaryDark,
   primaryContainer = primaryContainerDark,
   onPrimaryContainer = onPrimaryContainerDark,
   secondary = secondaryDark,
   onSecondary = onSecondaryDark,
   secondaryContainer = secondaryContainerDark,
   onSecondaryContainer = onSecondaryContainerDark,
   tertiary = tertiaryDark,
   onTertiary = onTertiaryDark,
   tertiaryContainer = tertiaryContainerDark,
   onTertiaryContainer = onTertiaryContainerDark,
   error = errorDark,
   onError = onErrorDark,
   errorContainer = errorContainerDark,
   onErrorContainer = onErrorContainerDark,
   background = backgroundDark,
   onBackground = onBackgroundDark,
   surface = surfaceDark,
   onSurface = onSurfaceDark,
   surfaceVariant = surfaceVariantDark,
   onSurfaceVariant = onSurfaceVariantDark,
   outline = outlineDark,
   outlineVariant = outlineVariantDark,
   scrim = scrimDark,
   inverseSurface = inverseSurfaceDark,
   inverseOnSurface = inverseOnSurfaceDark,
   inversePrimary = inversePrimaryDark,
   surfaceDim = surfaceDimDark,
   surfaceBright = surfaceBrightDark,
   surfaceContainerLowest = surfaceContainerLowestDark,
   surfaceContainerLow = surfaceContainerLowDark,
   surfaceContainer = surfaceContainerDark,
   surfaceContainerHigh = surfaceContainerHighDark,
   surfaceContainerHighest = surfaceContainerHighestDark,
)

private val mediumContrastLightColorScheme = lightColorScheme(
   primary = primaryLightMediumContrast,
   onPrimary = onPrimaryLightMediumContrast,
   primaryContainer = primaryContainerLightMediumContrast,
   onPrimaryContainer = onPrimaryContainerLightMediumContrast,
   secondary = secondaryLightMediumContrast,
   onSecondary = onSecondaryLightMediumContrast,
   secondaryContainer = secondaryContainerLightMediumContrast,
   onSecondaryContainer = onSecondaryContainerLightMediumContrast,
   tertiary = tertiaryLightMediumContrast,
   onTertiary = onTertiaryLightMediumContrast,
   tertiaryContainer = tertiaryContainerLightMediumContrast,
   onTertiaryContainer = onTertiaryContainerLightMediumContrast,
   error = errorLightMediumContrast,
   onError = onErrorLightMediumContrast,
   errorContainer = errorContainerLightMediumContrast,
   onErrorContainer = onErrorContainerLightMediumContrast,
   background = backgroundLightMediumContrast,
   onBackground = onBackgroundLightMediumContrast,
   surface = surfaceLightMediumContrast,
   onSurface = onSurfaceLightMediumContrast,
   surfaceVariant = surfaceVariantLightMediumContrast,
   onSurfaceVariant = onSurfaceVariantLightMediumContrast,
   outline = outlineLightMediumContrast,
   outlineVariant = outlineVariantLightMediumContrast,
   scrim = scrimLightMediumContrast,
   inverseSurface = inverseSurfaceLightMediumContrast,
   inverseOnSurface = inverseOnSurfaceLightMediumContrast,
   inversePrimary = inversePrimaryLightMediumContrast,
   surfaceDim = surfaceDimLightMediumContrast,
   surfaceBright = surfaceBrightLightMediumContrast,
   surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
   surfaceContainerLow = surfaceContainerLowLightMediumContrast,
   surfaceContainer = surfaceContainerLightMediumContrast,
   surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
   surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
   primary = primaryLightHighContrast,
   onPrimary = onPrimaryLightHighContrast,
   primaryContainer = primaryContainerLightHighContrast,
   onPrimaryContainer = onPrimaryContainerLightHighContrast,
   secondary = secondaryLightHighContrast,
   onSecondary = onSecondaryLightHighContrast,
   secondaryContainer = secondaryContainerLightHighContrast,
   onSecondaryContainer = onSecondaryContainerLightHighContrast,
   tertiary = tertiaryLightHighContrast,
   onTertiary = onTertiaryLightHighContrast,
   tertiaryContainer = tertiaryContainerLightHighContrast,
   onTertiaryContainer = onTertiaryContainerLightHighContrast,
   error = errorLightHighContrast,
   onError = onErrorLightHighContrast,
   errorContainer = errorContainerLightHighContrast,
   onErrorContainer = onErrorContainerLightHighContrast,
   background = backgroundLightHighContrast,
   onBackground = onBackgroundLightHighContrast,
   surface = surfaceLightHighContrast,
   onSurface = onSurfaceLightHighContrast,
   surfaceVariant = surfaceVariantLightHighContrast,
   onSurfaceVariant = onSurfaceVariantLightHighContrast,
   outline = outlineLightHighContrast,
   outlineVariant = outlineVariantLightHighContrast,
   scrim = scrimLightHighContrast,
   inverseSurface = inverseSurfaceLightHighContrast,
   inverseOnSurface = inverseOnSurfaceLightHighContrast,
   inversePrimary = inversePrimaryLightHighContrast,
   surfaceDim = surfaceDimLightHighContrast,
   surfaceBright = surfaceBrightLightHighContrast,
   surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
   surfaceContainerLow = surfaceContainerLowLightHighContrast,
   surfaceContainer = surfaceContainerLightHighContrast,
   surfaceContainerHigh = surfaceContainerHighLightHighContrast,
   surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
   primary = primaryDarkMediumContrast,
   onPrimary = onPrimaryDarkMediumContrast,
   primaryContainer = primaryContainerDarkMediumContrast,
   onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
   secondary = secondaryDarkMediumContrast,
   onSecondary = onSecondaryDarkMediumContrast,
   secondaryContainer = secondaryContainerDarkMediumContrast,
   onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
   tertiary = tertiaryDarkMediumContrast,
   onTertiary = onTertiaryDarkMediumContrast,
   tertiaryContainer = tertiaryContainerDarkMediumContrast,
   onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
   error = errorDarkMediumContrast,
   onError = onErrorDarkMediumContrast,
   errorContainer = errorContainerDarkMediumContrast,
   onErrorContainer = onErrorContainerDarkMediumContrast,
   background = backgroundDarkMediumContrast,
   onBackground = onBackgroundDarkMediumContrast,
   surface = surfaceDarkMediumContrast,
   onSurface = onSurfaceDarkMediumContrast,
   surfaceVariant = surfaceVariantDarkMediumContrast,
   onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
   outline = outlineDarkMediumContrast,
   outlineVariant = outlineVariantDarkMediumContrast,
   scrim = scrimDarkMediumContrast,
   inverseSurface = inverseSurfaceDarkMediumContrast,
   inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
   inversePrimary = inversePrimaryDarkMediumContrast,
   surfaceDim = surfaceDimDarkMediumContrast,
   surfaceBright = surfaceBrightDarkMediumContrast,
   surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
   surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
   surfaceContainer = surfaceContainerDarkMediumContrast,
   surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
   surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
   primary = primaryDarkHighContrast,
   onPrimary = onPrimaryDarkHighContrast,
   primaryContainer = primaryContainerDarkHighContrast,
   onPrimaryContainer = onPrimaryContainerDarkHighContrast,
   secondary = secondaryDarkHighContrast,
   onSecondary = onSecondaryDarkHighContrast,
   secondaryContainer = secondaryContainerDarkHighContrast,
   onSecondaryContainer = onSecondaryContainerDarkHighContrast,
   tertiary = tertiaryDarkHighContrast,
   onTertiary = onTertiaryDarkHighContrast,
   tertiaryContainer = tertiaryContainerDarkHighContrast,
   onTertiaryContainer = onTertiaryContainerDarkHighContrast,
   error = errorDarkHighContrast,
   onError = onErrorDarkHighContrast,
   errorContainer = errorContainerDarkHighContrast,
   onErrorContainer = onErrorContainerDarkHighContrast,
   background = backgroundDarkHighContrast,
   onBackground = onBackgroundDarkHighContrast,
   surface = surfaceDarkHighContrast,
   onSurface = onSurfaceDarkHighContrast,
   surfaceVariant = surfaceVariantDarkHighContrast,
   onSurfaceVariant = onSurfaceVariantDarkHighContrast,
   outline = outlineDarkHighContrast,
   outlineVariant = outlineVariantDarkHighContrast,
   scrim = scrimDarkHighContrast,
   inverseSurface = inverseSurfaceDarkHighContrast,
   inverseOnSurface = inverseOnSurfaceDarkHighContrast,
   inversePrimary = inversePrimaryDarkHighContrast,
   surfaceDim = surfaceDimDarkHighContrast,
   surfaceBright = surfaceBrightDarkHighContrast,
   surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
   surfaceContainerLow = surfaceContainerLowDarkHighContrast,
   surfaceContainer = surfaceContainerDarkHighContrast,
   surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
   surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)

@Immutable
data class ColorFamily(
   val color: Color,
   val onColor: Color,
   val colorContainer: Color,
   val onColorContainer: Color
)

@Composable
fun AppTheme(
   darkTheme: Boolean = isSystemInDarkTheme(),
   content: @Composable() () -> Unit
) {
   val fontFamily = FontFamily(
      listOf(
         Font(Res.font.noto_kufi_arabic_bold, style = FontStyle.Normal, weight = FontWeight.Bold),
         Font(Res.font.noto_kufi_arabic_black, style = FontStyle.Normal, weight = FontWeight.Black),
         Font(Res.font.noto_kufi_arabic_thin, style = FontStyle.Normal, weight = FontWeight.Thin),
         Font(Res.font.noto_kufi_arabic_light, style = FontStyle.Normal, weight = FontWeight.Light),
         Font(Res.font.noto_kufi_arabic_medium, style = FontStyle.Normal, weight = FontWeight.Medium),
         Font(Res.font.noto_kufi_arabic_regular, style = FontStyle.Normal, weight = FontWeight.Normal),
         Font(Res.font.noto_kufi_arabic_extra_bold, style = FontStyle.Normal, weight = FontWeight.ExtraBold),
         Font(Res.font.noto_kufi_arabic_semi_bold, style = FontStyle.Normal, weight = FontWeight.SemiBold),
         Font(Res.font.noto_kufi_arabic_extra_light, style = FontStyle.Normal, weight = FontWeight.ExtraLight),
      )
   )


// Default Material 3 typography values
   val baseline = Typography()

   val AppTypography = Typography(
      displayLarge = baseline.displayLarge.copy(fontFamily = fontFamily),
      displayMedium = baseline.displayMedium.copy(fontFamily = fontFamily),
      displaySmall = baseline.displaySmall.copy(fontFamily = fontFamily),
      headlineLarge = baseline.headlineLarge.copy(fontFamily = fontFamily),
      headlineMedium = baseline.headlineMedium.copy(fontFamily = fontFamily),
      headlineSmall = baseline.headlineSmall.copy(fontFamily = fontFamily),
      titleLarge = baseline.titleLarge.copy(fontFamily = fontFamily),
      titleMedium = baseline.titleMedium.copy(fontFamily = fontFamily),
      titleSmall = baseline.titleSmall.copy(fontFamily = fontFamily),

      bodyLarge = baseline.bodyLarge.copy(fontFamily = fontFamily),
      bodyMedium = baseline.bodyMedium.copy(fontFamily = fontFamily),
      bodySmall = baseline.bodySmall.copy(fontFamily = fontFamily),
      labelLarge = baseline.labelLarge.copy(fontFamily = fontFamily),
      labelMedium = baseline.labelMedium.copy(fontFamily = fontFamily),
      labelSmall = baseline.labelSmall.copy(fontFamily = fontFamily)
   )

   val colorScheme: ColorScheme = if (darkTheme) darkScheme else lightScheme
   MaterialTheme(
      colorScheme = colorScheme,
      typography = AppTypography,
      content = content
   )
}

val MaterialTheme.animationDuration: Int
   get() = 500

fun <T> MaterialTheme.myAnimationSpec() : AnimationSpec<T> {
   return tween(durationMillis = animationDuration)
}

val ColorScheme.green
   @Composable
   get() = if (MyLocalDarkTheme.current) Color(0xFF66BB6A) else Color(0xFF43A047)

val ColorScheme.disabled
   @Composable
   get() = if (MyLocalDarkTheme.current) Color(0xff4b4b4b) else Color(0xffd2d2d2)

val ColorScheme.onDisabled
   @Composable
   get() = if (MyLocalDarkTheme.current) Color(0xff888888) else Color(0xffCCCECF)

val ColorScheme.disabledContentColor
   @Composable
   get() = if (MyLocalDarkTheme.current) Color(0xff434544) else Color(0xffCCCECF)

val ColorScheme.greenContainer
   @Composable
   get() = if (MyLocalDarkTheme.current) Color(0xff031f05) else Color(0xffc0f3c2)

val ColorScheme.surface2
   @Composable
   get() = if (MyLocalDarkTheme.current) surfaceDark2 else surfaceLight

val ColorScheme.greenAnswer
   @Composable
   get() = if (MyLocalDarkTheme.current) greenAnswerDark else greenAnswerLight

val ColorScheme.redAnswer
   @Composable
   get() = if (MyLocalDarkTheme.current) redAnswerDark else redAnswerLight

val ColorScheme.redOnPrimary
   @Composable
   get() = redLight

val ColorScheme.gray
   @Composable
   get() = if (MyLocalDarkTheme.current) Color(0xFFBDBDBD) else Color(0xFF757575)

val ColorScheme.onGreen
   @Composable
   get() = if (MyLocalDarkTheme.current) Color(0xFF000000) else Color(0xFFFFFFFF)

val ColorScheme.blue
   @Composable
   get() = if (MyLocalDarkTheme.current) Color(0xFF29B6F6) else Color(0xFF1E88E5)

val ColorScheme.darkBlue
   @Composable
   get() = if (MyLocalDarkTheme.current) Color(0xFF5C6BC0) else Color(0xFF3949AB)

val ColorScheme.red
   @Composable
   get() = if (MyLocalDarkTheme.current) Color(0xFFEC4040) else Color(0xFFD81B1B)

val ColorScheme.redContainer
   @Composable
   get() = if (MyLocalDarkTheme.current) Color(0xFF170707) else Color(0xFFf8e8e8)

val ColorScheme.onRed
   @Composable
   get() = if (MyLocalDarkTheme.current) Color(0xFFFFFFFF) else Color(0xFFFFFFFF)

val ColorScheme.purple
   @Composable
   get() = if (MyLocalDarkTheme.current) Color(0xFF7E57C2) else Color(0xFF5E35B1)

val ColorScheme.darkRed
   @Composable
   get() = if (MyLocalDarkTheme.current) Color(0xFF8B2817) else Color(0xFF69190B)

val ColorScheme.yellow
   @Composable
   get() = if (MyLocalDarkTheme.current) Color(0xFFFFEE58) else Color(0xFFFDD835)

