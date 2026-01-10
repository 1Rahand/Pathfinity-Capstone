package ui.screens.course

import CourseVideoScreenRoute
import CreatorDetailRoute
import EditProfileRoute
import LiveScreenRoute
import PaymentGraph
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import data.wrapResultRepo
import database.MyDao
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.koinInject
import presentation.StringResources
import supabase.Course
import supabase.CourseReview
import supabase.CourseVideo
import supabase.CourseVideoProgress
import supabase.LiveSession
import supabase.UserContentCreator
import ui.components.MyTopAppBar
import ui.components.buttons.MyButton
import ui.components.environment.MyLocalUserStudent
import ui.components.environment.currentAppLang
import ui.theme.red
import kotlin.math.abs
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Format a time instant as a human-readable relative time (e.g., "5 minutes ago")
 */
private fun formatTimeAgo(instant: Instant): String {
    val now = Clock.System.now()
    val duration = now - instant

    return when {
        duration < 1.minutes -> "just now"
        duration < 60.minutes -> "${duration.inWholeMinutes} ${if (duration.inWholeMinutes == 1L) "minute" else "minutes"} ago"
        duration < 24.hours -> "${duration.inWholeHours} ${if (duration.inWholeHours == 1L) "hour" else "hours"} ago"
        duration < 30.days -> "${duration.inWholeDays} ${if (duration.inWholeDays == 1L) "day" else "days"} ago"
        else -> {
            val months = (duration.inWholeDays / 30).toInt()
            if (months < 12) {
                "$months ${if (months == 1) "month" else "months"} ago"
            } else {
                val years = months / 12
                "$years ${if (years == 1) "year" else "years"} ago"
            }
        }
    }
}

/**
 * Format a time instant as a readable date and time
 */
private fun formatDateTime(instant: Instant): String {
    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val month = when (dateTime.monthNumber) {
        1 -> "Jan"; 2 -> "Feb"; 3 -> "Mar"; 4 -> "Apr"; 5 -> "May"; 6 -> "Jun"
        7 -> "Jul"; 8 -> "Aug"; 9 -> "Sep"; 10 -> "Oct"; 11 -> "Nov"; 12 -> "Dec"
        else -> "???"
    }

    val hour = dateTime.hour % 12
    val formattedHour = if (hour == 0) 12 else hour
    val amPm = if (dateTime.hour < 12) "AM" else "PM"
    val minutes = dateTime.minute.toString().padStart(2, '0')

    return "$month ${dateTime.dayOfMonth}, ${dateTime.year} at $formattedHour:$minutes $amPm"
}

@OptIn(ExperimentalUuidApi::class)
@Composable
fun CourseDetailScreen(
   navController: NavController,
   dao: MyDao,
   courseId: String,
   supabase: SupabaseClient
) {
   val user = MyLocalUserStudent.current
   val isUserPremium = user.premium
   val course by dao.getCourseByIdFlow(courseId).collectAsStateWithLifecycle(null)
   val creator by dao.getUserContentCreatorByIdFlow(course?.creatorId).collectAsStateWithLifecycle(null)
   val videos by dao.getCourseVideosByIdFlow(courseId).collectAsStateWithLifecycle(emptyList())
   val videoProgresses by dao.getAllCourseVideoProgress().map { it.associateBy { it.videoId } }.collectAsStateWithLifecycle(mapOf())
   val lives by dao.getAllLiveSessions().map { it.groupBy { it.courseId } }.collectAsStateWithLifecycle(mapOf())
   val scope = rememberCoroutineScope()
   val snackbarHostState = remember { SnackbarHostState() }
   var videoLoading by remember { mutableStateOf<CourseVideo?>(null) }

   val courseReviews by dao.getCourseReviewsByCourseIdFlow(courseId).collectAsStateWithLifecycle(emptyList())

   val currentUserReview = courseReviews.firstOrNull { it.studentId == user.id }
   var reviewRating by remember { mutableStateOf(currentUserReview?.rating?.toFloat() ?: 0f) }
   var reviewComment by remember { mutableStateOf(currentUserReview?.reviewText ?: "") }

   val currentLiveSessions = lives[courseId] ?: emptyList()

   val onSubmitReview: () -> Unit = {
      if (reviewRating > 0)

      scope.launch {
         wrapResultRepo {
            val reviewToSubmit = currentUserReview?.copy(
               reviewText = reviewComment,
               rating = reviewRating.toInt(),
               updatedAt = Clock.System.now()
            ) ?: CourseReview(
               id = Uuid.random().toHexDashString(),
               courseId = courseId,
               studentId = user.id,
               reviewText = reviewComment,
               rating = reviewRating.toInt(),
               createdAt = Clock.System.now(),
               updatedAt = Clock.System.now()
            )

            val result = supabase.from(CourseReview.TABLE_NAME).upsert(reviewToSubmit) {
               select()
               limit(1)
               single()
            }.decodeAs<CourseReview>()

            dao.upsertCourseReview(result)
         }.onSuccess {
            snackbarHostState.showSnackbar("✅ Review submitted successfully")
         }.onError {
            snackbarHostState.showSnackbar("❌ Failed to submit review")
         }
      }
   }

   val onCheckedChange: (CourseVideo, Boolean) -> Unit = { video, new ->
      if (!user.premium && !video.isFreePreview) {
         if (user.email.isNullOrEmpty()) {
            navController.navigate(EditProfileRoute)
         } else {
            navController.navigate(PaymentGraph)
         }
      } else {
         scope.launch {
            videoLoading = video
            val progress = videoProgresses[video.id]
            wrapResultRepo {
               val newProgress = progress?.copy(completed = new) ?: CourseVideoProgress(
                  videoId = video.id,
                  completed = new,
                  studentId = user.id,
                  createdAt = Clock.System.now(),
                  updatedAt = Clock.System.now()
               )
               val result = supabase.from(CourseVideoProgress.TABLE_NAME).upsert(newProgress) {
                  select()
                  limit(1)
                  single()
               }.decodeAs<CourseVideoProgress>()

               dao.upsertCourseVideoProgress(result)
            }.onSuccess {
               // Successfully updated progress
            }.onError {
               launch {
                  snackbarHostState.showSnackbar("❌ Error")
               }
            }
            videoLoading = null
         }
      }
   }

   val onJoinLiveSession: (LiveSession) -> Unit = { liveSession ->
      if (!user.premium) {
         if (user.email.isNullOrEmpty()) {
            navController.navigate(EditProfileRoute)
         } else {
            navController.navigate(PaymentGraph)
         }
      } else {
         scope.launch {
            navController.navigate(LiveScreenRoute(liveSession.channelName))
         }
      }
   }

   Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = {
         MyTopAppBar(
            title = StringResources.course(currentAppLang()),
            onBackClick = {
               navController.navigateUp()
            }
         )
      },
      snackbarHost = {
         SnackbarHost(
            snackbarHostState
         )
      }
   ) { padding ->
      if (course == null) {
         // Loading state
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
               .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
         ) {
            // Course header section
            item {
               CourseHeader(
                  course = course!!,
                  creator = creator,
                  averageRating = if (courseReviews.isNotEmpty()) courseReviews.map { it.rating }.average() else null,
                  reviewCount = courseReviews.size,
                  onCreatorClick = { navController.navigate(CreatorDetailRoute(it.id)) }
               )
            }

            // Videos section
            item {
               Text(
                  text = "Course Content",
                  style = MaterialTheme.typography.titleLarge,
                  modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
               )
            }

            if (videos.isEmpty()) {
               item {
                  Box(
                     modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                     contentAlignment = Alignment.Center
                  ) {
                     Text(
                        text = "No videos available for this course",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                     )
                  }
               }
            } else {
               items(videos.sortedBy { it.sequenceNumber }) { video ->
                  VideoItem(
                     video = video,
                     onClick = {
                        if (!user.premium && !video.isFreePreview) {
                           if (user.email.isNullOrEmpty()) {
                              navController.navigate(EditProfileRoute)
                           } else {
                              navController.navigate(PaymentGraph)
                           }
                        } else {
                           navController.navigate(CourseVideoScreenRoute(video.id))
                        }
                     },
                     progress = videoProgresses[video.id],
                     onCheckedChange = { onCheckedChange(video, it) },
                     isLoading = videoLoading == video,
                     userCanWatch = isUserPremium || video.isFreePreview
                  )
               }
            }

            // Live Sessions section
            item {
               Spacer(modifier = Modifier.height(16.dp))
               HorizontalDivider()
               Text(
                  text = "Live Sessions",
                  style = MaterialTheme.typography.titleLarge,
                  modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
               )
            }

            if (currentLiveSessions.isEmpty()) {
               item {
                  Box(
                     modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                     contentAlignment = Alignment.Center
                  ) {
                     Text(
                        text = "No live sessions available for this course",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                     )
                  }
               }
            } else {
               items(currentLiveSessions) { liveSession ->
                  LiveSessionItem(
                     liveSession = liveSession,
                     onJoinClick = { onJoinLiveSession(liveSession) }
                  )
               }
            }

            // Reviews section
            item {
               Spacer(modifier = Modifier.height(16.dp))
               HorizontalDivider()
               Text(
                  text = "Reviews",
                  style = MaterialTheme.typography.titleLarge,
                  modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
               )
            }

            // Average rating display
            item {
               if (courseReviews.isNotEmpty()) {
                  val avgRating = courseReviews.map { it.rating }.average()
                  AverageRatingDisplay(
                     averageRating = avgRating,
                     reviewCount = courseReviews.size,
                     modifier = Modifier.padding(horizontal = 16.dp)
                  )
               }
            }

            // User's review form
            item {
               ReviewForm(
                  rating = reviewRating,
                  comment = reviewComment,
                  onRatingChange = { reviewRating = it },
                  onCommentChange = { reviewComment = it },
                  onSubmit = onSubmitReview,
                  isEditing = currentUserReview != null,
                  modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
               )
            }

            // Other users' reviews
            if (courseReviews.isNotEmpty()) {
               item {
                  Text(
                     text = "Student Reviews",
                     style = MaterialTheme.typography.titleMedium,
                     modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                  )
               }

               // Filter out current user's review and display others
               val otherReviews = courseReviews.filter { it.studentId != user.id }
               if (otherReviews.isEmpty()) {
                  item {
                     Text(
                        text = "No reviews from other students yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                     )
                  }
               } else {
                  items(otherReviews) { review ->
                     ReviewItem(
                        review = review,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                     )
                  }
               }
            }

            // Bottom spacing
            item {
               Spacer(modifier = Modifier.height(24.dp))
            }
         }
      }
   }
}

@Composable
private fun CourseHeader(
   course: Course,
   creator: UserContentCreator?,
   averageRating: Double? = null,
   reviewCount: Int = 0,
   onCreatorClick: (UserContentCreator) -> Unit = {}
) {
   Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
   ) {
      // Course thumbnail
      course.thumbnailUrl?.let { url ->
         Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
         ) {
            AsyncImage(
               model = url,
               contentDescription = "Course thumbnail",
               modifier = Modifier
                  .widthIn(max = 600.dp) // Maximum width for desktop screens
                  .fillMaxWidth() // Fill width up to the maximum
                  .aspectRatio(16f / 9f) // 16:9 aspect ratio
                  .clip(RoundedCornerShape(12.dp)),
               contentScale = ContentScale.Crop
            )
         }
      }

      // Course title with rating
      Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
         Text(
            text = course.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
         )

         // Show average rating if available
         if (averageRating != null) {
            Row(
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
               RatingBar(
                  rating = averageRating.toFloat(),
                  starSize = 20.dp,
                  interactive = false
               )

               // Format rating with one decimal place
               val ratingText = "${(averageRating * 10).toInt() / 10.0}"
               Text(
                  text = "$ratingText ($reviewCount ${if (reviewCount == 1) "review" else "reviews"})",
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
               )
            }
         }
      }

      // Course description
      course.description?.let { description ->
         Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp)
         )
      }

      // Creator info
      creator?.let {
         CreatorChip(
            creator = it,
            onClick = { onCreatorClick(it) },
            modifier = Modifier.padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = 4.dp
         )
      }

      HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
   }
}

@Composable
private fun VideoItem(
   video: CourseVideo,
   onClick: () -> Unit,
   onCheckedChange: (Boolean) -> Unit = {},
   progress: CourseVideoProgress? = null,
   isLoading: Boolean,
   userCanWatch: Boolean = true
) {
   Card(
      modifier = Modifier
         .fillMaxWidth()
         .padding(horizontal = 16.dp, vertical = 4.dp)
         .clickable(onClick = onClick),
      shape = RoundedCornerShape(8.dp),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
   ) {
      Row(
         modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
         verticalAlignment = Alignment.CenterVertically,
         horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
         // Video number
         Box(
            modifier = Modifier
               .size(32.dp)
               .clip(CircleShape)
               .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
         ) {
            Text(
               text = video.sequenceNumber.toString(),
               color = MaterialTheme.colorScheme.onPrimary,
               fontWeight = FontWeight.Bold
            )
         }

         // Video thumbnail
         video.thumbnailUrl?.let { url ->
            AsyncImage(
               model = url,
               contentDescription = "Video thumbnail",
               modifier = Modifier
                  .size(64.dp)
                  .clip(RoundedCornerShape(8.dp)),
               contentScale = ContentScale.Crop
            )
         } ?: run {
            Box(
               modifier = Modifier
                  .size(64.dp)
                  .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            )
         }

         Column(
            modifier = Modifier.weight(1f)
         ) {
            Text(
               text = video.title,
               style = MaterialTheme.typography.bodyLarge,
               fontWeight = FontWeight.Medium,
               maxLines = 2,
               overflow = TextOverflow.Ellipsis
            )

            Text(
               text = video.description,
               style = MaterialTheme.typography.bodyMedium,
               color = MaterialTheme.colorScheme.onSurfaceVariant,
               maxLines = 1,
               overflow = TextOverflow.Ellipsis
            )
         }

         if (userCanWatch) {
            Row(
               verticalAlignment = Alignment.CenterVertically
            ) {
               AnimatedVisibility(isLoading) {
                  CircularProgressIndicator()
               }
               Switch(
                  modifier = Modifier.padding(horizontal = 16.dp),
                  checked = (progress?.completed ?: false),
                  onCheckedChange = onCheckedChange
               )

               Icon(
                  imageVector = Icons.Default.PlayArrow,
                  contentDescription = "Play video",
                  tint = MaterialTheme.colorScheme.primary
               )
            }
         } else {
            MyButton(
               text = "Unlock",
               onClick = onClick,
               modifier = Modifier
                  .padding(horizontal = 16.dp),
               containerColor = MaterialTheme.colorScheme.primary,
               contentColor = MaterialTheme.colorScheme.onPrimary
            )
         }
      }
   }
}

@Composable
private fun LiveSessionItem(
   liveSession: LiveSession,
   onJoinClick: () -> Unit
) {
   Card(
      modifier = Modifier
         .fillMaxWidth()
         .padding(horizontal = 16.dp, vertical = 4.dp),
      shape = RoundedCornerShape(8.dp),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
   ) {
      Row(
         modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
         verticalAlignment = Alignment.CenterVertically,
         horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
         // Status indicator
         Box(
            modifier = Modifier
               .size(32.dp)
               .clip(CircleShape)
               .background(
                  when (liveSession.status) {
                     "active" -> MaterialTheme.colorScheme.red
                     "scheduled" -> MaterialTheme.colorScheme.tertiary
                     "ended" -> MaterialTheme.colorScheme.outline
                     "cancelled" -> MaterialTheme.colorScheme.error
                     else -> MaterialTheme.colorScheme.surfaceVariant
                  }
               ),
            contentAlignment = Alignment.Center
         ) {
            Text(
               text = when (liveSession.status) {
                  "active" -> "L"
                  "scheduled" -> "S"
                  "ended" -> "E"
                  "cancelled" -> "C"
                  else -> "?"
               },
               color = MaterialTheme.colorScheme.onPrimary,
               fontWeight = FontWeight.Bold
            )
         }

         Column(
            modifier = Modifier.weight(1f)
         ) {
            Text(
               text = liveSession.title,
               style = MaterialTheme.typography.bodyLarge,
               fontWeight = FontWeight.Medium,
               maxLines = 2,
               overflow = TextOverflow.Ellipsis
            )

            // Show session timing info
            val timeInfo = when {
               liveSession.status == "active" && liveSession.startedAt != null -> "Started ${formatTimeAgo(liveSession.startedAt)}"
               liveSession.status == "scheduled" && liveSession.scheduledAt != null -> "Scheduled for ${formatDateTime(liveSession.scheduledAt)}"
               liveSession.status == "ended" -> "Session ended"
               liveSession.status == "cancelled" -> "Session cancelled"
               else -> "Status: ${liveSession.status}"
            }

            Text(
               text = timeInfo,
               style = MaterialTheme.typography.bodyMedium,
               color = MaterialTheme.colorScheme.onSurfaceVariant,
               maxLines = 1,
               overflow = TextOverflow.Ellipsis
            )

            // Show viewer count if active
            if (liveSession.status == "active" && liveSession.viewerCount > 0) {
               Text(
                  text = "${liveSession.viewerCount} ${if (liveSession.viewerCount == 1) "viewer" else "viewers"}",
                  style = MaterialTheme.typography.labelMedium,
                  color = MaterialTheme.colorScheme.primary
               )
            }
         }

         // Join button only for active or scheduled sessions
         if (liveSession.status == "active" || liveSession.status == "scheduled") {
            MyButton(
               text = if (liveSession.status == "active") "Join" else "Remind",
               onClick = onJoinClick,
               containerColor = if (liveSession.status == "active")
                  MaterialTheme.colorScheme.primary
               else
                  MaterialTheme.colorScheme.secondaryContainer,
               contentColor = if (liveSession.status == "active")
                  MaterialTheme.colorScheme.onPrimary
               else
                  MaterialTheme.colorScheme.onSecondaryContainer
            )
         }
      }
   }
}

@Composable
private fun AverageRatingDisplay(
   averageRating: Double,
   reviewCount: Int,
   modifier: Modifier = Modifier
) {
   Card(
      modifier = modifier.fillMaxWidth(),
      shape = RoundedCornerShape(8.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
   ) {
      Row(
         modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
         verticalAlignment = Alignment.CenterVertically,
         horizontalArrangement = Arrangement.SpaceBetween
      ) {
         Column {
            Text(
               text = "Course Rating",
               style = MaterialTheme.typography.titleMedium
            )

            Text(
               text = "$reviewCount ${if (reviewCount == 1) "review" else "reviews"}",
               style = MaterialTheme.typography.bodyMedium,
               color = MaterialTheme.colorScheme.onSurfaceVariant
            )
         }

         Row(verticalAlignment = Alignment.CenterVertically) {
            // Display large rating number
            Text(
               text = "${(averageRating * 10).toInt() / 10.0}",
               style = MaterialTheme.typography.headlineMedium,
               fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.size(12.dp))

            // Display stars
            RatingBar(
               rating = averageRating.toFloat(),
               interactive = false,
               starSize = 24.dp
            )
         }
      }
   }
}

@Composable
private fun ReviewForm(
   rating: Float,
   comment: String,
   onRatingChange: (Float) -> Unit,
   onCommentChange: (String) -> Unit,
   onSubmit: () -> Unit,
   isEditing: Boolean,
   modifier: Modifier = Modifier
) {
   Card(
      modifier = modifier.fillMaxWidth(),
      shape = RoundedCornerShape(8.dp),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
   ) {
      Column(
         modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
         verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
         Text(
            text = if (isEditing) "Edit Your Review" else "Write a Review",
            style = MaterialTheme.typography.titleMedium
         )

         Column {
            Text(
               text = "Rating",
               style = MaterialTheme.typography.bodyMedium,
               modifier = Modifier.padding(bottom = 8.dp)
            )

            RatingBar(
               rating = rating,
               interactive = true,
               onRatingChanged = onRatingChange,
               starSize = 32.dp
            )
         }

         OutlinedTextField(
            value = comment,
            onValueChange = onCommentChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Your review (optional)") },
            minLines = 3,
            maxLines = 5
         )

         MyButton(
            text = if (isEditing) "Update Review" else "Submit Review",
            onClick = onSubmit,
            enabled = rating > 0f,
            modifier = Modifier.align(Alignment.End),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
         )
      }
   }
}

@Composable
private fun ReviewItem(
   review: CourseReview,
   modifier: Modifier = Modifier
) {
   Card(
      modifier = modifier.fillMaxWidth(),
      shape = RoundedCornerShape(8.dp),
      elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
   ) {
      Column(
         modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
         verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
         // User info row
         Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
         ) {
            // User avatar placeholder
            Box(
               modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.primary),
               contentAlignment = Alignment.Center
            ) {
               Text(
                  text = review.studentId.take(1).uppercase(),
                  color = MaterialTheme.colorScheme.onPrimary,
                  fontWeight = FontWeight.Bold
               )
            }

            Column {
               Text(
                  text = "Student",
                  style = MaterialTheme.typography.bodyLarge,
                  fontWeight = FontWeight.Medium
               )

               RatingBar(
                  rating = review.rating.toFloat(),
                  interactive = false,
                  starSize = 16.dp
               )
            }
         }

         // Review text
         if (!review.reviewText.isNullOrEmpty()) {
            Text(
               text = review.reviewText,
               style = MaterialTheme.typography.bodyMedium,
               modifier = Modifier.padding(top = 8.dp)
            )
         }
      }
   }
}

@Composable
private fun RatingBar(
   rating: Float,
   modifier: Modifier = Modifier,
   starSize: Dp = 24.dp,
   maxStars: Int = 5,
   interactive: Boolean = false,
   onRatingChanged: (Float) -> Unit = {}
) {
   Row(
      modifier = modifier,
      horizontalArrangement = Arrangement.spacedBy(4.dp)
   ) {
      for (i in 1..maxStars) {
         val isFilled = i <= rating
         val starIcon = if (isFilled) Icons.Filled.Star else Icons.Filled.StarOutline

         Icon(
            imageVector = starIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
               .size(starSize)
               .then(
                  if (interactive) {
                     Modifier.clickable { onRatingChanged(i.toFloat()) }
                  } else {
                     Modifier
                  }
               )
         )
      }
   }
}
