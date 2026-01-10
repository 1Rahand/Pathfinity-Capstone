package ui.components.buttons

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Save
import presentation.StringResources
import ui.components.environment.currentAppLang
import ui.theme.green
import ui.theme.onGreen

@Composable
fun SaveButton(
   modifier : Modifier = Modifier,
   isActive : Boolean,
   onClick : () -> Unit,
   isLoading : Boolean,
){
   MyButton(
      text = StringResources.update(currentAppLang()),
      onClick = onClick,
      leadingIcon = {
         Icon(
            imageVector = EvaIcons.Fill.Save,
            contentDescription = null,
         )
      },
      containerColor = MaterialTheme.colorScheme.green,
      contentColor = MaterialTheme.colorScheme.onGreen,
      modifier = modifier,
      textStyle = MaterialTheme.typography.labelLarge,
      isLoading = isLoading,
      enabled = isActive
   )
}