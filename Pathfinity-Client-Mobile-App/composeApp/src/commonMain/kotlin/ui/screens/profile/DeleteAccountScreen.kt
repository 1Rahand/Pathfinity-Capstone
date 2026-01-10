package ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import data.RepoAuth
import domain.UserType
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import presentation.StringResources
import pathfinity.composeapp.generated.resources.Res
import pathfinity.composeapp.generated.resources.icon_delete
import pathfinity.composeapp.generated.resources.logo_whatsapp_96
import ui.components.layout.ColumnC
import ui.components.buttons.MyButton
import ui.components.MyTopAppBar
import ui.components.layout.RowC
import ui.components.environment.MyLocalSettings
import ui.components.environment.MyLocalUserStudent
import ui.components.environment.currentAppLang
import ui.components.modifier.myBackground
import ui.theme.onRed
import ui.theme.red
import ui.theme.redContainer

@Composable
fun DeleteAccountScreen(navController: NavController , repoAuth: RepoAuth){
   var showDialog by remember { mutableStateOf(false) }
   val student = MyLocalUserStudent.current
   val scope = rememberCoroutineScope()
   val snackbarState = remember { SnackbarHostState() }
   var loading by remember { mutableStateOf(false) }
   val lang = currentAppLang()
   val onDeleteClick : () -> Unit = {
      scope.launch {
         loading = true
         repoAuth.deleteAccount()
            .onSuccess {
               launch {
                  snackbarState.showSnackbar(StringResources.success(lang))
               }
            }
            .onError {
               launch {
                  snackbarState.showSnackbar(it.getDescription(lang))
               }
            }
         loading = false
      }
   }
   Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = { MyTopAppBar(title = StringResources.deleteAccount(currentAppLang()), onBackClick = {navController.navigateUp()}) },
      snackbarHost = { SnackbarHost(snackbarState) }
   ) {
      ColumnC(
         modifier = Modifier.fillMaxSize().padding(it).padding(20.dp),
         spacing = 8.dp
      ) {
         Icon(
            painter = painterResource(Res.drawable.icon_delete),
            contentDescription = "Delete Account",
            modifier = Modifier
               .myBackground(containerColor = MaterialTheme.colorScheme.redContainer , shape = CircleShape)
               .padding(16.dp)
               .size(32.dp),
            tint = MaterialTheme.colorScheme.red
         )
         val titleText =  StringResources.areYouSure(currentAppLang())
         Text(
            text = titleText,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
         )
         val subtitleText = StringResources.thisActionCantBeUndone(currentAppLang())
         Text(
            text = subtitleText,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
         )


         MyButton(
            text = StringResources.deleteAccount(currentAppLang()),
            onClick = { showDialog = true },
            containerColor = MaterialTheme.colorScheme.red,
            contentColor = MaterialTheme.colorScheme.onRed,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            textStyle = MaterialTheme.typography.labelLarge,
         )

         if (showDialog){
            LastChanceDialog(
               onDismissRequest = { showDialog = false },
               onConfirm = {
                  onDeleteClick()
               },
               loading = loading
            )
         }
      }
   }
}

/**
 * Contact Us Button
 * @param modifier Modifier for the button
 */

@Composable
fun ContactUsButton(
   modifier: Modifier = Modifier,
   enabled : Boolean = true
){
   val settings = MyLocalSettings.current
   val uriHandler = LocalUriHandler.current
   RowC(
      modifier = modifier
         .myBackground(onClick = {uriHandler.openUri(settings.whatsappLink)}.takeIf { enabled })
         .padding(horizontal = 24.dp , vertical = 16.dp)
   ) {
      Image(
         painter = painterResource(Res.drawable.logo_whatsapp_96),
         contentDescription = "Whatsapp",
         modifier = Modifier
            .myBackground(shape = CircleShape)
            .size(24.dp)
      )
      Text(
         text = StringResources.contactUs(currentAppLang()),
         style = MaterialTheme.typography.bodyLarge,
         textAlign = TextAlign.Center,
         fontWeight = FontWeight.Bold
      )
   }
}

@Composable
fun LastChanceDialog(
   modifier: Modifier = Modifier,
   onDismissRequest : () -> Unit,
   loading : Boolean = false,
   onConfirm : () -> Unit
){
   Dialog(
      onDismissRequest = onDismissRequest
   ){
      Box(
         modifier = modifier
            .myBackground()
            .fillMaxWidth()
            .heightIn(min = 100.dp)
         ,
         contentAlignment = Alignment.Center
      ) {
         ColumnC(
            spacing = 4.dp,
            modifier = Modifier.padding(32.dp)
         ) {
            Text(
               text = StringResources.lastChance(currentAppLang()),
               style = MaterialTheme.typography.titleLarge,
               fontWeight = FontWeight.Bold
            )
            Text(
               text = StringResources.areYouSure(currentAppLang()),
               style = MaterialTheme.typography.bodyLarge,
               color = Color.Gray,
               textAlign = TextAlign.Center
            )
            MyButton(
               text = StringResources.deleteAccount(currentAppLang()),
               onClick = onConfirm,
               containerColor = MaterialTheme.colorScheme.red,
               contentColor = MaterialTheme.colorScheme.onRed,
               modifier = Modifier.padding(top = 16.dp).height(48.dp),
               textStyle = MaterialTheme.typography.labelLarge,
               isLoading = loading
            )
         }


         Icon(
            imageVector = Icons.Rounded.Close,
            contentDescription = "Close",
            modifier = Modifier
               .padding(8.dp)
               .myBackground(containerColor = Color.Transparent, shape = CircleShape , onClick = onDismissRequest , elevation = 0.dp)
               .padding(12.dp)
               .size(20.dp)
               .align(Alignment.TopEnd),
         )


      }
   }
}