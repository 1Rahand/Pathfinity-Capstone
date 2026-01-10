package ui.screens.internship

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import database.MyDao
import presentation.StringResources
import supabase.Internship
import supabase.UserCompany
import ui.components.MyTopAppBar
import ui.components.environment.currentAppLang

@Composable
fun InternshipDetailScreen(
   navController: NavController,
   dao: MyDao,
   internshipId: String
) {
   val internship by dao.getInternshipByIdFlow(internshipId).collectAsStateWithLifecycle(null)
   val companies by dao.getAllUserCompanies().collectAsStateWithLifecycle(null)
   val internshipCompany = remember(internship, companies) { companies?.find { it.id == internship?.companyId } }
   val isLoading = internship == null

   Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = {
         MyTopAppBar(
            title = StringResources.internship(currentAppLang()),
            onBackClick = { navController.navigateUp() }
         )
      }
   ) { paddingValues ->
      if (isLoading) {
         LoadingState(modifier = Modifier.fillMaxSize().padding(paddingValues))
      } else {
         internship?.let { intern ->
            DetailContent(
               modifier = Modifier.fillMaxSize().padding(paddingValues),
               internship = intern,
               company = internshipCompany
            )
         }
      }
   }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
   Column(
      modifier = modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
   ) {
      repeat(3) {
         Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
         ) {
            Box(
               modifier = Modifier
                  .fillMaxWidth()
                  .height(100.dp)
                  .background(
                     brush = Brush.horizontalGradient(
                        colors = listOf(
                           Color.LightGray.copy(alpha = 0.5f),
                           Color.LightGray.copy(alpha = 0.3f),
                           Color.LightGray.copy(alpha = 0.5f)
                        ),
                        startX = 0f,
                        endX = 1000f
                     )
                  )
            )
         }
      }
   }
}

@Composable
private fun DetailContent(
   modifier: Modifier = Modifier,
   internship: Internship,
   company: UserCompany?
) {
   val formattedCreatedDate = remember(internship) {
      internship.createdAt.toString().substringBefore("T")
   }

   LazyColumn(
      modifier = modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
   ) {
      // Header with company and status
      item {
         Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
         ) {
            Column {
               Text(
                  text = internship.title,
                  style = MaterialTheme.typography.headlineMedium,
                  fontWeight = FontWeight.Bold
               )
               company?.let {
                  Text(
                     text = it.companyName,
                     style = MaterialTheme.typography.titleMedium,
                     color = MaterialTheme.colorScheme.primary
                  )
               }
            }
         }
      }

      // Internship details card
      item {
         Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
         ) {
            Column(
               modifier = Modifier.padding(16.dp),
               verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
               DetailRow(
                  icon = Icons.Filled.AccessTime,
                  title = "Duration",
                  value = internship.duration
               )

               DetailRow(
                  icon = Icons.Filled.CalendarToday,
                  title = "Posted on",
                  value = formattedCreatedDate
               )

               Divider(modifier = Modifier.padding(vertical = 8.dp))

               Text(
                  text = "Description",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.SemiBold
               )

               Text(
                  text = internship.description,
                  style = MaterialTheme.typography.bodyMedium,
                  lineHeight = 24.sp
               )
            }
         }
      }

      // Skills section
      item {
         Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
         ) {
            Column(
               modifier = Modifier.padding(16.dp),
               verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
               Text(
                  text = "Skills Required",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.SemiBold
               )

               FlowRow(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(8.dp),
                  verticalArrangement = Arrangement.spacedBy(8.dp)
               ) {
                  internship.skills.forEach { skill ->
                     SkillChip(skill = skill)
                  }
               }
            }
         }
      }

      // Company info section
      item {
         val uriHandler = LocalUriHandler.current
         val emailSubject = "Application for ${internship.title} Position"
         val emailBody = """
        Dear Hiring Manager,

        I am writing to apply for the ${internship.title} internship position at ${company?.companyName ?: "your company"}.

        [Applicant can add their details here]

        Thank you for considering my application.

        Regards,
        [Applicant Name]
      """.trimIndent()

         Button(
            onClick = {
               company?.email?.let { email ->
                  val encodedSubject = urlEncode(emailSubject)
                  val encodedBody = urlEncode(emailBody)
                  val mailtoUri = "mailto:$email?subject=$encodedSubject&body=$encodedBody"

                  try {
                     uriHandler.openUri(mailtoUri)
                  } catch (e: Exception) {
                     println("Failed to open email app: ${e.message}")
                  }
               }
            },
            modifier = Modifier
               .fillMaxWidth()
               .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
               containerColor = MaterialTheme.colorScheme.primary
            )
         ) {
            Text(
               text = "Apply for Internship",
               style = MaterialTheme.typography.titleMedium
            )
         }
      }

      // Bottom spacing
      item {
         Spacer(modifier = Modifier.height(24.dp))
      }
   }
}

private fun urlEncode(input: String): String {
   val allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._~"
   return buildString {
      input.forEach { char ->
         when {
            char in allowedChars -> append(char)
            char == ' ' -> append("%20")
            else -> {
               // Convert to UTF-8 bytes and percent-encode
               val bytes = char.toString().encodeToByteArray()
               bytes.forEach { byte ->
                  append('%')
                  append(byte.toUByte().toString(16).padStart(2, '0').uppercase())
               }
            }
         }
      }
   }
}

@Composable
private fun DetailRow(
   icon: ImageVector,
   title: String,
   value: String
) {
   Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp)
   ) {
      Icon(
         imageVector = icon,
         contentDescription = title,
         tint = MaterialTheme.colorScheme.primary,
         modifier = Modifier.size(24.dp)
      )

      Column {
         Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
         )
         Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
         )
      }
   }
}

@Composable
private fun StatusChip(isActive: Boolean, text: String) {
   Surface(
      shape = RoundedCornerShape(50),
      color = if (isActive)
         MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
      else
         MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
   ) {
      Row(
         modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
         verticalAlignment = Alignment.CenterVertically,
         horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
         Box(
            modifier = Modifier
               .size(8.dp)
               .background(
                  color = if (isActive)
                     MaterialTheme.colorScheme.primary
                  else
                     MaterialTheme.colorScheme.error,
                  shape = CircleShape
               )
         )
         Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = if (isActive)
               MaterialTheme.colorScheme.primary
            else
               MaterialTheme.colorScheme.error
         )
      }
   }
}

