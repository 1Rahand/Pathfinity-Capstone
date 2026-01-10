package ui.screens.course

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.BarChart
import compose.icons.evaicons.outline.FileText
import compose.icons.evaicons.outline.PieChart
import supabase.Course
import supabase.CourseCategory
import supabase.UserContentCreator
import ui.components.CustomCircularProgress
import ui.components.modifier.myBackground
import ui.theme.blue
import ui.theme.green
import ui.theme.onRed
import ui.theme.red
import ui.theme.redContainer
import ui.theme.yellow

@Composable
fun CourseCard(
   course: Course,
   creator: UserContentCreator?,
   courseCategory: CourseCategory?,
   courseProgress : Double? = null,
   onClick: () -> Unit,
   onCreatorChipClick : (UserContentCreator) -> Unit,
   averageRating : Double? = null,
   modifier: Modifier = Modifier,
   hasLive : Boolean = false
) {
   Column(
      modifier = modifier
         .widthIn(max = 400.dp)
         .myBackground(onClick = onClick)
         .fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(12.dp)
   ) {
      // Thumbnail with 16:9 aspect ratio
      course.thumbnailUrl?.let {
         Box(
            modifier = Modifier
               .fillMaxWidth()
               .aspectRatio(16f/9f)
         ){
            AsyncImage(
               model = it,
               contentDescription = null,
               contentScale = ContentScale.Crop,
               modifier = Modifier
                  .fillMaxWidth()
                  .aspectRatio(16f/9f)
            )

            Box(
               modifier = Modifier
                  .fillMaxSize()
                  .background(
                     brush = Brush.verticalGradient(
                        colors = listOf(
                           Color.Black.copy(alpha = 0.6f),  // More opaque at bottom
                           Color.Transparent               // Transparent at top
                        ),
                        startY = Float.POSITIVE_INFINITY,  // Bottom
                        endY = 0f                          // Top
                     )
                  )
            )

            creator?.let {
               CreatorChip(
                  modifier = Modifier
                     .align(Alignment.BottomStart),
                  creator = it,
                  onClick = {onCreatorChipClick(it)}
               )
            }

            if (hasLive){
               Text(
                  text = "LIVE",
                  style = MaterialTheme.typography.titleSmall,
                  color = MaterialTheme.colorScheme.onRed,
                  modifier = Modifier
                     .align(Alignment.BottomEnd)
                     .padding(end = 8.dp, bottom = 8.dp)
                     .myBackground(containerColor = MaterialTheme.colorScheme.red, shape = RoundedCornerShape(4.dp))
                     .padding(horizontal = 16.dp, vertical = 2.dp)
               )
            }

            // Membership type chip
            MembershipChip(
               modifier = Modifier
                  .align(Alignment.TopEnd)
                  .padding(0.dp),
               membershipType = course.membershipType
            )
         }
      }

      // Content section with fixed padding
      Column(
         modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp, top = 8.dp),
         verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
         // Upper content: Title and Creator
         // Creator name as a chip with avatar placeholder

         // Title

         Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
         ) {
            Text(
               text = course.title,
               style = MaterialTheme.typography.titleLarge,
               fontWeight = FontWeight.Bold,
               maxLines = 2,
               overflow = TextOverflow.Ellipsis
            )

            courseProgress?.let {
               CustomCircularProgress(
                  modifier = Modifier
                     .size(40.dp)
                     .align(Alignment.CenterVertically),
                  progress = it.toFloat() / 100f,
                  color = MaterialTheme.colorScheme.primary,
                  strokeWidth = 4.dp,
                  centerContent = {
                     Text(
                        text = "${it.toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface
                     )
                  }
               )
            }

         }


         // Always show description area
         courseCategory?.let {
            Row(
               horizontalArrangement = Arrangement.spacedBy(8.dp),
               modifier = Modifier.alpha(0.75F)
            ) {
               Icon(
                  imageVector = EvaIcons.Outline.PieChart,
                  contentDescription = null,
                  modifier = Modifier.size(20.dp)
               )
               Text(
                  text = it.name,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  maxLines = 2,
                  overflow = TextOverflow.Ellipsis
               )
            }
         }


         Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.alpha(0.75F)
         ) {
            Icon(
               imageVector = EvaIcons.Outline.FileText,
               contentDescription = null,
               modifier = Modifier.size(20.dp)
            )
            Text(
               text = course.description ?: "",
               style = MaterialTheme.typography.bodyMedium,
               color = MaterialTheme.colorScheme.onSurfaceVariant,
               maxLines = 2,
               overflow = TextOverflow.Ellipsis
            )
         }

         Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.alpha(0.75F)
         ) {
            Icon(
               imageVector = EvaIcons.Fill.BarChart,
               contentDescription = null,
               modifier = Modifier.size(20.dp)
            )
            Text(
               text = course.difficulty ?: "",
               style = MaterialTheme.typography.bodyMedium,
               color = MaterialTheme.colorScheme.onSurfaceVariant,
               maxLines = 2,
               overflow = TextOverflow.Ellipsis
            )
         }

         averageRating?.let {
            Row(
               horizontalArrangement = Arrangement.spacedBy(8.dp),
               verticalAlignment = Alignment.CenterVertically,
               modifier = Modifier.alpha(0.75F)
            ) {
               Icon(
                  imageVector = Icons.Default.Star,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.yellow,
                  modifier = Modifier.size(20.dp)
               )

               Row(verticalAlignment = Alignment.CenterVertically) {
                  val ratingInt = (it * 10).toInt()
                  val formattedRating = "${ratingInt / 10}.${ratingInt % 10}"

                  Text(
                     text = formattedRating,
                     style = MaterialTheme.typography.bodyMedium,
                     fontWeight = FontWeight.Medium,
                     color = MaterialTheme.colorScheme.onSurfaceVariant
                  )

                  // Display star rating
                  RatingBar(
                     rating = it.toFloat(),
                     modifier = Modifier.padding(start = 8.dp)
                  )
               }
            }
         }
      }
   }
}

@Composable
private fun RatingBar(
   rating: Float,
   maxStars: Int = 5,
   modifier: Modifier = Modifier,
   starSize: Dp = 14.dp,
   starSpacing: Dp = 2.dp
) {
   Row(
      modifier = modifier,
      horizontalArrangement = Arrangement.spacedBy(starSpacing)
   ) {
      val fullStars = rating.toInt()
      val hasHalfStar = rating - fullStars >= 0.5f

      repeat(fullStars) {
         Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.yellow,
            modifier = Modifier.size(starSize)
         )
      }

      if (hasHalfStar) {
         Icon(
            imageVector = Icons.Default.StarHalf,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.yellow,
            modifier = Modifier.size(starSize)
         )
      }

      repeat(maxStars - fullStars - if (hasHalfStar) 1 else 0) {
         Icon(
            imageVector = Icons.Default.StarOutline,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.yellow.copy(alpha = 0.5f),
            modifier = Modifier.size(starSize)
         )
      }
   }
}

@Composable
fun MembershipChip(
   modifier: Modifier = Modifier,
   membershipType: Course.MembershipType
){
   val brush : Brush = when (membershipType) {
      Course.MembershipType.FREE -> Brush.linearGradient(
         colors = listOf(
            MaterialTheme.colorScheme.green,
            MaterialTheme.colorScheme.blue
         )
      )
      Course.MembershipType.PRO -> Brush.linearGradient(
         colors = listOf(
            MaterialTheme.colorScheme.red,
            MaterialTheme.colorScheme.blue
         )
      )
   }
   Text(
      text =  membershipType.name,
      style = MaterialTheme.typography.titleSmall,
      color = Color.White,
      modifier = modifier
         .clip(RoundedCornerShape(topEnd = 0.dp , topStart = 0.dp , bottomStart = 8.dp , bottomEnd = 0.dp))
         .drawBehind {
            drawRect(brush = brush)
         }
         .padding(horizontal = 16.dp, vertical = 8.dp)
   )
}