package ui.platform

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

expect fun Dp.toPlatformDp(iosOffset: Dp = 8.dp): Dp
