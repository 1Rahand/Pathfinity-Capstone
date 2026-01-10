package data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import database.MyDao
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import supabase.ChatMessage

@OptIn(SupabaseExperimental::class)
class MessageSyncer(
   private val supabase: SupabaseClient,
   private val dao: MyDao,
   private val repoAuth: RepoAuth
) : ViewModel() {
   init {
      // Listen for remote messages from Supabase
      viewModelScope.launch {
         supabase.from(ChatMessage.TABLE_NAME)
            .selectAsFlow(ChatMessage::id)
            .distinctUntilChanged() // Only process distinct changes
            .collect { messages ->
               val userId = repoAuth.getUserId()
               val newMessages = messages.map { message ->
                  // Only mark our own messages as synced
                  message.copy(isSyncWithServer = true)
               }

               // Save remote messages to local database
               if (newMessages.isNotEmpty()) {
                  dao.upsertChatMessages(newMessages)
               }
            }
      }

      // Send local unsynchronized messages to Supabase
      viewModelScope.launch {
         dao.getUnsyncedChatMessages()
            .distinctUntilChanged() // Only process when there are actual changes
            .collect { unsyncedMessages ->
               unsyncedMessages.forEach { message ->
                  try {
                     // Upload to Supabase
                     supabase.from(ChatMessage.TABLE_NAME).upsert(message)

                     // Mark as synchronized locally without triggering the flow again
                     dao.upsertChatMessage(message.copy(isSyncWithServer = true))
                  } catch (e: Exception) {
                     // Log error but continue with other messages
                     println("Failed to sync message ${message.id}: ${e.message}")
                  }
               }
            }
      }
   }
}