package ui.screens.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import data.RepoAuth
import data.RepoAuthMock
import domain.Lang
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import presentation.StringResources
import ui.components.MyPreview
import ui.components.MyTopAppBar
import ui.components.buttons.SaveButton
import ui.components.environment.MyLocalUserStudent
import ui.components.environment.currentAppLang
import ui.components.layout.ColumnC
import ui.components.layout.RowC
import ui.components.modifier.noIndicationClickable
import ui.components.textfield.MyOutlineTextField

@Composable
fun EditNameScreen(navController: NavController, repoAuth: RepoAuth) {
   val profile = MyLocalUserStudent.current
   var tempFirstName by remember(profile.firstName) { mutableStateOf(profile.firstName) }
   var tempLastName by remember(profile.lastName) { mutableStateOf(profile.lastName) }
   val focusManager = LocalFocusManager.current
   var isLoading by remember { mutableStateOf(false) }
   val scope = rememberCoroutineScope()
   val snackbarHostState = remember { SnackbarHostState() }
   val lang = currentAppLang()
   val onSave: () -> Unit = {
      scope.launch {
         isLoading = true
         repoAuth.saveUserStudent(profile.copy(firstName = tempFirstName , lastName = tempLastName))
            .onError {
               launch {
                  snackbarHostState.showSnackbar(it.getDescription(lang))
               }
            }
            .onSuccess {
               launch {
                  launch {
                     snackbarHostState.showSnackbar(StringResources.success(lang))
                  }
                  delay(1000)
                  navController.navigateUp()
               }
            }
         isLoading = false
      }
   }

   Scaffold(
      modifier = Modifier
         .fillMaxSize()
         .noIndicationClickable { focusManager.clearFocus() },
      topBar = { MyTopAppBar(title = StringResources.name(currentAppLang()), onBackClick = { navController.navigateUp() }) },
      snackbarHost = { SnackbarHost(modifier = Modifier.imePadding(), hostState = snackbarHostState) }
   ) {
      ColumnC(
         modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .padding(20.dp)
      ) {
         RowC {
            ColumnC(Modifier.weight(1f)) {
               Text(
                  text = StringResources.firstName(currentAppLang()),
                  style = MaterialTheme.typography.titleLarge,
                  color = MaterialTheme.colorScheme.onSurface,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.fillMaxWidth()
               )
               MyOutlineTextField(
                  value = tempFirstName ?: "",
                  onValueChange = { tempFirstName = it },
                  modifier = Modifier.fillMaxWidth(),
                  singleLine = true
               )

            }

            ColumnC(Modifier.weight(1f)) {
               Text(
                  text = StringResources.lastName(currentAppLang()),
                  style = MaterialTheme.typography.titleLarge,
                  color = MaterialTheme.colorScheme.onSurface,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.fillMaxWidth()
               )
               MyOutlineTextField(
                  value = tempLastName ?: "",
                  onValueChange = { tempLastName = it },
                  modifier = Modifier.fillMaxWidth(),
                  singleLine = true
               )

            }

         }


         SaveButton(
            modifier = Modifier
               .fillMaxWidth()
               .padding(top = 16.dp),
            isActive = tempFirstName != profile.firstName,
            onClick = onSave,
            isLoading = isLoading
         )

      }
   }
}

@Preview
@Composable
fun EditNameScreenPreview() {
   MyPreview(Lang.Eng) {
      EditNameScreen(rememberNavController(), RepoAuthMock)
   }
}