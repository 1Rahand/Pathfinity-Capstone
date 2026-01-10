package ui.screens.course

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import supabase.UserContentCreator
import ui.components.layout.RowC
import ui.components.modifier.myBackground

@Composable
fun CreatorChip(
   modifier: Modifier = Modifier,
   creator: UserContentCreator,
   shape : Shape = RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, bottomEnd = 0.dp, topEnd = 16.dp),
   elevation : Dp = 0.dp,
   onClick: () -> Unit = {}
) {
   RowC(
      modifier = modifier
         .myBackground(
            onClick = onClick,
            shape = shape,
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.75F),
            elevation = elevation
         )
         .padding(horizontal = 8.dp, vertical = 8.dp)
         .padding(end = 4.dp),
      spacing = 8.dp
   ) {
      AsyncImage(
         model = creator.profilePictureUrl,
         contentDescription = null,
         contentScale = ContentScale.Crop,
         modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(Color.Gray)
      )
      Text(
         text = "${creator.firstName} ${creator.lastName}",
         maxLines = 1,
         overflow = TextOverflow.Ellipsis,
         style = MaterialTheme.typography.bodySmall,
         fontWeight = FontWeight.Bold
      )
   }
}