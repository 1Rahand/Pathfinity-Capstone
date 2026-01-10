package ui.screens.authentication

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import data.RepoAuth
import data.RepoAuthMock
import domain.Lang
import domain.result.RootError
import domain.validators.EmailValidator
import domain.validators.OtpValidator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import presentation.StringResources
import ui.components.MyPreview
import ui.components.MyTopAppBar
import ui.components.environment.currentAppLang
import ui.components.layout.LtrLayout
import ui.components.modifier.noIndicationClickable
import ui.components.textfield.MyFilledTextField
import ui.components.textfield.MyOutlineTextField

@Composable
fun EmailConfirmationScreen(
   navController: NavController,
   repoAuth: RepoAuth
) {
   val focusManager = LocalFocusManager.current
   val coroutineScope = rememberCoroutineScope()

   // State variables
   var isLoading by remember { mutableStateOf(false) }
   var error by remember { mutableStateOf<RootError?>(null) }
   var email by remember { mutableStateOf("") }
   var sentEmailTo by remember { mutableStateOf("") }
   var otpSent by remember { mutableStateOf(false) }
   var otp by remember { mutableStateOf("") }
   var resendCooldown by remember { mutableIntStateOf(0) }
   var isAuthenticated by remember { mutableStateOf(false) }

   // Derived state
   val isEmailValid = EmailValidator.isValidInstagramEmail(email)

   // Functions
   fun startResendCooldown() {
      coroutineScope.launch {
         resendCooldown = 5
         while (resendCooldown > 0) {
            delay(1000)
            resendCooldown -= 1
         }
      }
   }

   fun sendVerificationEmail(isResend: Boolean = false) {
      coroutineScope.launch {
         isLoading = true
         error = null

         repoAuth.requestUpdateEmail(email)
            .onSuccess {
               otpSent = true
               sentEmailTo = email
            }
            .onError {
               error = it
            }

         if (isResend || otpSent) {
            startResendCooldown()
         }

         isLoading = false
      }
   }

   fun verifyOtp() {
      coroutineScope.launch {
         isLoading = true
         error = null

         repoAuth.verifyUpdateEmailOtp(email, otp)
            .onSuccess {
               isAuthenticated = true
               delay(1000)
               navController.navigateUp()
            }
            .onError {
               error = it
            }
            .onFinally {
               isLoading = false
            }
      }
   }



   Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = { MyTopAppBar(title = StringResources.verifyEmail(currentAppLang())) { navController.navigateUp() } }
   ) { paddingValues ->
      Column(
         modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(24.dp)
            .noIndicationClickable { focusManager.clearFocus() },
         horizontalAlignment = Alignment.CenterHorizontally,
         verticalArrangement = Arrangement.Center
      ) {
         // Title with icon
         Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
         ) {
            Icon(
               imageVector = Icons.Outlined.Email,
               contentDescription = null,
               tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
               text = StringResources.verifyEmail(currentAppLang()),
               style = MaterialTheme.typography.headlineSmall,
               fontWeight = FontWeight.SemiBold
            )
         }

         // Error message
         AnimatedVisibility(
            visible = error != null
         ) {
            error.let {
               Card(
                  colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                  modifier = Modifier
                     .fillMaxWidth()
                     .padding(vertical = 8.dp)
               ) {
                  Text(
                     text = it?.getDescription(currentAppLang()) ?: "",
                     style = MaterialTheme.typography.bodyMedium,
                     color = MaterialTheme.colorScheme.onErrorContainer,
                     modifier = Modifier.padding(12.dp),
                     textAlign = TextAlign.Center
                  )
               }
            }
         }

         // Email input field
         Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LtrLayout {
               MyFilledTextField(
                  value = email,
                  icon = Icons.Filled.Email,
                  placeholder = "example@gmail.com",
                  onValueChange = {
                     email = it.replace(" ", "")
                     if (error != null) error = null
                  },
                  modifier = Modifier.fillMaxWidth(),
                  singleLine = true
               )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Send Verification Code button with cooldown indicator
            Button(
               onClick = { sendVerificationEmail() },
               modifier = Modifier.fillMaxWidth(),
               enabled = !isLoading && isEmailValid && resendCooldown == 0
            ) {
               if (isLoading) {
                  CircularProgressIndicator(
                     modifier = Modifier.size(24.dp),
                     strokeWidth = 2.dp
                  )
               } else if (resendCooldown > 0) {
                  Text(
                     "${StringResources.resendIn(currentAppLang())} ${resendCooldown}s",
                     style = MaterialTheme.typography.labelLarge
                  )
               } else {
                  Text(
                     StringResources.sendVerificationCode(currentAppLang()),
                     style = MaterialTheme.typography.labelLarge
                  )
               }
            }
         }

         // OTP verification section
         AnimatedVisibility(visible = otpSent) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
               // Success message
               Card(
                  colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                  modifier = Modifier
                     .fillMaxWidth()
                     .padding(vertical = 8.dp)
               ) {
                  Text(
                     text = "${StringResources.sentVerificationCodeToEmail(currentAppLang())}\n${sentEmailTo}",
                     style = MaterialTheme.typography.bodyMedium,
                     color = MaterialTheme.colorScheme.onPrimaryContainer,
                     modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.CenterHorizontally),
                     textAlign = TextAlign.Center
                  )
               }

               Spacer(modifier = Modifier.height(16.dp))

               Text(
                  text = StringResources.enterTheVerificationCode(currentAppLang()),
                  style = MaterialTheme.typography.bodyLarge,
                  fontWeight = FontWeight.Medium
               )

               Spacer(modifier = Modifier.height(8.dp))

               LtrLayout {
                  MyOutlineTextField(
                     value = otp,
                     placeholder = "123456",
                     onValueChange = {
                        if (it.all { char -> char.isDigit() } && it.length <= 6) {
                           otp = it
                        }
                        if (error != null) error = null
                     },
                     keyboardType = KeyboardType.Number,
                     modifier = Modifier.fillMaxWidth()
                  )
               }

               Spacer(modifier = Modifier.height(16.dp))

               Button(
                  onClick = { verifyOtp() },
                  modifier = Modifier.fillMaxWidth(),
                  enabled = !isLoading && OtpValidator.isOtpValid(otp)
               ) {
                  if (isLoading) {
                     CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                     )
                  } else {
                     Text(
                        text = StringResources.verifyEmail(currentAppLang()),
                        style = MaterialTheme.typography.labelLarge
                     )
                  }
               }
            }
         }

         // Email verification success
         AnimatedVisibility(
            visible = isAuthenticated,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
         ) {
            Column(
               horizontalAlignment = Alignment.CenterHorizontally,
               modifier = Modifier.padding(vertical = 16.dp)
            ) {
               Text(
                  text = StringResources.emailUpdatedSuccessfully(currentAppLang()),
                  style = MaterialTheme.typography.bodyLarge,
                  modifier = Modifier.padding(12.dp),
                  textAlign = TextAlign.Center,
                  fontWeight = FontWeight.Medium
               )
            }
         }
      }
   }
}

@Preview
@Composable
fun EmailConfirmationScreenPreview() {
   MyPreview(Lang.Eng) {
      EmailConfirmationScreen(
         navController = rememberNavController(),
         repoAuth = RepoAuthMock
      )
   }
}