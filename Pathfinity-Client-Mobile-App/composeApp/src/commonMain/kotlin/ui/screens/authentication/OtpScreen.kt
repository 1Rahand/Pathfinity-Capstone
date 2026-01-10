package ui.screens.authentication

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import data.RepoAuth
import domain.validators.OtpValidator
import domain.result.ErrorOtp
import domain.result.ErrorSupabaseCancellation
import domain.result.RootError
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pathfinity.composeapp.generated.resources.Res
import pathfinity.composeapp.generated.resources.otp
import presentation.StringResources
import ui.components.MyTopAppBar
import ui.components.buttons.MyButton
import ui.components.environment.currentAppLang
import ui.components.layout.ColumnC
import ui.components.layout.LtrLayout
import ui.components.modifier.noIndicationClickable
import ui.components.textfield.MyOutlineTextField

@Composable
fun OtpScreen(
   navController: NavController,
   email: String,
   repoAuth: RepoAuth
) {
   val scope = rememberCoroutineScope()
   val focusManager = LocalFocusManager.current
   var otp by rememberSaveable { mutableStateOf("") }
   var isLoading by remember { mutableStateOf(false) }
   var error by remember { mutableStateOf<RootError?>(null) }

   // Resend OTP related state variables
   var remainingSeconds by rememberSaveable { mutableIntStateOf(10) }
   var canResend by rememberSaveable { mutableStateOf(false) }
   var waitTimeMultiplier by rememberSaveable { mutableIntStateOf(1) }

   // Timer for resend cooldown
   LaunchedEffect(waitTimeMultiplier) {
      remainingSeconds = 10 * waitTimeMultiplier
      canResend = false

      while (remainingSeconds > 0) {
         delay(1000)
         remainingSeconds--
      }

      canResend = true
   }

   val onVerify: suspend () -> Unit = {
      isLoading = true
      error = null
      repoAuth.verifyOtp(email, otp)
         .onSuccess {

         }
         .onError {
            error = if (error == ErrorSupabaseCancellation.Rest) {
               ErrorOtp
            } else {
               it
            }
         }
      isLoading = false
   }

   val onResendOtp: suspend () -> Unit = {
      if (canResend) {
         isLoading = true
         error = null

         repoAuth.requestOtp(email)
            .onSuccess {
               // Increase wait time for next resend
               waitTimeMultiplier++
            }
            .onError {
               error = it
            }

         isLoading = false
      }
   }

   Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = {
         MyTopAppBar(
            title = StringResources.verifyEmail(lang = currentAppLang()),
            onBackClick = { navController.navigateUp() }
         )
      }
   ) {
      Column(
         modifier = Modifier
            .padding(it)
            .padding(PaddingValues(16.dp))
            .fillMaxSize()
            .noIndicationClickable { focusManager.clearFocus() },
         verticalArrangement = Arrangement.SpaceAround
      ) {

         ColumnC(
            modifier = Modifier
               .padding(vertical = 32.dp)
               .fillMaxWidth(),
            spacing = 8.dp
         ) {
            Image(
               painter = painterResource(Res.drawable.otp),
               contentDescription = null,
               modifier = Modifier
                  .widthIn(max = 120.dp)
            )
            Text(
               text = StringResources.verifyEmail(lang = currentAppLang()),
               style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black)
            )
            Text(
               text = StringResources.aSixDigitCodeHasBeenSentToYourEmail(lang = currentAppLang(), email = email),
               style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Light),
               modifier = Modifier.alpha(0.5f),
               textAlign = TextAlign.Center
            )
         }

         ColumnC(
            modifier = Modifier
               .align(Alignment.CenterHorizontally),
            spacing = 8.dp
         ) {
            TitleText(StringResources.code(lang = currentAppLang()))
            LtrLayout {
               MyOutlineTextField(
                  modifier = Modifier.widthIn(max = 200.dp),
                  value = otp,
                  onValueChange = { if (it.all { it.isDigit() } && it.length <= 6) otp = it },
                  keyboardType = KeyboardType.Number,
                  singleLine = true
               )
            }
         }

         ColumnC(
            spacing = 0.dp
         ) {
            AnimatedVisibility(isLoading) {
               CircularProgressIndicator(
                  modifier = Modifier.size(24.dp),
                  color = MaterialTheme.colorScheme.onBackground
               )
            }

            AnimatedVisibility(error != null) {
               Text(
                  text = error?.getDescription(currentAppLang()) ?: "",
                  style = MaterialTheme.typography.labelMedium,
                  modifier = Modifier.padding(top = 16.dp),
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.error
               )
            }

            MyButton(
               modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
               onClick = {
                  scope.launch {
                     focusManager.clearFocus()
                     onVerify()
                  }
               },
               text = StringResources.verify(lang = currentAppLang()),
               containerColor = MaterialTheme.colorScheme.primary,
               contentColor = MaterialTheme.colorScheme.onPrimary,
               enabled = OtpValidator.isOtpValid(otp)
            )

            // Resend OTP button with countdown
            TextButton(
               onClick = { scope.launch { onResendOtp() } },
               enabled = canResend && !isLoading,
               modifier = Modifier.padding(top = 8.dp)
            ) {
               Text(
                  text = if (canResend)
                     StringResources.resendOtp(currentAppLang())
                  else
                     "${StringResources.resendOtp(currentAppLang())} (${remainingSeconds}s)",
                  style = MaterialTheme.typography.labelLarge
               )
            }
         }
      }
   }
}


@Composable
private fun TitleText(text: String) {
   Text(
      text = text,
      style = MaterialTheme.typography.headlineMedium,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onBackground
   )
}