package ui.screens.course

import CourseDetailRoute
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import database.MyDao
import presentation.StringResources
import ui.components.MyTopAppBar
import ui.components.environment.currentAppLang
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.coroutines.flow.map
import supabase.Course
import supabase.CourseCategory
import supabase.CourseReview
import supabase.CourseVideo
import supabase.CourseVideoProgress
import supabase.LiveSession
import supabase.UserContentCreator

@Composable
fun CreatorDetailScreen(
   navController: NavController,
   dao: MyDao,
   creatorId: String,
) {
   val creator by dao.getUserContentCreatorByIdFlow(creatorId).collectAsStateWithLifecycle(null)
   val courses by dao.getCoursesByCreatorIdFlow(creatorId).collectAsStateWithLifecycle(emptyList())
   val categories by dao.getAllCourseCategories().map { it.associateBy { it.id } }.collectAsStateWithLifecycle(mapOf())
   val videos by dao.getAllCourseVideos().map { list -> list.groupBy { it.courseId } }.collectAsStateWithLifecycle(mapOf())
   val videoProgresses by dao.getAllCourseVideoProgress().map { it.associateBy { it.videoId } }.collectAsStateWithLifecycle(mapOf())
   val reviews by dao.getAllCourseReviews().map { it.groupBy { it.courseId } }.collectAsStateWithLifecycle(mapOf())
   val lives by dao.getAllLiveSessions().map { list -> list.groupBy { live -> live.courseId } }.collectAsStateWithLifecycle(mapOf())

   Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = {
         MyTopAppBar(
            title = StringResources.creator(currentAppLang()),
            onBackClick = {
               navController.navigateUp()
            }
         )
      }
   ) { padding ->
      if (creator == null) {
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
               CreatorHeader(creator = creator!!)
            }

            // Course count indicator
            item {
               Text(
                  text = "${courses.size} ${StringResources.courses(currentAppLang())}",
                  style = MaterialTheme.typography.titleMedium,
                  modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
               )
            }

            // Courses Section Header
            item {
               Text(
                  text = "${StringResources.courses(currentAppLang())} ${StringResources.by(currentAppLang())} ${creator!!.firstName}",
                  style = MaterialTheme.typography.headlineSmall,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier
                     .padding(horizontal = 16.dp)
                     .padding(top = 8.dp)
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

                  AdaptiveCoursesGrid(
                     courses = courses,
                     categories = categories,
                     navController = navController,
                     columns = columns,
                     videos = videos,
                     videoProgresses = videoProgresses,
                     reviews = reviews,
                     lives = lives
                  )
               }
            }
         }
      }
   }
}

@Composable
private fun CreatorHeader(creator: UserContentCreator) {
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
         if (creator.profilePictureUrl != null) {
            AsyncImage(
               model = creator.profilePictureUrl,
               contentDescription = "${creator.firstName} ${creator.lastName}",
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
                  text = "${creator.firstName.first()}${creator.lastName.first()}",
                  style = MaterialTheme.typography.headlineLarge,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
               )
            }
         }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Creator name
      Text(
         text = "${creator.firstName} ${creator.lastName}",
         style = MaterialTheme.typography.headlineMedium,
         fontWeight = FontWeight.Bold,
         textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Creator bio
      creator.bio?.let {
         Text(
            text = it,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
         )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Contact information
      FlowRow(
         horizontalArrangement = Arrangement.spacedBy(12.dp , Alignment.CenterHorizontally),
         verticalArrangement = Arrangement.spacedBy(12.dp , Alignment.CenterVertically),
         modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
      ) {
         CreatorContactChip(
            icon = Icons.Default.Email,
            label = creator.email
         )

         creator.phone?.let {
            CreatorContactChip(
               icon = Icons.Default.Phone,
               label = it
            )
         }
      }

      HorizontalDivider(
         modifier = Modifier
            .padding(vertical = 16.dp)
            .padding(horizontal = 24.dp)
      )
   }
}

@Composable
fun CreatorContactChip(icon: ImageVector, label: String) {
   Surface(
      shape = RoundedCornerShape(50),
      tonalElevation = 1.dp,
      color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
   ) {
      Row(
         verticalAlignment = Alignment.CenterVertically,
         modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
         horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
         Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.primary
         )
         Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
         )
      }
   }
}

@Composable
private fun AdaptiveCoursesGrid(
   courses: List<Course>,
   categories : Map<String , CourseCategory>,
   navController: NavController,
   columns: Int,
   videos: Map<String, List<CourseVideo>>,
   videoProgresses: Map<String, CourseVideoProgress>,
   reviews : Map<String, List<CourseReview>>,
   lives: Map<String, List<LiveSession>>
) {
   if (courses.isEmpty()) {
      Box(
         modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
         contentAlignment = Alignment.Center
      ) {
         Text(
            text = "No courses available from this creator",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
         )
      }
   } else {
      StaggeredGrid(
         columns = columns,
         modifier = Modifier.fillMaxWidth()
      ) {
         courses.forEach { course ->
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
               course = course,
               creator = null,
               onClick = {
                  navController.navigate(CourseDetailRoute(course.id))
               },
               modifier = Modifier
                  .padding(8.dp)
                  .fillMaxWidth(),
               onCreatorChipClick = {},
               courseCategory = categories[course.categoryId],
               courseProgress = courseProgress,
               averageRating = averagreRating,
               hasLive = lives[course.id]?.any { it.status == "active" } ?: false
            )
         }
      }
   }
}

@Composable
fun StaggeredGrid(
   columns: Int,
   modifier: Modifier = Modifier,
   content: @Composable () -> Unit
) {
   Layout(
      content = content,
      modifier = modifier
   ) { measurables, constraints ->
      val columnWidths = IntArray(columns) { 0 }
      val columnHeights = IntArray(columns) { 0 }

      val placeables = measurables.mapIndexed { index, measurable ->
         // Find the column with the smallest height
         val column = columnHeights.withIndex().minBy { it.value }.index
         val placeable = measurable.measure(
            constraints.copy(
               maxWidth = constraints.maxWidth / columns,
               minWidth = constraints.minWidth / columns
            )
         )

         // Update the width and height for this column
         columnWidths[column] = maxOf(columnWidths[column], placeable.width)
         columnHeights[column] += placeable.height

         // Return the column and placeable
         column to placeable
      }

      val height = columnHeights.maxOrNull()?.coerceIn(constraints.minHeight, constraints.maxHeight) ?: constraints.minHeight
      val width = columnWidths.sum().coerceIn(constraints.minWidth, constraints.maxWidth)

      layout(width, height) {
         val columnYs = IntArray(columns) { 0 }

         placeables.forEach { (column, placeable) ->
            placeable.place(
               x = columnWidths.take(column).sum(),
               y = columnYs[column]
            )
            columnYs[column] += placeable.height
         }
      }
   }
}