package ui.screens.authentication

import SigningRoute
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import data.RepoAuth
import data.RepoAuthMock
import domain.Lang
import domain.result.RootError
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pathfinity.composeapp.generated.resources.Res
import pathfinity.composeapp.generated.resources.login
import presentation.StringResources
import ui.components.MyPreview
import ui.components.buttons.MyButton
import ui.components.environment.currentAppLang
import ui.components.layout.ColumnC
import ui.components.layout.RowC
import ui.components.modifier.noIndicationClickable

@Composable
fun SignupScreen(
   navController: NavController,
   repoAuth: RepoAuth
) {
   val snackbarState = remember { SnackbarHostState() }
   val scope = rememberCoroutineScope()
   val focusManager: FocusManager = LocalFocusManager.current
   var isLoading by remember { mutableStateOf(false) }
   var error by remember { mutableStateOf<RootError?>(null) }
   val lang = currentAppLang()
   val onSignupClick: suspend () -> Unit = {
      isLoading = true
      error = null
      repoAuth.anonymousSignUp()
         .onSuccess {
         }
         .onError {
            snackbarState.showSnackbar(it.getDescription(lang))
         }
      isLoading = false
   }


   Scaffold(
      modifier = Modifier.fillMaxSize(),
      snackbarHost = {
         SnackbarHost(hostState = snackbarState)
      }
   ) {
      Column(
         modifier = Modifier
            .noIndicationClickable { focusManager.clearFocus() }
            .fillMaxSize()
            .padding(it)
            .padding(24.dp)
         ,
         horizontalAlignment = Alignment.CenterHorizontally,
         verticalArrangement = Arrangement.SpaceEvenly
      ) {

         ColumnC(spacing = 8.dp) {
            Image(
               painter = painterResource(Res.drawable.login),
               contentDescription = "login",
               modifier = Modifier.widthIn(max = 170.dp)
            )
            Text(
               text = StringResources.signUp(currentAppLang()),
               style = MaterialTheme.typography.headlineLarge,
               fontWeight = FontWeight.Black
            )

            Text(
               text = StringResources.createAnAccountIfItsYourFirstTimeOrYouCanSignInIfYouAlreadyHaveAnAccount(currentAppLang()),
               style = MaterialTheme.typography.labelLarge,
               fontWeight = FontWeight.Light,
               modifier = Modifier.alpha(0.50F),
               textAlign = TextAlign.Center
            )
         }

         ColumnC {
            MyButton(
               onClick = {
                  scope.launch {
                     onSignupClick()
                  }
               },
               text = StringResources.signUp(currentAppLang()),
               enabled = true,
               modifier = Modifier.fillMaxWidth(),
               textStyle = MaterialTheme.typography.titleLarge,
               trailingIcon = null,
               containerColor = MaterialTheme.colorScheme.primary,
               contentColor = MaterialTheme.colorScheme.onPrimary,
               isLoading = isLoading
            )

            RowC(spacing = 0.dp) {
               Text(
                  text = StringResources.alreadyHaveAnAccount(currentAppLang()),
                  style = MaterialTheme.typography.labelLarge,
                  fontWeight = FontWeight.Light,
                  modifier = Modifier.alpha(0.50F)
               )
               Spacer(modifier = Modifier.width(4.dp))
               TextButton(
                  onClick = { navController.navigate(SigningRoute) },
                  content = {
                     Text(
                        text = StringResources.signIn(currentAppLang()),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                     )
                  }
               )
            }
         }
      }
   }
}

@Preview
@Composable
fun SigningScreenPreview() {
   MyPreview(Lang.Eng) {
      SignupScreen(rememberNavController(), RepoAuthMock)
   }
}