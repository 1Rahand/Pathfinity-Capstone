package ui.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import data.RepoAuth
import data.RepoAuthMock
import domain.Lang
import domain.result.ErrorAccountAlreadyExists
import domain.result.ErrorOtp
import domain.result.ErrorSupabaseCancellation
import domain.validators.EmailValidator
import domain.validators.OtpValidator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import presentation.StringResources
import ui.components.MyPreview
import ui.components.MyTopAppBar
import ui.components.buttons.MyButton
import ui.components.environment.MyLocalUserStudent
import ui.components.environment.currentAppLang
import ui.components.layout.ColumnC
import ui.components.layout.LtrLayout
import ui.components.layout.RowC
import ui.components.modifier.myBackground
import ui.components.modifier.noIndicationClickable
import ui.components.textfield.MyFilledTextField
import ui.components.textfield.MyOutlineTextField

@Composable
fun EditEmailScreen(
   navController: NavController,
   repoAuth: RepoAuth
) {
   val focusManager = LocalFocusManager.current
   val scope = rememberCoroutineScope()
   val profile = MyLocalUserStudent.current

   // State variables
   var sendLoading by remember { mutableStateOf(false) }
   var verifyLoading by remember { mutableStateOf(false) }
   var tempEmail : String by remember(profile.email) { mutableStateOf(profile.email ?: "") }
   var sentEmailTo by remember { mutableStateOf("") }
   var otpSent by remember { mutableStateOf(false) }
   var otp by remember { mutableStateOf("") }
   var resendCooldown by remember { mutableIntStateOf(0) }
   val snackbarHostState = remember { SnackbarHostState() }

   // Derived state
   val isEmailValid = EmailValidator.isValidInstagramEmail(tempEmail)
   val lang = currentAppLang()
   val otpFieldFocusRequester = remember { FocusRequester() }


   // Functions
   fun startResendCooldown() {
      scope.launch {
         resendCooldown = 5
         while (resendCooldown > 0) {
            delay(1000)
            resendCooldown -= 1
         }
      }
   }

   fun sendVerificationEmail(isResend: Boolean = false) {
      scope.launch {
         sendLoading = true
         repoAuth.requestUpdateEmail(tempEmail)
            .onSuccess {
               otpSent = true
               sentEmailTo = tempEmail
               launch {
                  snackbarHostState.showSnackbar(message = "✅  ${StringResources.sentVerificationCodeToEmail(lang)}\n${sentEmailTo}")
               }
            }
            .onError {
               launch {
                  if (it == ErrorSupabaseCancellation.Rest) {
                     snackbarHostState.showSnackbar(ErrorAccountAlreadyExists.getDescription(lang))
                  } else {
                     snackbarHostState.showSnackbar(it.getDescription(lang))
                  }
               }
            }

         if (isResend || otpSent) {
            startResendCooldown()
         }

         sendLoading = false
      }
   }

   fun verifyOtp() {
      scope.launch {
         verifyLoading = true
         repoAuth.verifyUpdateEmailOtp(tempEmail, otp)
            .onSuccess {
               launch {
                  snackbarHostState.showSnackbar(StringResources.success(lang = lang))
               }
               delay(1000)
               navController.navigateUp()
            }
            .onError {
               launch {
                  if (it == ErrorSupabaseCancellation.Rest) {
                     snackbarHostState.showSnackbar(ErrorOtp.getDescription(lang))
                  } else {
                     snackbarHostState.showSnackbar(it.getDescription(lang))
                  }
               }
            }
         verifyLoading = false
      }
   }



   Scaffold(
      modifier = Modifier
         .fillMaxSize(),
      topBar = { MyTopAppBar(title = StringResources.verifyEmail(currentAppLang())) { navController.navigateUp() } },
      snackbarHost = { SnackbarHost(snackbarHostState) }
   ) { paddingValues ->
      Column(
         modifier = Modifier
            .noIndicationClickable { focusManager.clearFocus() }
            .fillMaxSize()
            .padding(paddingValues)
            .padding(24.dp)
         ,
         horizontalAlignment = Alignment.CenterHorizontally,
         verticalArrangement = Arrangement.Center
      ) {
         // Title with icon



         ColumnC(
            spacing = 8.dp
         ) {


            Icon(
               imageVector = Icons.Outlined.Email,
               contentDescription = null,
               tint = MaterialTheme.colorScheme.primary,
               modifier = Modifier
                  .myBackground(shape = CircleShape, elevation = 1.dp)
                  .padding(16.dp)
                  .size(32.dp),
            )
            Text(
               text = StringResources.setEmail(currentAppLang()),
               style = MaterialTheme.typography.titleLarge,
               fontWeight = FontWeight.Bold
            )

            // Email input field
            LtrLayout {
               MyFilledTextField(
                  value = tempEmail,
                  icon = Icons.Filled.Email,
                  placeholder = "example@gmail.com",
                  onValueChange = {
                     tempEmail = it.replace(" ", "")
                  },
                  modifier = Modifier.fillMaxWidth(),
                  singleLine = true
               )
            }

            MyButton(
               modifier = Modifier.fillMaxWidth(0.70F).padding(top = 16.dp),
               onClick = { sendVerificationEmail() },
               text = if (resendCooldown > 0) "${StringResources.resendIn(currentAppLang())} ${resendCooldown}s" else StringResources.sendVerificationCode(
                  currentAppLang()
               ),
               enabled = isEmailValid && resendCooldown == 0 && tempEmail != profile.email,
               containerColor = MaterialTheme.colorScheme.primary,
               contentColor = MaterialTheme.colorScheme.onPrimary,
               isLoading = sendLoading,
            )
         }


         // OTP verification section
         AnimatedVisibility(visible = otpSent) {
            ColumnC(Modifier.padding(top = 32.dp)) {
               HorizontalDivider()
               Text(
                  text = StringResources.enterTheVerificationCode(currentAppLang()),
                  style = MaterialTheme.typography.titleLarge,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.align(Alignment.Start)
               )


               RowC {
                  LtrLayout {
                     MyOutlineTextField(
                        value = otp,
                        placeholder = "123456",
                        onValueChange = {
                           if (it.all { char -> char.isDigit() } && it.length <= 6) {
                              otp = it
                           }
                        },
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.width(120.dp).focusRequester(otpFieldFocusRequester)
                     )
                  }

                  MyButton(
                     text = StringResources.verify(currentAppLang()),
                     onClick = { verifyOtp() },
                     modifier = Modifier.weight(1f),
                     enabled = OtpValidator.isOtpValid(otp),
                     isLoading = verifyLoading,
                     containerColor = MaterialTheme.colorScheme.primary,
                     contentColor = MaterialTheme.colorScheme.onPrimary
                  )
               }
            }
         }
      }
   }
}

@Preview
@Composable
fun EmailConfirmationScreenPreview() {
   MyPreview(Lang.Eng) {
      EditEmailScreen(
         navController = rememberNavController(),
         repoAuth = RepoAuthMock
      )
   }
}