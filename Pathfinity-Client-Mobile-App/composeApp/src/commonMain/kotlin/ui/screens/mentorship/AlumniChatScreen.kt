package ui.screens.mentorship

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import database.MyDao
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import supabase.ChatMessage
import supabase.UserAlumni
import ui.components.MyTopAppBar
import ui.components.environment.MyLocalUserStudent
import ui.components.modifier.noIndicationClickable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun AlumniChatScreen(
   navController: NavController,
   dao: MyDao,
   conversationId: String
) {
   // Get conversation data
   val conversation by dao.getChatConversationByIdFlow(conversationId).collectAsStateWithLifecycle(null)
   val participants by dao.getChatParticipantsByConversationIdFlow(conversationId).collectAsStateWithLifecycle(emptyList())
   val messages by dao.getChatMessagesByConversationIdFlow(conversationId).collectAsStateWithLifecycle(emptyList())
   val focusManager = LocalFocusManager.current

   LaunchedEffect(messages) {
      messages.forEach {
         println(it)
      }
   }
   // Current user
   val currentUser = MyLocalUserStudent.current

   // Find the alumni participant
   val alumniParticipant = participants.firstOrNull { it.userType == "alumni" }
   val alumniId = alumniParticipant?.userId
   val alumni by dao.getUserAlumniByIdFlow(alumniId ?: "").collectAsStateWithLifecycle(null)

   // Message input state
   var messageText by remember { mutableStateOf("") }
   val scope = rememberCoroutineScope()
   val snackbarHostState = remember { SnackbarHostState() }

   // Send message function
   val sendMessage = {
      if (messageText.isNotEmpty()) {
         scope.launch {
            try {
               val newMessage = ChatMessage(
                  id = Uuid.random().toHexDashString(),
                  conversationId = conversationId,
                  senderId = currentUser.id,
                  content = messageText,
                  isRead = false,
                  createdAt = Clock.System.now(),
                  updatedAt = Clock.System.now(),
                  isSyncWithServer = false // Will be synced by MessageSyncer
               )

               // Insert into local database
               dao.upsertChatMessage(newMessage)

               // Clear the input field
               messageText = ""
            } catch (e: Exception) {
               snackbarHostState.showSnackbar("Failed to send message")
            }
         }
      }
   }


   LaunchedEffect(messages) {
      messages.forEach { message ->
         if (message.senderId != currentUser.id && !message.isRead) {
            dao.upsertChatMessage(message.copy(isRead = true, isSyncWithServer = false, updatedAt = Clock.System.now()))
         }
      }
   }


   Scaffold(
      topBar = {
         ChatTopBar(alumni = alumni, onBackClick = { navController.navigateUp() })
      },
      snackbarHost = { SnackbarHost(snackbarHostState) }
   ) { paddingValues ->
      Box(
         modifier = Modifier
            .noIndicationClickable { focusManager.clearFocus() }
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
      ) {
         if (alumni == null || conversation == null) {
            CircularProgressIndicator(
               modifier = Modifier.align(Alignment.Center)
            )
         } else {
            Column(
               modifier = Modifier.fillMaxSize()
            ) {
               // Messages list
               LazyColumn(
                  modifier = Modifier
                     .weight(1f)
                     .fillMaxWidth(),
                  reverseLayout = true,
                  contentPadding = PaddingValues(16.dp),
                  verticalArrangement = Arrangement.spacedBy(8.dp)
               ) {
                  items(messages.sortedByDescending { it.createdAt }) { message ->
                     MessageItem(
                        message = message,
                        isFromCurrentUser = message.senderId == currentUser.id,
                        alumni = alumni
                     )
                  }
               }

               // Message input
               MessageInput(
                  modifier = Modifier,
                  value = messageText,
                  onValueChange = { messageText = it },
                  onSend = sendMessage
               )
            }
         }
      }
   }
}

@Composable
private fun ChatTopBar(
   alumni: UserAlumni?,
   onBackClick: () -> Unit
) {
   MyTopAppBar(
      title = alumni?.let { "${it.firstName} ${it.lastName}" } ?: "Chat",
      onBackClick = onBackClick
   )
}

@Composable
private fun MessageItem(
   message: ChatMessage,
   isFromCurrentUser: Boolean,
   alumni: UserAlumni?
) {
   Column(
      modifier = Modifier
         .fillMaxWidth()
         .padding(vertical = 2.dp),
      horizontalAlignment = if (isFromCurrentUser) Alignment.End else Alignment.Start
   ) {
      Row(
         modifier = Modifier.padding(bottom = 4.dp),
         verticalAlignment = Alignment.CenterVertically,
         horizontalArrangement = if (isFromCurrentUser) Arrangement.End else Arrangement.Start
      ) {
         if (!isFromCurrentUser) {
            // Alumni avatar
            Box(
               modifier = Modifier
                  .size(24.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
               alumni?.profilePictureUrl?.let {
                  AsyncImage(
                     model = it,
                     contentDescription = "Profile picture",
                     modifier = Modifier.fillMaxSize(),
                     contentScale = ContentScale.Crop
                  )
               }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
               text = alumni?.firstName ?: "Alumni",
               style = MaterialTheme.typography.labelMedium,
               color = MaterialTheme.colorScheme.onSurfaceVariant
            )
         } else {
            Text(
               text = "You",
               style = MaterialTheme.typography.labelMedium,
               color = MaterialTheme.colorScheme.onSurfaceVariant
            )
         }
      }

      Surface(
         shape = RoundedCornerShape(
            topStart = if (isFromCurrentUser) 16.dp else 4.dp,
            topEnd = if (isFromCurrentUser) 4.dp else 16.dp,
            bottomStart = 16.dp,
            bottomEnd = 16.dp
         ),
         color = if (isFromCurrentUser)
            MaterialTheme.colorScheme.primary
         else
            MaterialTheme.colorScheme.surfaceVariant,
         shadowElevation = 1.dp,
         modifier = Modifier.widthIn(max = 280.dp)
      ) {
         Column(modifier = Modifier.padding(12.dp)) {
            Text(
               text = message.content,
               style = MaterialTheme.typography.bodyMedium,
               color = if (isFromCurrentUser)
                  MaterialTheme.colorScheme.onPrimary
               else
                  MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Time and status in a row
            Row(
               modifier = Modifier.fillMaxWidth(),
               horizontalArrangement = Arrangement.End,
               verticalAlignment = Alignment.CenterVertically
            ) {
               // Status indicators
               if (isFromCurrentUser) {
                  Text(
                     text = when {
                        !message.isSyncWithServer -> "Not Synced"
                        message.isRead -> "Read"
                        else -> "Sent"
                     },
                     style = MaterialTheme.typography.labelSmall,
                     color = if (isFromCurrentUser)
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                     else
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                  )

                  Spacer(modifier = Modifier.width(8.dp))
               }

               // Time with AM/PM
               Text(
                  text = formatTime(message.createdAt),
                  style = MaterialTheme.typography.labelSmall,
                  color = if (isFromCurrentUser)
                     MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                  else
                     MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
               )
            }
         }
      }
   }
}

@Composable
private fun MessageInput(
   modifier: Modifier = Modifier,
   value: String,
   onValueChange: (String) -> Unit,
   onSend: () -> Unit
) {
   Surface(
      modifier = modifier.fillMaxWidth(),
      color = MaterialTheme.colorScheme.surfaceVariant
   ) {
      Row(
         modifier = Modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .padding(12.dp),
         verticalAlignment = Alignment.CenterVertically,
         horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
         OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("Type a message") },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(24.dp),
            maxLines = 4
         )

         IconButton(
            onClick = onSend,
            enabled = value.isNotEmpty(),
            modifier = Modifier
               .size(48.dp)
               .clip(CircleShape)
               .background(
                  if (value.isNotEmpty())
                     MaterialTheme.colorScheme.primary
                  else
                     MaterialTheme.colorScheme.surfaceVariant
               )
         ) {
            Icon(
               imageVector = Icons.Filled.Send,
               contentDescription = "Send",
               tint = if (value.isNotEmpty())
                  MaterialTheme.colorScheme.onPrimary
               else
                  MaterialTheme.colorScheme.onSurfaceVariant
            )
         }
      }
   }
}

private fun formatTime(instant: Instant): String {
   val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
   val hour = localDateTime.hour
   val minute = localDateTime.minute

   val amPm = if (hour < 12) "AM" else "PM"
   val hour12 = when {
      hour == 0 -> 12   // 0:00 is 12 AM
      hour > 12 -> hour - 12  // Convert to 12-hour format
      else -> hour  // Already 1-12
   }

   return "$hour12:${minute.toString().padStart(2, '0')} $amPm"
}