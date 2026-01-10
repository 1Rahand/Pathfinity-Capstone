package ui.screens.internship

import CompanyDetailRoute
import EditProfileRoute
import InternshipDetailRoute
import PaymentGraph
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import database.MyDao
import kotlinx.coroutines.flow.map
import ui.components.MyBaseModalSheet
import ui.components.bottomNavBarHeight
import ui.components.buttons.FilterButton
import ui.components.environment.MyLocalUserStudent
import ui.components.textfield.MyOutlineTextField

@Composable
fun InternshipsScreen(
   navController: NavController,
   dao: MyDao
) {
   val currentUser = MyLocalUserStudent.current
   val isPremium = currentUser.premium
   val internships by dao.getAllInternships().collectAsStateWithLifecycle(null)
   val companies by dao.getAllUserCompanies().map { it.associateBy { it.id } }.collectAsStateWithLifecycle(mapOf())

   // Search query state
   var searchQuery by remember { mutableStateOf("") }

   // Filter state
   var showFilterSheet by remember { mutableStateOf(false) }
   var internshipFilters by remember { mutableStateOf(InternshipFilters()) }

   // Extract unique values from internships data for filters
   val allCities by remember(internships) {
      derivedStateOf {
         internships?.mapNotNull { it.city }
            ?.filter { it.isNotBlank() }
            ?.distinct()
            ?.sorted() ?: emptyList()
      }
   }

   val allDurations by remember(internships) {
      derivedStateOf {
         internships?.map { it.duration }
            ?.filter { it.isNotBlank() }
            ?.distinct()
            ?.sorted() ?: emptyList()
      }
   }

   val allSkills by remember(internships) {
      derivedStateOf {
         internships?.flatMap { it.skills }
            ?.filter { it.isNotBlank() }
            ?.distinct()
            ?.sorted() ?: emptyList()
      }
   }

   // Filtered internships based on search and filter
   val filteredInternships = remember(internships, searchQuery, internshipFilters) {
      internships?.filter { internship ->
         // Text search filter
         val matchesSearch = searchQuery.isEmpty() ||
                 internship.title.contains(searchQuery, ignoreCase = true) ||
                 internship.description.contains(searchQuery, ignoreCase = true) ||
                 companies[internship.companyId]?.companyName?.contains(searchQuery, ignoreCase = true) == true ||
                 internship.skills.any { it.contains(searchQuery, ignoreCase = true) }

         // Apply other filters
         val matchesPaid = (internshipFilters.showPaid && internship.isPaid) ||
                 (internshipFilters.showUnpaid && !internship.isPaid)

         val matchesCity = internshipFilters.city.isEmpty() ||
                 internship.city?.equals(internshipFilters.city, ignoreCase = true) == true

         val matchesDuration = internshipFilters.duration == null ||
                 internship.duration == internshipFilters.duration

         val matchesSkills = internshipFilters.skills.isEmpty() ||
                 internshipFilters.skills.any { skill ->
                    internship.skills.any { it.equals(skill, ignoreCase = true) }
                 }

         matchesSearch && matchesPaid && matchesCity && matchesDuration && matchesSkills
      } ?: emptyList()
   }

   Scaffold(
      modifier = Modifier.fillMaxSize()
   ) { paddingValues ->
      Box(modifier = Modifier.fillMaxSize()) {
         // Main content (will be blurred for non-premium users)
         Column(
            modifier = Modifier
               .fillMaxSize()
               .padding(paddingValues)
               .padding(bottom = bottomNavBarHeight)
               // Apply blur for non-premium users
               .then(if (!isPremium) Modifier.blur(radius = 8.dp) else Modifier)
         ) {
            // Search and filter bar
            SearchFilterBar(
               searchQuery = searchQuery,
               onSearchQueryChange = { if (isPremium) searchQuery = it },
               isFilterApplied = internshipFilters.isAnyFilterApplied,
               onFilterIconClick = { if (isPremium) showFilterSheet = true }
            )

            if (internships == null) {
               // Loading state
               Box(
                  modifier = Modifier.fillMaxSize(),
                  contentAlignment = Alignment.Center
               ) {
                  CircularProgressIndicator()
               }
            } else if (filteredInternships.isEmpty()) {
               // Empty state
               Box(
                  modifier = Modifier.fillMaxSize(),
                  contentAlignment = Alignment.Center
               ) {
                  Column(
                     horizontalAlignment = Alignment.CenterHorizontally,
                     verticalArrangement = Arrangement.spacedBy(8.dp)
                  ) {
                     Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                     )
                     Text(
                        text = "No internships found",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                     )
                  }
               }
            } else {
               // Internships list
               FlowRow(
                  modifier = Modifier
                     .verticalScroll(rememberScrollState())
                     .padding(16.dp)
                     .fillMaxSize(),
                  verticalArrangement = Arrangement.spacedBy(16.dp),
                  horizontalArrangement = Arrangement.spacedBy(16.dp),
               ) {
                  var minHeight by remember { mutableStateOf(0.dp) }
                  val density = LocalDensity.current
                  filteredInternships.forEach { internship ->
                     val company = companies[internship.companyId]

                     InternshipCard(
                        modifier = Modifier
                           .onSizeChanged {
                              with(density) {
                                 minHeight = max(minHeight, it.height.toDp())
                              }
                           }
                           .heightIn(min = minHeight),
                        internship = internship,
                        company = company,
                        onClick = {
                           if (isPremium) {
                              navController.navigate(InternshipDetailRoute(internship.id))
                           }
                        },
                        onCompanyClick = {
                           if (isPremium) {
                              navController.navigate(CompanyDetailRoute(it.id))
                           }
                        }
                     )
                  }
               }
            }
         }

         // Premium upgrade prompt overlay for non-premium users
         if (!isPremium) {
            PremiumUpgradePrompt(
               modifier = Modifier
                  .fillMaxSize()
                  .padding(paddingValues)
                  .padding(32.dp),
               onUpgradeClick = {
                  // Navigate to premium upgrade screen
                  if(currentUser.email.isNullOrEmpty()){
                     navController.navigate(EditProfileRoute)
                  }else{
                     navController.navigate(PaymentGraph)
                  }
               }
            )
         }
      }
   }

   // Show filter sheet if needed
   if (showFilterSheet && isPremium) {
      InternshipFilterSheet(
         onDismissRequest = { showFilterSheet = false },
         initialFilters = internshipFilters,
         onApplyFilters = { filters ->
            internshipFilters = filters
         },
         availableCities = allCities,
         availableDurations = allDurations,
         availableSkills = allSkills
      )
   }
}

@Composable
fun PremiumUpgradePrompt(
   modifier: Modifier = Modifier,
   onUpgradeClick: () -> Unit
) {
   Column(
      modifier = modifier,
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
   ) {
      Icon(
         imageVector = Icons.Outlined.LocationOn,
         contentDescription = null,
         modifier = Modifier.size(64.dp),
         tint = MaterialTheme.colorScheme.primary
      )

      Spacer(modifier = Modifier.height(16.dp))

      Text(
         text = "Premium Feature",
         style = MaterialTheme.typography.headlineMedium,
         fontWeight = FontWeight.Bold,
         textAlign = androidx.compose.ui.text.style.TextAlign.Center
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
         text = "Access to internship opportunities is available exclusively for premium members.",
         style = MaterialTheme.typography.bodyLarge,
         textAlign = androidx.compose.ui.text.style.TextAlign.Center,
         color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(24.dp))

      Button(
         onClick = onUpgradeClick,
         modifier = Modifier.fillMaxWidth(0.8f)
      ) {
         Text(
            text = "Upgrade to Premium",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 4.dp)
         )
      }
   }
}

@Composable
private fun SearchFilterBar(
   searchQuery: String,
   onSearchQueryChange: (String) -> Unit,
   isFilterApplied: Boolean,
   onFilterIconClick: () -> Unit,
   modifier: Modifier = Modifier
) {
   Row(
      modifier = modifier.padding(16.dp),
      horizontalArrangement = Arrangement.spacedBy(16.dp),
      verticalAlignment = Alignment.CenterVertically
   ) {
      OutlinedTextField(
         value = searchQuery,
         onValueChange = onSearchQueryChange,
         modifier = Modifier.weight(1f),
         placeholder = { Text("Search internships...") },
         leadingIcon = {
            Icon(
               imageVector = Icons.Default.Search,
               contentDescription = "Search"
            )
         },
         trailingIcon = {
            if (searchQuery.isNotEmpty()) {
               IconButton(onClick = { onSearchQueryChange("") }) {
                  Icon(
                     imageVector = Icons.Default.Clear,
                     contentDescription = "Clear"
                  )
               }
            }
         },
         singleLine = true,
         shape = RoundedCornerShape(12.dp)
      )

      FilterButton(
         isActive = isFilterApplied,
         onClick = onFilterIconClick
      )
   }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InternshipFilterSheet(
   onDismissRequest: () -> Unit,
   onApplyFilters: (InternshipFilters) -> Unit,
   initialFilters: InternshipFilters = InternshipFilters(),
   availableCities: List<String> = emptyList(),
   availableDurations: List<String> = emptyList(),
   availableSkills: List<String> = emptyList()
) {
   var filters by remember { mutableStateOf(initialFilters) }
   var selectedSkills by remember { mutableStateOf(initialFilters.skills.toSet()) }

   MyBaseModalSheet(
      onDismissRequest = onDismissRequest
   ) {
      Column(
         modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
         verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
         Text(
            text = "Filter Internships",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
         )

         Divider()

         // Payment Filter
         Text(
            text = "Payment",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
         )
         Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
         ) {
            FilterChip(
               selected = filters.showPaid,
               onClick = { filters = filters.copy(showPaid = !filters.showPaid) },
               label = { Text("Paid") }
            )
            FilterChip(
               selected = filters.showUnpaid,
               onClick = { filters = filters.copy(showUnpaid = !filters.showUnpaid) },
               label = { Text("Unpaid") }
            )
         }

         // Location Filter
         if (availableCities.isNotEmpty()) {
            Text(
               text = "Location",
               style = MaterialTheme.typography.titleMedium,
               fontWeight = FontWeight.Medium
            )
            FlowRow(
               horizontalArrangement = Arrangement.spacedBy(8.dp),
               verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
               FilterChip(
                  selected = filters.city.isEmpty(),
                  onClick = { filters = filters.copy(city = "") },
                  label = { Text("Any") }
               )
               availableCities.forEach { city ->
                  FilterChip(
                     selected = city == filters.city,
                     onClick = { filters = filters.copy(city = if (filters.city == city) "" else city) },
                     label = { Text(city) }
                  )
               }
            }
         }

         // Duration Filter
         if (availableDurations.isNotEmpty()) {
            Text(
               text = "Duration",
               style = MaterialTheme.typography.titleMedium,
               fontWeight = FontWeight.Medium
            )
            FlowRow(
               horizontalArrangement = Arrangement.spacedBy(8.dp),
               verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
               FilterChip(
                  selected = filters.duration == null,
                  onClick = { filters = filters.copy(duration = null) },
                  label = { Text("Any") }
               )
               availableDurations.forEach { duration ->
                  FilterChip(
                     selected = duration == filters.duration,
                     onClick = { filters = filters.copy(duration = if (filters.duration == duration) null else duration) },
                     label = { Text(duration) }
                  )
               }
            }
         }

         // Skills Filter
         if (availableSkills.isNotEmpty()) {
            Text(
               text = "Skills",
               style = MaterialTheme.typography.titleMedium,
               fontWeight = FontWeight.Medium
            )

            FlowRow(
               horizontalArrangement = Arrangement.spacedBy(8.dp),
               verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
               availableSkills.forEach { skill ->
                  FilterChip(
                     selected = skill in selectedSkills,
                     onClick = {
                        selectedSkills = if (skill in selectedSkills) {
                           selectedSkills - skill
                        } else {
                           selectedSkills + skill
                        }
                        filters = filters.copy(skills = selectedSkills.toList())
                     },
                     label = { Text(skill) }
                  )
               }
            }
         }

         Spacer(modifier = Modifier.height(16.dp))

         // Action buttons
         Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
         ) {
            OutlinedButton(
               onClick = {
                  filters = InternshipFilters()
                  selectedSkills = emptySet()
               },
               modifier = Modifier.weight(1f)
            ) {
               Text("Reset")
            }
            Button(
               onClick = {
                  onApplyFilters(filters)
                  onDismissRequest()
               },
               modifier = Modifier.weight(1f)
            ) {
               Text("Apply")
            }
         }
      }
   }
}

data class InternshipFilters(
   val showPaid: Boolean = true,
   val showUnpaid: Boolean = true,
   val city: String = "",
   val duration: String? = null,
   val skills: List<String> = emptyList()
) {
   val isAnyFilterApplied: Boolean
      get() = !showPaid || !showUnpaid || city.isNotEmpty() || duration != null || skills.isNotEmpty()
}

@Composable
fun SkillChip(skill: String) {
   Surface(
      shape = RoundedCornerShape(50),
      color = MaterialTheme.colorScheme.secondaryContainer
   ) {
      Text(
         text = skill,
         style = MaterialTheme.typography.bodySmall,
         color = MaterialTheme.colorScheme.onSecondaryContainer,
         modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
      )
   }
}