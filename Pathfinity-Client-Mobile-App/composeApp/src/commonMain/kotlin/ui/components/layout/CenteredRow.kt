package ui.components.layout

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun CenteredRow(
   modifier: Modifier = Modifier,
   startElements: @Composable RowScope.() -> Unit = {},
   centeredElements: @Composable RowScope.() -> Unit,
   endElements: @Composable RowScope.() -> Unit,
   spacing: Dp = 16.dp
) {
   val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

   val startRow = @Composable {
      Row(
         verticalAlignment = Alignment.CenterVertically
      ) {
         if (isRtl) {
            Spacer(modifier = Modifier.width(spacing))
            Row(
               horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
               endElements()
            }
         } else {
            Row(
               horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
               startElements()
            }
            Spacer(modifier = Modifier.width(spacing))
         }
      }
   }

   val centerRow = @Composable {
      Row(
         verticalAlignment = Alignment.CenterVertically,
         horizontalArrangement = Arrangement.spacedBy(spacing)
      ) {
         centeredElements()
      }
   }

   val endRow = @Composable {
      Row(
         verticalAlignment = Alignment.CenterVertically
      ) {
         if (isRtl) {
            Row(
               horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
               startElements()
            }
            Spacer(modifier = Modifier.width(spacing))
         } else {
            Spacer(modifier = Modifier.width(spacing))
            Row(
               horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
               endElements()
            }
         }
      }
   }
   Layout(modifier = modifier, contents = listOf(startRow, centerRow, endRow)) { measurablesList, constraints ->
      val measurables: List<Measurable> = measurablesList.flatten()

      val placeables = measurables.map {
         it.measure(Constraints())
      }


      val widthOfRows = mutableListOf<Int>(0, 0, 0)
      var layoutHeight: Int = 0
      var layoutWidth: Int = 0

      placeables.forEachIndexed { index, placeable ->
         layoutHeight = maxOf(layoutHeight, placeable.height)
         widthOfRows[index] = placeable.width
      }

      widthOfRows[0] = maxOf(widthOfRows[0], widthOfRows[2])
      widthOfRows[2] = maxOf(widthOfRows[0], widthOfRows[2])

      layoutWidth = widthOfRows.sum()

      val centeredXToStart = (layoutWidth / 2) - (placeables[1].width / 2)

      layout(layoutWidth, layoutHeight) {
         placeables[1].place(centeredXToStart, (layoutHeight / 2) - placeables[1].height / 2)

         if (isRtl) {
            placeables[0].place(centeredXToStart - placeables[0].width, (layoutHeight / 2) - placeables[0].height / 2)
            placeables[2].place(centeredXToStart + placeables[1].width, (layoutHeight / 2) - placeables[2].height / 2)
         } else {
            placeables[0].place(centeredXToStart - placeables[0].width, (layoutHeight / 2) - placeables[0].height / 2)
            placeables[2].place(centeredXToStart + placeables[1].width, (layoutHeight / 2) - placeables[2].height / 2)
         }
      }
   }
}