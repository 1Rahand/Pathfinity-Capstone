package ui.components.buttons

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import domain.Lang
import org.jetbrains.compose.ui.tooling.preview.Preview
import presentation.StringResources
import ui.components.MyPreview
import ui.components.environment.currentAppLang
import ui.components.modifier.myBackground
import ui.theme.animationDuration
import ui.theme.disabledContentColor
import ui.theme.myAnimationSpec

@Composable
fun MyButton(
   modifier: Modifier = Modifier,
   text: String,
   onClick: () -> Unit,
   containerColor: Color = MaterialTheme.colorScheme.surface,
   contentColor: Color = MaterialTheme.colorScheme.onSurface,
   trailingIcon: @Composable (() -> Unit)? = null,
   leadingIcon: @Composable (() -> Unit)? = null,
   horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp , Alignment.CenterHorizontally),
   enabled: Boolean = true,
   textStyle: TextStyle = MaterialTheme.typography.labelLarge,
   isLoading: Boolean = false,
   elevation: Dp = 5.dp,
) {

   val finalContainerColor by animateColorAsState(if (enabled) containerColor else MaterialTheme.colorScheme.surface , animationSpec = MaterialTheme.myAnimationSpec())
   val finalContentColor by animateColorAsState(if (enabled) contentColor else MaterialTheme.colorScheme.disabledContentColor, animationSpec = MaterialTheme.myAnimationSpec())
   val finalElevation by animateDpAsState(if (enabled) elevation else 0.dp, animationSpec = MaterialTheme.myAnimationSpec())

   val finalOnClick: (() -> Unit)? = if (enabled) {
      { if (!isLoading) onClick() }
   } else null


   CompositionLocalProvider(LocalContentColor provides finalContentColor) {
      Box(
         modifier = modifier
            .myBackground(containerColor = finalContainerColor, onClick = finalOnClick, elevation = finalElevation)
            .heightIn(min = 56.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .width(IntrinsicSize.Max),
         contentAlignment = Alignment.Center
      ) {
         AnimatedVisibility(
            isLoading,
            enter = fadeIn(tween(MaterialTheme.animationDuration)) + expandIn(tween(MaterialTheme.animationDuration), expandFrom = Alignment.Center),
            exit = fadeOut(tween(MaterialTheme.animationDuration)) + shrinkOut(tween(MaterialTheme.animationDuration), shrinkTowards = Alignment.Center)
         ) {
            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = LocalContentColor.current)
         }
         AnimatedVisibility(
            !isLoading,
            enter = fadeIn(tween(MaterialTheme.animationDuration)) + expandIn(tween(MaterialTheme.animationDuration), expandFrom = Alignment.Center),
            exit = fadeOut(tween(MaterialTheme.animationDuration)) + shrinkOut(tween(MaterialTheme.animationDuration), shrinkTowards = Alignment.Center)
         ) {
            Row(
               modifier = Modifier.fillMaxWidth(),
               horizontalArrangement = horizontalArrangement,
               verticalAlignment = Alignment.CenterVertically,
            ) {
               leadingIcon?.invoke()
               Text(
                  text = text,
                  style = textStyle,
                  fontWeight = FontWeight.Bold,
               )
               trailingIcon?.invoke()
            }

         }

      }

   }
}

@Preview
@Composable
fun MyButtonPreview() {
   MyPreview(Lang.Eng) {
      Column(Modifier.fillMaxSize().safeContentPadding().padding(16.dp)) {
         MyButton(
            onClick = { },
            text = StringResources.signUp(currentAppLang()),
            enabled = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = null,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
         )
      }
   }
}