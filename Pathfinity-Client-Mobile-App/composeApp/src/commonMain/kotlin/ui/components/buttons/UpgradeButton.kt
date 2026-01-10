package ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import presentation.StringResources
import ui.components.environment.currentAppLang

val gradientColors = listOf(
   Color(0xFFFF3F3F),
   Color(0xFF063CFF)
)

val gradientBrush = Brush.linearGradient(
   colors = gradientColors
)

@Composable
fun UpgradeButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
   Button(
      onClick = onClick,
      colors = ButtonDefaults.buttonColors(
         contentColor = Color.White,
         containerColor = Color.Transparent
      ),
      modifier = modifier.clip(RoundedCornerShape(24.dp)).background(gradientBrush)
   ) {
      Text(
         text = StringResources.upgrade(currentAppLang()),
         style = MaterialTheme.typography.labelMedium,
         fontWeight = FontWeight.Black,
         modifier = Modifier
      )
   }
}