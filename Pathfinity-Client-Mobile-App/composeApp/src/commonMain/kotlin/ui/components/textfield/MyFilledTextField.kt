package ui.components.textfield

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import domain.Lang
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.components.MyPreview
import ui.components.modifier.myBackground
import ui.theme.surface2

@Composable
fun MyFilledTextField(
   modifier: Modifier = Modifier,
   icon: ImageVector? = null,
   placeholder: String = "",
   value: String,
   onValueChange: (String) -> Unit,
   singleLine: Boolean = false,
   keyboardType: KeyboardType = KeyboardType.Unspecified
) {

   // Local state for toggling password visibility if needed.
   var internalValue by remember(value) { mutableStateOf(value) }


   // Leading icon for the field.


   // The BasicTextField takes available space.

   val shape = RoundedCornerShape(8.dp)
   TextField(
      modifier = modifier
         .myBackground(containerColor = MaterialTheme.colorScheme.surface2 , elevation = 10.dp , shape = shape)
         .heightIn(min = 56.dp),
      value = internalValue,
      onValueChange = {
         internalValue = it
         onValueChange(it)
      },
      textStyle = MaterialTheme.typography.labelLarge.copy(
         textDirection = if (LocalLayoutDirection.current == LayoutDirection.Ltr) TextDirection.Ltr else TextDirection.Rtl
      ),
      keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
      singleLine = singleLine,
      colors = TextFieldDefaults.colors(
         unfocusedContainerColor = MaterialTheme.colorScheme.surface2,
         unfocusedIndicatorColor = Color.Transparent,
         focusedIndicatorColor = Color.Transparent,
         focusedContainerColor = MaterialTheme.colorScheme.surface2,
         unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
         focusedTextColor = MaterialTheme.colorScheme.onSurface,
      ),
      shape = shape,
      leadingIcon = if (icon != null) {
         {
            Icon(
               imageVector = icon,
               contentDescription = null,
               tint = MaterialTheme.colorScheme.onSurface,
               modifier = Modifier
                  .size(20.dp)
                  .alpha(0.25f)
            )
         }
      } else null,
      placeholder = {
         Text(
            text = placeholder,
            color = Color.Gray,
            style = MaterialTheme.typography.labelLarge
         )
      }

   )

}

@Preview
@Composable
fun MyFilledTextFieldPreview() {
   MyPreview(Lang.Eng) {
      Column(Modifier.padding(16.dp)) {
         MyFilledTextField(
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Rounded.Email,
            placeholder = "Email",
            value = "",
            onValueChange = {}
         )
      }
   }
}