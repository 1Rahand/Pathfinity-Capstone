package ui.components.checkbox

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class PolygonShape(private val sides: Int) : Shape {
   override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
      return Outline.Generic(path = Path().apply { polygon(sides, size.maxDimension / 2, size.center) })
   }
}

fun Path.polygon(sides: Int, radius: Float, center: Offset) {
   val angle = 2.0 * PI / sides
   moveTo(
      x = center.x + (radius * cos(0.0)).toFloat(),
      y = center.y + (radius * sin(0.0)).toFloat()
   )
   for (i in 1 until sides) {
      lineTo(
         x = center.x + (radius * cos(angle * i)).toFloat(),
         y = center.y + (radius * sin(angle * i)).toFloat()
      )
   }
   close()
}