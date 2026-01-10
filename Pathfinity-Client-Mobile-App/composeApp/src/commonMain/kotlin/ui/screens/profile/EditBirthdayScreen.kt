package ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Calendar
import data.RepoAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import presentation.StringResources
import ui.components.MyTopAppBar
import ui.components.buttons.SaveButton
import ui.components.environment.MyLocalUserStudent
import ui.components.environment.currentAppLang
import ui.components.layout.ColumnC
import ui.components.modifier.myBackground
import ui.platform.toPlatformDp
import utilities.fromMillis
import utilities.toMillis
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBirthdayScreen(navController: NavController, repoAuth: RepoAuth) {
   val profile = MyLocalUserStudent.current
   val snackbarState = remember { SnackbarHostState() }
   val scope = rememberCoroutineScope()
   var loading by remember { mutableStateOf(false) }

   // Get current date as default if birthdate is null
   val datePickerState = rememberDatePickerState(initialSelectedDateMillis = profile.birthdate?.toMillis())
   val selectedDate = datePickerState.selectedDateMillis?.let { LocalDate.fromMillis(it) }

   val lang = currentAppLang()
   val onSave: () -> Unit = {
      scope.launch {
         loading = true
         repoAuth.saveUserStudent(profile.copy(birthdate = selectedDate))
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
      topBar = { MyTopAppBar(title = StringResources.birthday(currentAppLang()), onBackClick = { navController.navigateUp() }) },
      snackbarHost = { SnackbarHost(snackbarState) }
   ) {
      ColumnC(
         modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(it)
            .padding(20.dp),
         spacing = 4.dp.toPlatformDp(8.dp)
      ) {
         Icon(
            imageVector = EvaIcons.Fill.Calendar,
            contentDescription = "Birthday",
            modifier = Modifier
               .myBackground(shape = CircleShape)
               .padding(16.dp)
               .size(32.dp),
         )

         Text(
            text = StringResources.birthday(currentAppLang()),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
         )

         Text(
            text = StringResources.selectYourBirthday(currentAppLang()),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
         )

         Spacer(modifier = Modifier.height(16.dp))

         // Date picker component



         DatePicker(
            state = datePickerState,
            showModeToggle = false
         )



         SaveButton(
            modifier = Modifier
               .fillMaxWidth()
               .padding(top = 24.dp),
            isActive = datePickerState.selectedDateMillis != profile.birthdate?.toMillis(),
            onClick = onSave,
            isLoading = loading
         )
      }
   }
}