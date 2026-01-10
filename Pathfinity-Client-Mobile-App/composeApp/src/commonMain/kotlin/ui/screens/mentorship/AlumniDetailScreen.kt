package ui.screens.mentorship

import AlumniChatRoute
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import database.MyDao
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import supabase.ChatConversation
import supabase.ChatParticipant
import ui.components.MyTopAppBar
import ui.components.environment.MyLocalUserStudent
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun AlumniDetailScreen(
   navController: NavController,
   dao: MyDao,
   alumniId: String,
   supabase: SupabaseClient
) {
   val alumnus by dao.getUserAlumniByIdFlow(alumniId).collectAsStateWithLifecycle(null)
   val chatConversations by dao.getAllChatConversations().collectAsStateWithLifecycle(emptyList())
   val chatParticipants by dao.getAllChatParticipants().collectAsStateWithLifecycle(emptyList())
   val chatMessages by dao.getAllChatMessages().collectAsStateWithLifecycle(emptyList())
   val currentUser = MyLocalUserStudent.current
   val scope = rememberCoroutineScope()
   val snackbarHostState = remember { SnackbarHostState() }
   var isLoading by remember { mutableStateOf(false) }

   val onStartChat: () -> Unit = {
      // Check if I already have a conversation with this alumnus
      val existingConversation = chatConversations.find { conversation ->
         // Get all participants for this specific conversation
         val conversationParticipants = chatParticipants.filter { it.conversationId == conversation.id }
         // Check if BOTH the current user AND the alumnus are participants
         val hasCurrentUser = conversationParticipants.any { it.userId == currentUser.id }
         val hasAlumnus = conversationParticipants.any { it.userId == alumnus?.id }
         hasCurrentUser && hasAlumnus
      }

      if (existingConversation != null) {
         // If conversation exists, navigate to it
         navController.navigate(AlumniChatRoute(existingConversation.id))
      } else if (alumnus != null) {
         // Create a new conversation only if one doesn't exist
         scope.launch {
            isLoading = true
            try {
               // Existing code for creating a new conversation...
               // Create a new conversation
               val conversationId = Uuid.random().toHexDashString()
               val conversation = ChatConversation(
                  id = conversationId,
                  createdAt = Clock.System.now(),
                  updatedAt = Clock.System.now()
               )

               // Create participants for the conversation
               val currentUserParticipant = ChatParticipant(
                  id = Uuid.random().toHexDashString(),
                  conversationId = conversationId,
                  userId = currentUser.id,
                  userType = "student",
                  createdAt = Clock.System.now(),
                  updatedAt = Clock.System.now()
               )

               val alumniParticipant = ChatParticipant(
                  id = Uuid.random().toHexDashString(),
                  conversationId = conversationId,
                  userId = alumnus!!.id,
                  userType = "alumni",
                  createdAt = Clock.System.now(),
                  updatedAt = Clock.System.now()
               )

               // Save to Supabase
               supabase.from(ChatConversation.TABLE_NAME).insert(conversation)
               supabase.from(ChatParticipant.TABLE_NAME).insert(currentUserParticipant)
               supabase.from(ChatParticipant.TABLE_NAME).insert(alumniParticipant)

               // Save to local database
               dao.upsertChatConversation(conversation)
               dao.upsertChatParticipant(currentUserParticipant)
               dao.upsertChatParticipant(alumniParticipant)

               // Navigate to chat screen
               navController.navigate(AlumniChatRoute(conversationId))
            } catch (e: Exception) {
               // Handle error
            }
         }
      }
   }

   Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = {
         MyTopAppBar(
            title = "Alumni Profile",
            onBackClick = { navController.navigateUp() }
         )
      },
      floatingActionButton = {
         ExtendedFloatingActionButton(
            onClick = onStartChat,
            icon = { Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null) },
            text = { Text("Start Chat") },
            expanded = true
         )
      },
      snackbarHost = { SnackbarHost(snackbarHostState) }
   ) { paddingValues ->
      if (alumnus == null) {
         // Loading state
         Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
         ) {
            CircularProgressIndicator()
         }
      } else {
         Column(
            modifier = Modifier
               .fillMaxSize()
               .padding(paddingValues)
               .verticalScroll(rememberScrollState())
         ) {
            // Profile header
            Box(
               modifier = Modifier
                  .fillMaxWidth()
                  .height(200.dp)
                  .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
               // Profile picture
               Box(
                  modifier = Modifier
                     .align(Alignment.Center)
                     .size(140.dp)
                     .clip(CircleShape)
                     .background(MaterialTheme.colorScheme.surface)
               ) {
                  if (alumnus?.profilePictureUrl != null) {
                     AsyncImage(
                        model = alumnus?.profilePictureUrl,
                        contentDescription = "Profile picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                     )
                  } else {
                     Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                     ) {
                        Icon(
                           imageVector = Icons.Outlined.Person,
                           contentDescription = null,
                           tint = MaterialTheme.colorScheme.onSurface,
                           modifier = Modifier.size(80.dp)
                        )
                     }
                  }
               }
            }

            // Alumni details
            Column(
               modifier = Modifier
                  .fillMaxWidth()
                  .padding(16.dp),
               verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
               // Name
               Text(
                  text = "${alumnus?.firstName} ${alumnus?.lastName}",
                  style = MaterialTheme.typography.headlineMedium,
                  fontWeight = FontWeight.Bold
               )

               // Info cards
               Card(
                  modifier = Modifier.fillMaxWidth(),
                  shape = RoundedCornerShape(12.dp)
               ) {
                  Column(
                     modifier = Modifier.padding(16.dp),
                     verticalArrangement = Arrangement.spacedBy(12.dp)
                  ) {
                     // Email
                     Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                     ) {
                        Icon(
                           imageVector = Icons.Default.Email,
                           contentDescription = null,
                           tint = MaterialTheme.colorScheme.primary
                        )
                        Text(text = alumnus?.email ?: "")
                     }

                     // University
                     alumnus?.university?.let {
                        Row(
                           verticalAlignment = Alignment.CenterVertically,
                           horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                           Icon(
                              imageVector = Icons.Default.School,
                              contentDescription = null,
                              tint = MaterialTheme.colorScheme.primary
                           )
                           Text(text = it)
                        }
                     }

                     // Graduation Year
                     alumnus?.graduationYear?.let {
                        Row(
                           verticalAlignment = Alignment.CenterVertically,
                           horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                           Icon(
                              imageVector = Icons.Default.School,
                              contentDescription = null,
                              tint = MaterialTheme.colorScheme.primary
                           )
                           Text(text = "Graduated in $it")
                        }
                     }
                  }
               }

               // Experience
               alumnus?.experience?.let {
                  Card(
                     modifier = Modifier.fillMaxWidth(),
                     shape = RoundedCornerShape(12.dp)
                  ) {
                     Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                     ) {
                        Row(
                           verticalAlignment = Alignment.CenterVertically,
                           horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                           Icon(
                              imageVector = Icons.Outlined.Work,
                              contentDescription = null,
                              tint = MaterialTheme.colorScheme.primary
                           )
                           Text(
                              text = "Experience",
                              style = MaterialTheme.typography.titleMedium,
                              fontWeight = FontWeight.Bold
                           )
                        }
                        Text(text = it)
                     }
                  }
               }
            }
         }

         if (isLoading) {
            Box(
               modifier = Modifier
                  .fillMaxSize()
                  .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)),
               contentAlignment = Alignment.Center
            ) {
               CircularProgressIndicator()
            }
         }
      }
   }
}