package ui.platform

import androidx.compose.ui.unit.Dp

actual fun Dp.toPlatformDp(iosOffset : Dp): Dp {
    return this
}