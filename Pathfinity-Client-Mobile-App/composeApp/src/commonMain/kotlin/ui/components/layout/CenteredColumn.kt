package ui.components.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.unit.Constraints

@Composable
fun CenteredColumn(
   modifier: Modifier = Modifier,
   centeredElement: @Composable () -> Unit,
   belowElements: @Composable () -> Unit,
) {
   Layout(modifier = modifier, contents = listOf(centeredElement, belowElements)) { measurablesList, constraints ->
      val measurables: List<Measurable> = measurablesList.flatten()

      val placeables = measurables.map {
         it.measure(Constraints())
      }
      var height: Int = 0
      var width: Int = 0

      placeables.forEachIndexed { index, placeable ->
         width = maxOf(width, placeable.width)
         height += if (index == 0) placeable.height else placeable.height * 2
      }

      var centeredYToStart = (height / 2) - (placeables[0].height / 2)

      val result = layout(width, height) {
         placeables.forEach {
            it.place((width / 2) - it.width / 2, centeredYToStart)
            centeredYToStart += it.height
         }
      }

      result
   }
}