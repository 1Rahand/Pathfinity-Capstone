package ui.components.buttons

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import domain.Lang
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pathfinity.composeapp.generated.resources.Res
import pathfinity.composeapp.generated.resources.icon_logout
import ui.components.MyPreview
import ui.components.layout.ColumnC
import ui.components.modifier.myBackground
import ui.theme.animationDuration
import ui.theme.disabledContentColor
import ui.theme.myAnimationSpec
import ui.theme.red

@Composable
fun MyIconButton(
   res: DrawableResource,
   modifier: Modifier = Modifier,
   onClick: () -> Unit = {},
   contentColor: Color = MaterialTheme.colorScheme.onSurface,
   containerColor : Color = MaterialTheme.colorScheme.surface,
   isLoading: Boolean = false,
   enabled : Boolean = true,
   elevation: Dp = 5.dp,
   shape : Shape = RoundedCornerShape(8.dp)

) {
   val finalContainerColor by animateColorAsState(if (enabled) containerColor else MaterialTheme.colorScheme.surface , animationSpec = MaterialTheme.myAnimationSpec())
   val finalContentColor by animateColorAsState(if (enabled) contentColor else MaterialTheme.colorScheme.disabledContentColor, animationSpec = MaterialTheme.myAnimationSpec())
   val finalElevation by animateDpAsState(if (enabled) elevation else 0.dp, animationSpec = MaterialTheme.myAnimationSpec())

   val finalOnClick: (() -> Unit)? = if (enabled) {
      { if (!isLoading) onClick() }
   } else null

   CompositionLocalProvider(LocalContentColor provides contentColor) {
      Box(
         modifier
            .myBackground(containerColor = finalContainerColor, onClick = finalOnClick, elevation = finalElevation , shape = shape)
            .padding(12.dp)
            .size(20.dp),
         contentAlignment = Alignment.Center
      ) {

         AnimatedVisibility(
            isLoading,
            enter = fadeIn(tween(MaterialTheme.animationDuration)) +  expandIn(tween(MaterialTheme.animationDuration),expandFrom = Alignment.Center),
            exit = fadeOut(tween(MaterialTheme.animationDuration)) + shrinkOut(tween(MaterialTheme.animationDuration),shrinkTowards = Alignment.Center)
         ) {
            CircularProgressIndicator(
               color = finalContentColor,
               modifier = Modifier.size(16.dp)
            )
         }
         AnimatedVisibility(
            !isLoading,
            enter = fadeIn(tween(MaterialTheme.animationDuration)) +  expandIn(tween(MaterialTheme.animationDuration),expandFrom = Alignment.Center),
            exit = fadeOut(tween(MaterialTheme.animationDuration)) + shrinkOut(tween(MaterialTheme.animationDuration),shrinkTowards = Alignment.Center)
         ) {
            Icon(
               painter = painterResource(res),
               contentDescription = null,
               modifier = Modifier
                  .size(20.dp),
               tint = finalContentColor
            )
         }
      }
   }
}

@Composable
fun MyIconButton(
   imageVector: ImageVector,
   modifier: Modifier = Modifier,
   onClick: () -> Unit = {},
   contentColor: Color = MaterialTheme.colorScheme.onSurface,
   containerColor : Color = MaterialTheme.colorScheme.surface,
   isLoading: Boolean = false,
   enabled : Boolean = true,
   elevation: Dp = 5.dp,
   shape : Shape = RoundedCornerShape(8.dp)
) {

   val finalContainerColor by animateColorAsState(if (enabled) containerColor else MaterialTheme.colorScheme.surface , animationSpec = MaterialTheme.myAnimationSpec())
   val finalContentColor by animateColorAsState(if (enabled) contentColor else MaterialTheme.colorScheme.disabledContentColor, animationSpec = MaterialTheme.myAnimationSpec())
   val finalElevation by animateDpAsState(if (enabled) elevation else 0.dp, animationSpec = MaterialTheme.myAnimationSpec())

   val finalOnClick: (() -> Unit)? = if (enabled) {
      { if (!isLoading) onClick() }
   } else null


   CompositionLocalProvider(LocalContentColor provides contentColor) {
      Box(
         modifier
            .myBackground(containerColor = finalContainerColor, onClick = finalOnClick, elevation = finalElevation , shape = shape)
            .padding(12.dp)
            .size(20.dp),
         contentAlignment = Alignment.Center
      ) {

         AnimatedVisibility(
            isLoading,
            enter = fadeIn(tween(MaterialTheme.animationDuration)) +  expandIn(tween(MaterialTheme.animationDuration),expandFrom = Alignment.Center),
            exit = fadeOut(tween(MaterialTheme.animationDuration)) + shrinkOut(tween(MaterialTheme.animationDuration),shrinkTowards = Alignment.Center)
         ) {
            CircularProgressIndicator(
               color = finalContentColor,
               modifier = Modifier.size(16.dp)
            )
         }
         AnimatedVisibility(
            !isLoading,
            enter = fadeIn(tween(MaterialTheme.animationDuration)) +  expandIn(tween(MaterialTheme.animationDuration),expandFrom = Alignment.Center),
            exit = fadeOut(tween(MaterialTheme.animationDuration)) + shrinkOut(tween(MaterialTheme.animationDuration),shrinkTowards = Alignment.Center)
         ) {
            Icon(
               imageVector = imageVector,
               contentDescription = null,
               modifier = Modifier
                  .size(20.dp),
               tint = finalContentColor
            )
         }
      }
   }
}

@Preview
@Composable
private fun MyIconButtonPreview() {
   var isLoading by remember { mutableStateOf(false) }
   val scope = rememberCoroutineScope()
   MyPreview(Lang.Eng){
      ColumnC(Modifier.fillMaxSize()) {
         MyIconButton(
            res = Res.drawable.icon_logout,
            onClick = {
               scope.launch {
                  isLoading = true
                  delay(2000)
                  isLoading = false
               }
            },
            isLoading = isLoading,
            contentColor = MaterialTheme.colorScheme.red
         )

      }

   }
}