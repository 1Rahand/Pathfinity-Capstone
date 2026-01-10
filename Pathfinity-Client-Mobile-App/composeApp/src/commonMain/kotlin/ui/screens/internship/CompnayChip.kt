package ui.screens.internship

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import supabase.UserCompany
import supabase.UserContentCreator
import ui.components.layout.RowC
import ui.components.modifier.myBackground
import ui.theme.surface2

@Composable
fun CompanyChip(
   modifier: Modifier = Modifier,
   company: UserCompany,
   onClick: () -> Unit = {}
) {
   RowC(
      modifier = modifier
         .myBackground(
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.surface2,
         )
         .padding(horizontal = 8.dp, vertical = 8.dp),
      spacing = 8.dp
   ) {
      AsyncImage(
         model = company.profilePictureUrl,
         contentDescription = null,
         contentScale = ContentScale.Crop,
         modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(Color.Gray)
      )
      Text(
         text = company.companyName,
         maxLines = 1,
         overflow = TextOverflow.Ellipsis,
         style = MaterialTheme.typography.bodySmall,
         fontWeight = FontWeight.Bold
      )
   }
}