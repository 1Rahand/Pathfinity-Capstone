package ui.screens.payment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import domain.Lang
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import presentation.StringResources
import pathfinity.composeapp.generated.resources.Res
import pathfinity.composeapp.generated.resources.fib
import pathfinity.composeapp.generated.resources.whatsapp_logo_256
import ui.components.buttons.BackButton
import ui.components.layout.ColumnC
import ui.components.MyPreview
import ui.components.layout.RowC
import ui.components.environment.MyLocalSettings
import ui.components.environment.currentAppLang

@Composable
fun AcquireCodeScreen(navController: NavController) {
   Scaffold(Modifier.fillMaxSize()) {
      Box(
         Modifier
            .fillMaxSize()
            .padding(it)

      ) {
         BackButton(modifier = Modifier.padding(start = 8.dp , top = 8.dp)) { navController.navigateUp() }
         Column(
            Modifier
               .fillMaxSize()
               .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
         ) {
            FibLogo()
            Instructions()
         }
      }

   }
}

@Composable
private fun FibLogo() {
   ColumnC {
      Image(
         painter = painterResource(Res.drawable.fib),
         contentDescription = "FIB",
         modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .size(128.dp)
      )

      Text(
         text = StringResources.purchaseCode(currentAppLang()),
         style = MaterialTheme.typography.headlineMedium,
         fontWeight = FontWeight.Bold
      )

   }
}

@Composable
private fun Instructions(modifier: Modifier = Modifier) {
   val fontWeight = FontWeight.Normal
   val fontStyle = MaterialTheme.typography.labelMedium
   val uriHandler = LocalUriHandler.current
   Column(
      modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(24.dp),
      horizontalAlignment = Alignment.Start
   ) {

      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
         Text(
            text = StringResources.sendXThroughOrYBasedOnYourDesiredPlanThroughFIBToThisNumber(currentAppLang() , "50,000" , "500,000"),
            style = fontStyle,
            fontWeight = fontWeight
         )
         CopyableNumber(
            number = MyLocalSettings.current.fibPhone,
            afterClick = {}
         )
      }



      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
         val whatsappPhone = MyLocalSettings.current.whatsappPhone
         Text(
            text = StringResources.sendUsTheScreenshotOfThePaymentThroughThisWhatsAppNumber(currentAppLang()),
            style = fontStyle,
            fontWeight = fontWeight
         )

         WhatsAppNumber(
            number = whatsappPhone,
            onClick = { uriHandler.openUri("https://wa.me/964$whatsappPhone") }
         )
      }




      Text(
         text = StringResources.waitForUsToVerifyThePaymentAndSendYouTheCode(currentAppLang()),
         style = fontStyle,
         fontWeight = fontWeight
      )


   }
}


@Composable
fun CopyableNumber(modifier: Modifier = Modifier, number: String, afterClick: () -> Unit) {
   val clipboardManager = LocalClipboardManager.current

   Column {
      RowC(
         spacing = 8.dp,
         modifier = modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1F), RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable {
               clipboardManager.setText(AnnotatedString(number))
               afterClick()
            }
            .padding(horizontal = 12.dp, vertical = 6.dp)
      ) {
         Text(
            text = number,
            style = MaterialTheme.typography.labelMedium,
            modifier = modifier,
            fontWeight = FontWeight.Black
         )
         Icon(imageVector = Icons.Rounded.ContentCopy, contentDescription = "Copy")
      }
   }
}

@Composable
fun WhatsAppNumber(modifier: Modifier = Modifier, number: String, onClick: () -> Unit) {
   val clipboardManager = LocalClipboardManager.current

   Column {
      RowC(
         spacing = 8.dp,
         modifier = modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1F), RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable {
               onClick()
            }
            .padding(horizontal = 12.dp, vertical = 6.dp)
      ) {
         Text(
            text = number,
            style = MaterialTheme.typography.labelMedium,
            modifier = modifier,
            fontWeight = FontWeight.Black
         )
         Image(painter = painterResource(Res.drawable.whatsapp_logo_256), contentDescription = "WhatsApp", modifier = Modifier.size(24.dp))
      }
   }
}

@Preview
@Composable
fun AcquireCodeScreenPreview() {
   MyPreview(
      Lang.Eng
   ) {
      AcquireCodeScreen(navController = rememberNavController())
   }
}