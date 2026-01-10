package ui.screens.internship

import CompanyDetailRoute
import InternshipDetailRoute
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import database.MyDao
import presentation.StringResources
import supabase.UserCompany
import ui.components.MyTopAppBar
import ui.components.environment.currentAppLang
import ui.screens.course.CreatorContactChip

@Composable
fun CompanyDetailScreen(
   navController: NavController,
   dao : MyDao,
   companyId: String,
) {
   val company by dao.getUserCompanyByIdFlow(companyId).collectAsStateWithLifecycle(null)
   val internships by dao.getAllInternshipsByCompanyId(companyId).collectAsStateWithLifecycle(emptyList())
   Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = {
         MyTopAppBar(
            title = StringResources.company(currentAppLang()),
            onBackClick = {
               navController.navigateUp()
            }
         )
      }
   ) { padding ->
      if (company == null) {
         Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
         ) {
            CircularProgressIndicator()
         }
      } else {
         LazyColumn(
            modifier = Modifier
               .fillMaxSize()
               .padding(padding)
         ) {
            // Header Section with Creator Info
            item {
               CompanyHeader(company = company!!)
            }

            // Course count indicator
            item {
               Text(
                  text = "${internships.size} ${StringResources.internship(currentAppLang())}",
                  style = MaterialTheme.typography.titleMedium,
                  modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
               )
            }

            // Courses Section Header
            item {
               Text(
                  text = "${StringResources.internship(currentAppLang())} ${StringResources.by(currentAppLang())} ${company!!.companyName}",
                  style = MaterialTheme.typography.headlineSmall,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier
                     .padding(horizontal = 16.dp)
                     .padding(top = 8.dp , bottom = 16.dp)
               )
            }

            // Adaptive grid layout for courses
            item {
               BoxWithConstraints(
                  modifier = Modifier
                     .fillMaxWidth()
                     .padding(horizontal = 16.dp)
               ) {
                  val columns = when {
                     maxWidth > 840.dp -> 3
                     maxWidth > 600.dp -> 2
                     else -> 1
                  }

                  FlowRow(
                     maxItemsInEachRow = columns,
                     horizontalArrangement = Arrangement.spacedBy(16.dp),
                     verticalArrangement = Arrangement.spacedBy(16.dp),
                  ) {
                     internships.forEach { internship ->
                        InternshipCard(
                           modifier = Modifier
                              .widthIn(max = 400.dp),
                           internship = internship,
                           company = null,
                           onClick = {
                              navController.navigate(InternshipDetailRoute(internship.id))
                           },
                           onCompanyClick = {
                              navController.navigate(CompanyDetailRoute(internship.companyId))
                           }
                        )
                     }
                  }
               }
            }
         }
      }
   }
}

@Composable
private fun CompanyHeader(company: UserCompany) {
   Column(
      modifier = Modifier
         .fillMaxWidth()
         .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
   ) {
      // Profile avatar with image
      Box(
         modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .border(
               width = 3.dp,
               color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
               shape = CircleShape
            ),
         contentAlignment = Alignment.Center
      ) {
         if (company.profilePictureUrl != null) {
            AsyncImage(
               model = company.profilePictureUrl,
               contentDescription = null,
               contentScale = ContentScale.Crop,
               modifier = Modifier.fillMaxSize()
            )
         } else {
            // Fallback with initials when no profile image is available
            Box(
               modifier = Modifier
                  .fillMaxSize()
                  .background(MaterialTheme.colorScheme.surfaceVariant),
               contentAlignment = Alignment.Center
            ) {
               Text(
                  text = company.companyName.firstOrNull()?.toString() ?: "",
                  style = MaterialTheme.typography.headlineLarge,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
               )
            }
         }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Creator name
      Text(
         text = company.companyName,
         style = MaterialTheme.typography.headlineMedium,
         fontWeight = FontWeight.Bold,
         textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Contact information
      Row(
         horizontalArrangement = Arrangement.spacedBy(12.dp),
         verticalAlignment = Alignment.CenterVertically,
         modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
      ) {
         CreatorContactChip(
            icon = Icons.Default.Email,
            label = company.email
         )
      }

      HorizontalDivider(
         modifier = Modifier
            .padding(vertical = 16.dp)
            .padding(horizontal = 24.dp)
      )
   }
}