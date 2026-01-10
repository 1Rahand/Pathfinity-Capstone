package ui.screens.mentorship

import AlumniChatRoute
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import database.MyDao
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import kotlinx.datetime.toLocalDateTime
import presentation.StringResources
import supabase.ChatConversation
import supabase.ChatMessage
import supabase.UserAlumni
import ui.components.MyTopAppBar
import ui.components.environment.MyLocalUserStudent
import ui.components.environment.currentAppLang
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

@Composable
fun AlumniChatsScreen(
   navController: NavController,
   dao: MyDao
) {
   val currentUser = MyLocalUserStudent.current
   val appLang = currentAppLang()

   // Get all conversations where the current user is a participant
   val allConversations by dao.getAllChatConversations().collectAsStateWithLifecycle(emptyList())
   val allParticipants by dao.getAllChatParticipants().collectAsStateWithLifecycle(emptyList())
   val allMessages by dao.getAllChatMessages().collectAsStateWithLifecycle(emptyList())
   val allAlumni by dao.getAllUserAlumni().collectAsStateWithLifecycle(emptyList())

   // Filter conversations where the current user is a participant
   val userConversations = remember(allConversations, allParticipants) {
      val userParticipations = allParticipants.filter { it.userId == currentUser.id }
      val conversationIds = userParticipations.map { it.conversationId }
      allConversations.filter { it.id in conversationIds }
   }

   // Get the alumni for each conversation
   val conversationsWithDetails = remember(userConversations, allParticipants, allAlumni, allMessages) {
      userConversations.mapNotNull { conversation ->
         // Find alumni in this conversation
         val participants = allParticipants.filter { it.conversationId == conversation.id }
         val alumniParticipant = participants.firstOrNull { it.userType == "alumni" }
         val alumni = alumniParticipant?.let { participant ->
            allAlumni.firstOrNull { it.id == participant.userId }
         }

         // Find last message in this conversation
         val lastMessage = allMessages
            .filter { it.conversationId == conversation.id }
            .maxByOrNull { it.createdAt }

         // Count unread messages
         val unreadCount = allMessages.count {
            it.conversationId == conversation.id &&
                    it.senderId != currentUser.id &&
                    !it.isRead
         }

         // Check if latest message is from the current user
         val isLatestFromCurrentUser = lastMessage?.senderId == currentUser.id

         if (alumni != null) {
            ConversationWithDetails(
               conversation = conversation,
               alumni = alumni,
               lastMessage = lastMessage,
               unreadCount = unreadCount,
               isLatestFromCurrentUser = isLatestFromCurrentUser
            )
         } else null
      }.sortedByDescending { it.lastMessage?.createdAt ?: it.conversation.updatedAt }
   }

   Scaffold(
      topBar = {
         MyTopAppBar(
            title = StringResources.messages(appLang),
            onBackClick = { navController.navigateUp() }
         )
      }
   ) { paddingValues ->
      if (conversationsWithDetails.isEmpty()) {
         // Empty state
         Box(
            modifier = Modifier
               .fillMaxSize()
               .padding(paddingValues),
            contentAlignment = Alignment.Center
         ) {
            Column(
               horizontalAlignment = Alignment.CenterHorizontally,
               verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
               Icon(
                  imageVector = Icons.Outlined.Chat,
                  contentDescription = null,
                  modifier = Modifier.size(64.dp),
                  tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
               )
               Text(
                  text = StringResources.noMessages(appLang),
                  style = MaterialTheme.typography.titleMedium
               )
            }
         }
      } else {
         // Conversation list
         LazyColumn(
            modifier = Modifier
               .fillMaxSize()
               .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
         ) {
            items(conversationsWithDetails) { conversationDetails ->
               ConversationItem(
                  conversationDetails = conversationDetails,
                  onClick = {
                     navController.navigate(AlumniChatRoute(conversationDetails.conversation.id))
                  }
               )
            }
         }
      }
   }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConversationItem(
   conversationDetails: ConversationWithDetails,
   onClick: () -> Unit
) {
   Card(
      onClick = onClick,
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(12.dp),
      elevation = CardDefaults.cardElevation(
         defaultElevation = if (conversationDetails.unreadCount > 0) 4.dp else 1.dp
      ),
      colors = CardDefaults.cardColors(
         containerColor = if (conversationDetails.unreadCount > 0)
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
         else
            MaterialTheme.colorScheme.surface
      )
   ) {
      Row(
         modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
         verticalAlignment = Alignment.CenterVertically
      ) {
         // Alumni avatar
         Surface(
            modifier = Modifier
               .size(60.dp)
               .clip(CircleShape),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            shadowElevation = 2.dp
         ) {
            if (conversationDetails.alumni.profilePictureUrl != null) {
               AsyncImage(
                  model = conversationDetails.alumni.profilePictureUrl,
                  contentDescription = "Profile picture",
                  modifier = Modifier.fillMaxSize(),
                  contentScale = ContentScale.Crop
               )
            } else {
               Box(
                  modifier = Modifier.fillMaxSize(),
                  contentAlignment = Alignment.Center,
                  content = {
                     Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(32.dp)
                     )
                  }
               )
            }
         }

         Spacer(modifier = Modifier.width(16.dp))

         Column(
            modifier = Modifier.weight(1f)
         ) {
            Row(
               modifier = Modifier.fillMaxWidth(),
               horizontalArrangement = Arrangement.SpaceBetween,
               verticalAlignment = Alignment.CenterVertically
            ) {
               // Alumni name
               Text(
                  text = "${conversationDetails.alumni.firstName} ${conversationDetails.alumni.lastName}",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = if (conversationDetails.unreadCount > 0) FontWeight.Bold else FontWeight.Normal
               )

               // Time
               conversationDetails.lastMessage?.let {
                  Text(
                     text = formatRelativeTime(it.createdAt),
                     style = MaterialTheme.typography.bodySmall,
                     color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
               }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
               modifier = Modifier.fillMaxWidth(),
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.SpaceBetween
            ) {
               // Last message with preview
               Text(
                  text = conversationDetails.lastMessage?.content ?: "No messages yet",
                  style = MaterialTheme.typography.bodyMedium,
                  color = if (conversationDetails.unreadCount > 0)
                     MaterialTheme.colorScheme.onSurface
                  else
                     MaterialTheme.colorScheme.onSurfaceVariant,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                  modifier = Modifier.weight(1f)
               )

               // Status indicators
               if (conversationDetails.isLatestFromCurrentUser && conversationDetails.lastMessage != null) {
                  Row(
                     verticalAlignment = Alignment.CenterVertically,
                     horizontalArrangement = Arrangement.spacedBy(4.dp)
                  ) {
                     // Read status
                     val isRead = conversationDetails.lastMessage.isRead
                     Box(modifier = Modifier.padding(start = 8.dp)) {
                        Icon(
                           imageVector = if (isRead) Icons.Filled.CheckCircle else Icons.Filled.Check,
                           contentDescription = if (isRead) "Read" else "Sent",
                           tint = if (isRead)
                              MaterialTheme.colorScheme.primary
                           else
                              MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                           modifier = Modifier.size(16.dp)
                        )
                     }
                  }
               } else if (conversationDetails.unreadCount > 0) {
                  // Unread indicator
                  Box(
                     modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                     contentAlignment = Alignment.Center
                  ) {
                     Text(
                        text = conversationDetails.unreadCount.toString(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelSmall
                     )
                  }
               }
            }
         }
      }
   }
}

private data class ConversationWithDetails(
   val conversation: ChatConversation,
   val alumni: UserAlumni,
   val lastMessage: ChatMessage?,
   val unreadCount: Int,
   val isLatestFromCurrentUser: Boolean
)

private fun formatRelativeTime(instant: Instant): String {
   val now = Clock.System.now()
   val diff = now - instant

   return when {
      diff < 60.seconds -> "Just now"
      diff < 1.hours -> "${diff.inWholeMinutes}m ago"
      diff < 24.hours -> "${diff.inWholeHours}h ago"
      diff < 48.hours -> "Yesterday"
      diff < 7.days -> {
         val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
         when (localDateTime.dayOfWeek.ordinal) {
            0 -> "Monday"
            1 -> "Tuesday"
            2 -> "Wednesday"
            3 -> "Thursday"
            4 -> "Friday"
            5 -> "Saturday"
            6 -> "Sunday"
            else -> "${diff.inWholeDays}d ago"
         }
      }
      else -> {
         val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
         "${localDateTime.monthNumber}/${localDateTime.dayOfMonth}"
      }
   }
}