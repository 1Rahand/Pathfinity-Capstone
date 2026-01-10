package ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import presentation.StringResources
import ui.components.MyTopAppBar
import ui.components.environment.currentAppLang
import ui.platform.toPlatformDp

@Composable
fun AboutUsScreen(navController: NavController) {
   Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = { MyTopAppBar(title = StringResources.aboutUs(currentAppLang()), onBackClick = { navController.navigateUp() }) }
   ) {
      Column(
         Modifier
            .fillMaxSize()
            .padding(it)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
      ) {
         TitleText(text = StringResources.pathfinity(currentAppLang()))
         Spacer(Modifier.height(0.dp.toPlatformDp(8.dp)))
         BodyText(text = StringResources.pathfinityDescription(currentAppLang()))

         Spacer(Modifier.height(32.dp))

         TitleText(text = StringResources.ourGoal(currentAppLang()))
         Spacer(Modifier.height(0.dp.toPlatformDp(8.dp)))
         BodyText(text = StringResources.pathfinityGoal(currentAppLang()))

         Spacer(Modifier.height(32.dp))

         TitleText(text = StringResources.howItBegan(currentAppLang()))
         Spacer(Modifier.height(0.dp.toPlatformDp(8.dp)))
         BodyText(text = StringResources.pathfinityOrigin(currentAppLang()))
      }
   }
}

@Composable
fun TitleText(
   text: String,
   modifier: Modifier = Modifier,
) {
   Text(
      text = text,
      style = MaterialTheme.typography.titleLarge,
      modifier = modifier,
      fontWeight = FontWeight.Bold
   )
}

@Composable
fun BodyText(
   text: String,
   modifier: Modifier = Modifier
) {
   Text(
      text = text,
      style = MaterialTheme.typography.bodySmall,
      modifier = modifier,
      lineHeight = 28.sp,
      textAlign = TextAlign.Justify,
      color = Color.Gray
   )
}