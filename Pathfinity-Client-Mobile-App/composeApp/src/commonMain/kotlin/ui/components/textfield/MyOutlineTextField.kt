package ui.components.textfield

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun MyOutlineTextField(
   modifier: Modifier = Modifier,
   value: String,
   onValueChange: (String) -> Unit,
   radius: Dp = 8.dp,
   textStyle: TextStyle = LocalTextStyle.current,
   keyboardType: KeyboardType = KeyboardType.Unspecified,
   placeholder : String = "",
   singleLine : Boolean = false,
   leadingIcon: @Composable (() -> Unit)? = null,
) {
//   This is done to ensure that the text field is updated immediately when the value is changed, because of iOS sometimes it lags
   var internalValue by remember(value) { mutableStateOf(value) }
   OutlinedTextField(
      value = value,
      onValueChange = {
         internalValue = it
         onValueChange(it)
      },
      leadingIcon = leadingIcon,
      modifier = modifier
         .onKeyEvent {
            it.key == Key.Enter
         },
      singleLine = singleLine,
      textStyle = textStyle.copy(
         textDirection = if (LocalLayoutDirection.current == LayoutDirection.Ltr) TextDirection.Ltr else TextDirection.Rtl
      ),
      colors = OutlinedTextFieldDefaults.colors().copy(
         unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f),
         focusedIndicatorColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
         cursorColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5F),
      ),
      shape = RoundedCornerShape(radius),
      keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
      placeholder = {
         Text(
            text = placeholder,
            style = textStyle,
            fontWeight = FontWeight.Light,
            modifier = Modifier.alpha(0.50F)
         )
      }
   )
}