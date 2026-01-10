package ui.screens.internship

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.outlined.AddLocation
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import supabase.Internship
import supabase.UserCompany
import ui.components.layout.ColumnC
import ui.components.layout.RowC
import ui.components.modifier.myBackground

@Composable
fun InternshipCard(
   internship: Internship,
   company: UserCompany?,
   onClick: () -> Unit,
   onCompanyClick: (UserCompany) -> Unit,
   modifier: Modifier = Modifier
) {
   Column(
      modifier = modifier
         .widthIn(max = 400.dp)
         .myBackground(onClick = onClick)
         .fillMaxWidth()
         .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
   ) {
      // Header with title and status
      Column(
         verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
         Text(
            text = internship.title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
         )

         // Brief description
         Text(
            text = internship.description,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
         )
      }

      // Company name
      company?.let {
         CompanyChip(
            company = it,
            onClick = {
               onCompanyClick(it)
            }
         )
      }

      // Duration and skills
      Row(
         modifier = Modifier.fillMaxWidth(),
         horizontalArrangement = Arrangement.SpaceBetween,
         verticalAlignment = Alignment.CenterVertically
      ) {
         RowC(spacing = 8.dp) {
            Icon(
               imageVector = Icons.Default.AccessTime,
               contentDescription = null,
               modifier = Modifier.size(16.dp),
               tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
               text = internship.duration,
               style = MaterialTheme.typography.bodySmall,
               color = MaterialTheme.colorScheme.onSurfaceVariant
            )
         }

         // Show skills badges
         Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
         ) {
            internship.skills.take(2).forEach { skill ->
               SkillChip(skill = skill)
            }
            if (internship.skills.size > 2) {
               SkillChip(skill = "+${internship.skills.size - 2}")
            }
         }
      }

      // City and Paid/Unpaid status
      Row(
         modifier = Modifier.fillMaxWidth(),
         horizontalArrangement = Arrangement.SpaceBetween,
         verticalAlignment = Alignment.CenterVertically
      ) {
         // City
         RowC(spacing = 8.dp) {
            Icon(
               imageVector = Icons.Outlined.LocationOn,
               contentDescription = null,
               modifier = Modifier.size(16.dp),
               tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
               text = internship.city ?: "Unknown City",
               style = MaterialTheme.typography.bodySmall,
               color = MaterialTheme.colorScheme.onSurfaceVariant
            )
         }

         // Paid/Unpaid status
         RowC(spacing = 8.dp) {
            Icon(
               imageVector = Icons.Outlined.Payments,
               contentDescription = null,
               modifier = Modifier.size(16.dp),
               tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
               text = if (internship.isPaid) "Paid" else "Unpaid",
               style = MaterialTheme.typography.bodySmall,
               color = MaterialTheme.colorScheme.onSurfaceVariant
            )
         }
      }
   }
}