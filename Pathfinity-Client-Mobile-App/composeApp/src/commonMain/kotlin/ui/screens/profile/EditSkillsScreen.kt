package ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import data.RepoAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pathfinity.composeapp.generated.resources.Res
import pathfinity.composeapp.generated.resources.icon_star
import presentation.StringResources
import ui.components.MyTopAppBar
import ui.components.buttons.SaveButton
import ui.components.environment.MyLocalUserStudent
import ui.components.environment.currentAppLang
import ui.components.layout.ColumnC
import ui.components.modifier.myBackground
import ui.platform.toPlatformDp

@Composable
fun EditSkillsScreen(navController: NavController, repoAuth: RepoAuth) {
   val profile = MyLocalUserStudent.current
   val snackbarState = remember { SnackbarHostState() }
   val scope = rememberCoroutineScope()
   val lang = currentAppLang()
   var loading by remember { mutableStateOf(false) }
   val currentSkills = profile.skills ?: emptyList()
   var skillsList by remember(profile.skills) { mutableStateOf(currentSkills) }
   var newSkill by remember { mutableStateOf("") }
   val scrollState = rememberScrollState()

   val onSave: () -> Unit = {
      scope.launch {
         loading = true
         repoAuth.saveUserStudent(profile.copy(skills = skillsList))
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
      topBar = { MyTopAppBar(title = StringResources.skills(lang), onBackClick = { navController.navigateUp() }) },
      snackbarHost = { SnackbarHost(snackbarState) }
   ) { paddingValues ->
      ColumnC(
         modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(20.dp)
            .verticalScroll(scrollState),
         spacing = 4.dp.toPlatformDp(8.dp)
      ) {
         Icon(
            painter = painterResource(Res.drawable.icon_star),
            contentDescription = "Skills",
            modifier = Modifier
               .myBackground(shape = CircleShape)
               .padding(16.dp)
               .size(32.dp),
         )

         Text(
            text = StringResources.editSkills(lang),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
         )

         Text(
            text = StringResources.skillsDescription(lang),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
         )

         Spacer(modifier = Modifier.height(16.dp))

         // Add new skill input
         Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
         ) {
            OutlinedTextField(
               value = newSkill,
               onValueChange = { newSkill = it },
               modifier = Modifier.weight(1f),
               label = { Text(StringResources.enterSkill(lang)) },
               singleLine = true
            )

            IconButton(
               onClick = {
                  if (newSkill.isNotBlank() && !skillsList.contains(newSkill)) {
                     skillsList = skillsList + newSkill
                     newSkill = ""
                  }
               },
               modifier = Modifier.align(androidx.compose.ui.Alignment.CenterVertically)
            ) {
               Icon(Icons.Default.Add, contentDescription = "Add Skill")
            }
         }

         Spacer(modifier = Modifier.height(16.dp))

         // Display current skills
         Text(
            text = StringResources.yourSkills(lang),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
         )

         Spacer(modifier = Modifier.height(8.dp))

         if (skillsList.isEmpty()) {
            Text(
               text = StringResources.noSkillsAdded(lang),
               style = MaterialTheme.typography.bodyMedium,
               color = Color.Gray
            )
         } else {
            FlowRow(
               horizontalArrangement = Arrangement.spacedBy(8.dp),
               verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
               skillsList.forEach { skill ->
                  InputChip(
                     selected = true,
                     onClick = { },
                     label = { Text(skill) },
                     trailingIcon = {
                        IconButton(
                           onClick = {
                              skillsList = skillsList.filter { it != skill }
                           },
                           modifier = Modifier.size(24.dp)
                        ) {
                           Icon(
                              Icons.Default.Add,
                              contentDescription = StringResources.removeSkill(lang),
                              modifier = Modifier.rotate(45f)
                           )
                        }
                     }
                  )
               }
            }
         }

         Spacer(modifier = Modifier.height(16.dp))

         SaveButton(
            modifier = Modifier
               .fillMaxWidth()
               .padding(top = 16.dp),
            isActive = skillsList != currentSkills,
            onClick = onSave,
            isLoading = loading
         )
      }
   }
}