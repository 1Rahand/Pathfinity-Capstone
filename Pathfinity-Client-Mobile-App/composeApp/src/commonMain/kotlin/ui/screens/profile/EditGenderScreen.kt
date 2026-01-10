package ui.screens.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import data.RepoAuth
import domain.Gender
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pathfinity.composeapp.generated.resources.Res
import pathfinity.composeapp.generated.resources.icon_gender
import presentation.StringResources
import ui.components.MyTopAppBar
import ui.components.buttons.SaveButton
import ui.components.environment.MyLocalUserStudent
import ui.components.environment.currentAppLang
import ui.components.layout.ColumnC
import ui.components.layout.RowC
import ui.components.modifier.myBackground
import ui.platform.toPlatformDp
import ui.utility.GenderIcon

@Composable
fun EditGenderScreen(navController: NavController, repoAuth: RepoAuth) {
   val profile = MyLocalUserStudent.current
   val snackbarState = remember { SnackbarHostState() }
   val scope = rememberCoroutineScope()
   var loading by remember { mutableStateOf(false) }
   var tempGender by remember(profile.gender) { mutableStateOf(profile.gender) }
   val lang = currentAppLang()
   val onSave: () -> Unit = {
      scope.launch {
         loading = true
         repoAuth.saveUserStudent(profile.copy(genderString = tempGender.name.lowercase()))
            .onError {
               launch {
                  snackbarState.showSnackbar(it.getDescription(lang))
               }
            }
            .onSuccess {
               launch {
                  launch {
                     snackbarState.showSnackbar(StringResources.success(lang))
                  }
                  delay(1000)
                  navController.navigateUp()
               }
            }
         loading = false
      }
   }
   Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = { MyTopAppBar(title = StringResources.gender(currentAppLang()), onBackClick = { navController.navigateUp() }) },
      snackbarHost = { SnackbarHost(snackbarState) }
   ) {
      ColumnC(
         modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .padding(20.dp),
         spacing = 4.dp.toPlatformDp(8.dp)
      ) {
         Icon(
            painter = painterResource(Res.drawable.icon_gender),
            contentDescription = "Gender",
            modifier = Modifier
               .myBackground(shape = CircleShape)
               .padding(16.dp)
               .size(32.dp),
         )

         Text(
            text = StringResources.gender(currentAppLang()),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
         )
         Text(
            text = StringResources.chooseYourGender(currentAppLang()),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
         )

         RowC(Modifier.padding(top = 16.dp)) {
            val genders = listOf(Gender.Male , Gender.Female)
            genders.forEach {
               GenderIcon(
                  modifier = Modifier.weight(1f),
                  gender = it,
                  isActive = it == tempGender,
                  onClick = {
                     tempGender = it
                  }
               )
            }
         }

         SaveButton(
            modifier = Modifier
               .fillMaxWidth()
               .padding(top = 16.dp),
            isActive = tempGender != profile.gender,
            onClick = onSave,
            isLoading = loading
         )
      }
   }
}