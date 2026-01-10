package ui.screens.payment

import AcquireCodeRoute
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import data.RepoAuth
import domain.result.ErrorInvalidOrRedeemedGiftCard
import domain.result.ErrorSupabaseCancellation
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.painterResource
import pathfinity.composeapp.generated.resources.Res
import pathfinity.composeapp.generated.resources.voucher
import presentation.StringResources
import ui.components.MyTopAppBar
import ui.components.RotativeChevron
import ui.components.buttons.MyButton
import ui.components.environment.MyLocalUserStudent
import ui.components.environment.currentAppLang
import ui.components.layout.ColumnC
import ui.components.layout.LtrLayout
import ui.components.modifier.myBackground
import ui.components.modifier.noIndicationClickable
import ui.components.textfield.MyOutlineTextField
import ui.platform.toPlatformDp

@Composable
fun CodeActivationScreen(navController: NavController, repoAuth: RepoAuth) {
   val focusManager = LocalFocusManager.current
   val profile = MyLocalUserStudent.current
   val snackBarHost = remember { SnackbarHostState() }
   var code by remember { mutableStateOf("") }
   var isLoading by remember { mutableStateOf(false) }
   val isUpgradeButtonEnabled = code.length == 16
   val scope = rememberCoroutineScope()
   val lang = currentAppLang()

   val onUpgradeClick: () -> Unit = {
      scope.launch {
         isLoading = true
         repoAuth.redeemCode(code)
            .onSuccess {
               if (it) {
                  snackBarHost.showSnackbar(StringResources.success(lang))
               } else {
                  snackBarHost.showSnackbar(ErrorInvalidOrRedeemedGiftCard.getDescription(lang))
               }
            }
            .onError {
               if (it == ErrorSupabaseCancellation.Rest) {
                  snackBarHost.showSnackbar(ErrorInvalidOrRedeemedGiftCard.getDescription(lang))
               } else {
                  snackBarHost.showSnackbar(it.getDescription(lang))
               }
            }
         isLoading = false
      }
   }


   Scaffold(
      modifier = Modifier
         .fillMaxSize()
         .noIndicationClickable { focusManager.clearFocus() },
      topBar = {
         MyTopAppBar(
            title = StringResources.upgrade(currentAppLang()),
            onBackClick = { navController.navigateUp() }
         )
      },
      snackbarHost = {
         SnackbarHost(
            hostState = snackBarHost,
            modifier = Modifier
         )
      }
   ) {

      Box(
         Modifier
            .padding(it)
            .fillMaxSize()
            .padding(24.dp),
         contentAlignment = Alignment.Center
      ) {
         AnimatedVisibility(profile.premium, enter = fadeIn(), exit = fadeOut()) {
            ColumnC {
               Icon(imageVector = Icons.Rounded.CheckCircle, contentDescription = null, modifier = Modifier.size(64.dp))
               Text(
                  text = StringResources.youAreAlreadyPro(currentAppLang()),
                  style = MaterialTheme.typography.headlineMedium,
                  fontWeight = FontWeight.Black,
                  modifier = Modifier,
                  textAlign = TextAlign.Center
               )
               Text(
                  text = StringResources.expiresInXDays(
                     currentAppLang(),
                     profile.premiumExpiresAt!!.minus(Clock.System.now()).inWholeDays.toString()
                  ),
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.Light,
                  modifier = Modifier.alpha(0.75F)
               )
            }
         }

         AnimatedVisibility(!profile.premium, enter = fadeIn(), exit = fadeOut()) {
            Column(
               Modifier.fillMaxSize(),
               verticalArrangement = Arrangement.SpaceAround,
               horizontalAlignment = Alignment.CenterHorizontally
            ) {


//             Header
               ColumnC(spacing = 16.dp) {
                  Image(
                     painter = painterResource(Res.drawable.voucher),
                     contentDescription = null,
                     modifier = Modifier
                        .myBackground(shape = CircleShape)
                        .padding(16.dp)
                        .size(56.dp)
                  )

                  Text(
                     text = StringResources.accessToAllCoursesAndFeatures(currentAppLang()),
                     style = MaterialTheme.typography.labelLarge,
                     fontWeight = FontWeight.Light,
                     modifier = Modifier,
                     textAlign = TextAlign.Center,
                     color = Color.Gray
                  )
               }


               ColumnC(spacing = 8.dp.toPlatformDp(8.dp)) {
                  Text(
                     text = StringResources.enter16DigitCode(currentAppLang()),
                     style = MaterialTheme.typography.labelLarge,
                     fontWeight = FontWeight.Light
                  )

                  LtrLayout {
                     MyOutlineTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = code,
                        onValueChange = { value ->
                           val noHyphens = value.replace("-", "")
                           val alphaNumericOnly = noHyphens.replace("[^A-Za-z0-9]".toRegex(), "")
                           val truncated = alphaNumericOnly.take(16)
                           val uppercased = truncated.uppercase()
                           code = uppercased
                        },
                        placeholder = "AAAABBBBCCCCDDDD"
                     )
                  }


                  MyButton(
                     text = StringResources.getActivationCode(currentAppLang()),
                     modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                     onClick = { navController.navigate(AcquireCodeRoute) },
                     horizontalArrangement = Arrangement.SpaceBetween,
                     trailingIcon = {
                        RotativeChevron()
                     }
                  )


               }



               ColumnC {

                  MyButton(
                     onClick = onUpgradeClick,
                     text = StringResources.upgrade(currentAppLang()),
                     isLoading = isLoading,
                  )
               }

            }
         }
      }
   }
}

