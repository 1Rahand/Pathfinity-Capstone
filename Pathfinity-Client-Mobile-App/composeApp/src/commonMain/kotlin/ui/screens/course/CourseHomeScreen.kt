package ui.screens.course

import CourseDetailRoute
import CreatorDetailRoute
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import database.MyDao
import domain.Lang
import kotlinx.coroutines.flow.map
import presentation.StringResources
import supabase.Course
import ui.components.MyBaseModalSheet
import ui.components.bottomNavBarHeight
import ui.components.buttons.FilterButton
import ui.components.environment.currentAppLang
import ui.components.modifier.noIndicationClickable

@Composable
fun CourseHomeScreen(
   navController: NavController,
   dao: MyDao
) {
   val focusManager = LocalFocusManager.current
   val courses by dao.getAllCourses().collectAsStateWithLifecycle(null)
   val creators by dao.getAllUserContentCreators().map { it.associateBy { it.id } }.collectAsStateWithLifecycle(mapOf())
   val courseCategories by dao.getAllCourseCategories().map { it.associateBy { it.id } }.collectAsStateWithLifecycle(mapOf())
   val videos by dao.getAllCourseVideos().map { list -> list.groupBy { it.courseId } }.collectAsStateWithLifecycle(mapOf())
   val videoProgresses by dao.getAllCourseVideoProgress().map { it.associateBy { it.videoId } }.collectAsStateWithLifecycle(mapOf())
   val reviews by dao.getAllCourseReviews().map { it.groupBy { it.courseId } }.collectAsStateWithLifecycle(mapOf())
   val lives by dao.getAllLiveSessions().map { list -> list.groupBy { live -> live.courseId } }.collectAsStateWithLifecycle(mapOf())


   val appLang = currentAppLang()

   // Search query state
   var searchQuery by remember { mutableStateOf("") }

   // Filter state
   var showFilterSheet by remember { mutableStateOf(false) }
   var courseFilters by remember { mutableStateOf(CourseFilters()) }

   // Extract unique values from courses data for filters
   val allCategories by remember(courses, courseCategories) {
      derivedStateOf {
         courses?.mapNotNull { course ->
            course.categoryId?.let { id -> courseCategories[id]?.name }
         }?.filter { it.isNotBlank() }
            ?.distinct()
            ?.sorted() ?: emptyList()
      }
   }

   val allDifficulties by remember(courses) {
      derivedStateOf {
         courses?.mapNotNull { it.difficulty }
            ?.filter { it.isNotBlank() }
            ?.distinct()
            ?.sorted() ?: emptyList()
      }
   }

   val allCreators by remember(courses, creators) {
      derivedStateOf {
         courses?.mapNotNull { course ->
            creators[course.creatorId]?.let { "${it.firstName} ${it.lastName}" }
         }?.filter { it.isNotBlank() }
            ?.distinct()
            ?.sorted() ?: emptyList()
      }
   }

   // Filtered courses based on search and filter criteria
   val filteredCourses = remember(courses, searchQuery, courseFilters) {
      courses?.filter { course ->
         // Text search filter
         val matchesSearch = searchQuery.isEmpty() ||
                 course.title.contains(searchQuery, ignoreCase = true) ||
                 (course.description?.contains(searchQuery, ignoreCase = true) == true) ||
                 creators[course.creatorId]?.firstName?.contains(searchQuery, ignoreCase = true) == true ||
                 creators[course.creatorId]?.lastName?.contains(searchQuery, ignoreCase = true) == true

         // Apply other filters
         val matchesMembership = (courseFilters.showFree && course.membershipType == Course.MembershipType.FREE) ||
                 (courseFilters.showPro && course.membershipType == Course.MembershipType.PRO)

         val matchesCategory = courseFilters.category.isEmpty() ||
                 (course.categoryId?.let { id -> courseCategories[id]?.name } == courseFilters.category)

         val matchesDifficulty = courseFilters.difficulty.isEmpty() ||
                 course.difficulty == courseFilters.difficulty

         val creatorName = creators[course.creatorId]?.let { "${it.firstName} ${it.lastName}" }
         val matchesCreator = courseFilters.creator.isEmpty() ||
                 creatorName == courseFilters.creator

         matchesSearch && matchesMembership && matchesCategory && matchesDifficulty && matchesCreator
      } ?: emptyList()
   }

   Scaffold(
      modifier = Modifier
         .noIndicationClickable { focusManager.clearFocus() }
         .fillMaxSize()
   ) { paddingValues ->
      Column(
         modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(bottom = bottomNavBarHeight)
      ) {
         // Search and filter bar
         SearchFilterBar(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            isFilterApplied = courseFilters.isAnyFilterApplied,
            onFilterIconClick = { showFilterSheet = true },
            appLang = appLang
         )

         if (courses == null) {
            // Loading state
            Box(
               modifier = Modifier.fillMaxSize(),
               contentAlignment = Alignment.Center
            ) {
               CircularProgressIndicator()
            }
         } else if (filteredCourses.isEmpty()) {
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
                     imageVector = Icons.Filled.School,
                     contentDescription = null,
                     modifier = Modifier.size(64.dp),
                     tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                  )
                  Text(
                     text = "No courses found",
                     style = MaterialTheme.typography.titleMedium,
                     color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
               }
            }
         } else {
            // Courses list
            FlowRow(
               modifier = Modifier
                  .fillMaxSize()
                  .verticalScroll(rememberScrollState())
                  .padding(16.dp),
               verticalArrangement = Arrangement.spacedBy(16.dp),
               horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            ) {
               filteredCourses.forEach { course ->
                  val creator = creators[course.creatorId]
                  val courseProgress = videos[course.id]?.let {
                     val totalVideos = it.size
                     val completedVideos = it.count { video ->
                        videoProgresses[video.id]?.completed == true
                     }
                     if (totalVideos > 0) {
                        (completedVideos.toDouble() / totalVideos.toDouble() * 100.0)
                     } else {
                        0.0
                     }
                  }

                  val reviewss = reviews[course.id]
                  val averagreRating = reviewss?.map { it.rating }?.average()

                  CourseCard(
                     modifier = Modifier,
                     course = course,
                     creator = creator,
                     onClick = {
                        navController.navigate(CourseDetailRoute(course.id))
                     },
                     onCreatorChipClick = {
                        navController.navigate(CreatorDetailRoute(it.id))
                     },
                     courseCategory = courseCategories[course.categoryId],
                     courseProgress = courseProgress,
                     averageRating = averagreRating,
                     hasLive = lives[course.id]?.any { it.status == "active" } ?: false
                  )
               }
            }
         }
      }
   }

   // Show filter sheet if needed
   if (showFilterSheet) {
      CourseFilterSheet(
         onDismissRequest = { showFilterSheet = false },
         initialFilters = courseFilters,
         onApplyFilters = { filters ->
            courseFilters = filters
         },
         availableCategories = allCategories,
         availableDifficulties = allDifficulties,
         availableCreators = allCreators
      )
   }
}

@Composable
private fun SearchFilterBar(
   searchQuery: String,
   onSearchQueryChange: (String) -> Unit,
   isFilterApplied: Boolean,
   onFilterIconClick: () -> Unit,
   appLang: Lang,
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
         placeholder = { Text(StringResources.searchCourses(appLang)) },
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
fun CourseFilterSheet(
   onDismissRequest: () -> Unit,
   onApplyFilters: (CourseFilters) -> Unit,
   initialFilters: CourseFilters = CourseFilters(),
   availableCategories: List<String> = emptyList(),
   availableDifficulties: List<String> = emptyList(),
   availableCreators: List<String> = emptyList()
) {
   var filters by remember { mutableStateOf(initialFilters) }

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
            text = "Filter Courses",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
         )

         Divider()

         // Membership Type Filter
         Text(
            text = "Membership Type",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
         )
         Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
         ) {
            FilterChip(
               selected = filters.showFree,
               onClick = { filters = filters.copy(showFree = !filters.showFree) },
               label = { Text("Free") }
            )
            FilterChip(
               selected = filters.showPro,
               onClick = { filters = filters.copy(showPro = !filters.showPro) },
               label = { Text("Pro") }
            )
         }

         // Category Filter
         if (availableCategories.isNotEmpty()) {
            Text(
               text = "Category",
               style = MaterialTheme.typography.titleMedium,
               fontWeight = FontWeight.Medium
            )
            FlowRow(
               horizontalArrangement = Arrangement.spacedBy(8.dp),
               verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
               FilterChip(
                  selected = filters.category.isEmpty(),
                  onClick = { filters = filters.copy(category = "") },
                  label = { Text("Any") }
               )
               availableCategories.forEach { category ->
                  FilterChip(
                     selected = category == filters.category,
                     onClick = { filters = filters.copy(category = if (filters.category == category) "" else category) },
                     label = { Text(category) }
                  )
               }
            }
         }

         // Difficulty Filter
         if (availableDifficulties.isNotEmpty()) {
            Text(
               text = "Difficulty",
               style = MaterialTheme.typography.titleMedium,
               fontWeight = FontWeight.Medium
            )
            FlowRow(
               horizontalArrangement = Arrangement.spacedBy(8.dp),
               verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
               FilterChip(
                  selected = filters.difficulty.isEmpty(),
                  onClick = { filters = filters.copy(difficulty = "") },
                  label = { Text("Any") }
               )
               availableDifficulties.forEach { difficulty ->
                  FilterChip(
                     selected = difficulty == filters.difficulty,
                     onClick = { filters = filters.copy(difficulty = if (filters.difficulty == difficulty) "" else difficulty) },
                     label = { Text(difficulty) }
                  )
               }
            }
         }

         // Creator Filter
         if (availableCreators.isNotEmpty()) {
            Text(
               text = "Creator",
               style = MaterialTheme.typography.titleMedium,
               fontWeight = FontWeight.Medium
            )
            FlowRow(
               horizontalArrangement = Arrangement.spacedBy(8.dp),
               verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
               FilterChip(
                  selected = filters.creator.isEmpty(),
                  onClick = { filters = filters.copy(creator = "") },
                  label = { Text("Any") }
               )
               availableCreators.forEach { creator ->
                  FilterChip(
                     selected = creator == filters.creator,
                     onClick = { filters = filters.copy(creator = if (filters.creator == creator) "" else creator) },
                     label = { Text(creator) }
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
                  filters = CourseFilters()
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

data class CourseFilters(
   val showFree: Boolean = true,
   val showPro: Boolean = true,
   val category: String = "",
   val difficulty: String = "",
   val creator: String = ""
) {
   val isAnyFilterApplied: Boolean
      get() = !showFree || !showPro || category.isNotEmpty() || difficulty.isNotEmpty() || creator.isNotEmpty()
}
