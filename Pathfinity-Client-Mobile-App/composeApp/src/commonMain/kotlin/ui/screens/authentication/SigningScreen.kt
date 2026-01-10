package ui.screens.authentication

import OtpSignUpRoute
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import data.RepoAuth
import domain.validators.EmailValidator
import domain.result.RootError
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pathfinity.composeapp.generated.resources.Res
import pathfinity.composeapp.generated.resources.login
import presentation.StringResources
import ui.components.buttons.MyButton
import ui.components.environment.currentAppLang
import ui.components.layout.ColumnC
import ui.components.layout.LtrLayout
import ui.components.modifier.noIndicationClickable
import ui.components.textfield.MyFilledTextField
import ui.theme.red

@Composable
fun SigningScreen(
   navController: NavController,
   repoAuth: RepoAuth
) {
   val scope = rememberCoroutineScope()
   val focusManager: FocusManager = LocalFocusManager.current
   var isLoading by remember { mutableStateOf(false) }
   var error by remember { mutableStateOf<RootError?>(null) }
   var email by remember { mutableStateOf("") }
   var shouldShowEmailError by remember { mutableStateOf(false) }
   val onSignIn: suspend () -> Unit = {
      isLoading = true
      error = null
      shouldShowEmailError = false

      repoAuth.requestOtp(email)
         .onSuccess {
            navController.navigate(OtpSignUpRoute(email))
         }
         .onError {
            error = it
         }

   }

   Scaffold(modifier = Modifier.fillMaxSize()) {
      Column(
         modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .padding(16.dp)
            .noIndicationClickable {
               focusManager.clearFocus()
            },
         horizontalAlignment = Alignment.CenterHorizontally,
         verticalArrangement = Arrangement.SpaceAround
      ) {

         ColumnC(spacing = 8.dp) {
            Image(painter = painterResource(Res.drawable.login), contentDescription = "login", modifier = Modifier.widthIn(max = 200.dp))
            Text(
               text = StringResources.signIn(currentAppLang()),
               style = MaterialTheme.typography.headlineLarge,
               fontWeight = FontWeight.Black
            )

            Text(
               text = StringResources.pleaseEnterYourEmailWeWillCreateAnAccountForYouIfItIsYourFirstTime(currentAppLang()),
               style = MaterialTheme.typography.labelLarge,
               fontWeight = FontWeight.Light,
               modifier = Modifier.alpha(0.50F),
               textAlign = TextAlign.Center
            )
         }




         Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
         ) {
            AnimatedVisibility(isLoading) {
               CircularProgressIndicator(
                  modifier = Modifier.padding(bottom = 16.dp).size(24.dp),
                  color = MaterialTheme.colorScheme.onBackground
               )
            }

            AnimatedVisibility(error != null) {
               Text(
                  text = error?.getDescription(currentAppLang()) ?: "",
                  style = MaterialTheme.typography.labelMedium,
                  modifier = Modifier.padding(vertical = 8.dp),
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.error
               )
            }

            LtrLayout {
               MyFilledTextField(
                  modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(),
                  icon = Icons.Outlined.Mail,
                  placeholder = "Email",
                  value = email,
                  onValueChange = { email = it.replace(" ", "") },
               )
            }


            AnimatedVisibility(shouldShowEmailError) {
               Text(
                  modifier = Modifier.fillMaxWidth(),
                  text = StringResources.emailInvalid(currentAppLang()),
                  style = MaterialTheme.typography.labelLarge,
                  fontWeight = FontWeight.Light,
                  color = MaterialTheme.colorScheme.red
               )
            }
         }


         MyButton(
            modifier = Modifier.fillMaxWidth(),
            text = StringResources.signIn(currentAppLang()),
            enabled = EmailValidator.isValidInstagramEmail(email),
            onClick = { scope.launch { onSignIn() } },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
         )
      }
   }
}



