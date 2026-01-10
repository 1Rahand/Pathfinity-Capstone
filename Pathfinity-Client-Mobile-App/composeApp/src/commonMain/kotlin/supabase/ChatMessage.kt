package supabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Entity(tableName = ChatMessage.TABLE_NAME)
@Serializable
data class ChatMessage(
    @PrimaryKey @ColumnInfo(name = "id") @SerialName("id") val id: String,
    @ColumnInfo(name = "conversation_id") @SerialName("conversation_id") val conversationId: String,
    @ColumnInfo(name = "sender_id") @SerialName("sender_id") val senderId: String,
    @ColumnInfo(name = "content") @SerialName("content") val content: String,
    @ColumnInfo(name = "is_read") @SerialName("is_read") val isRead: Boolean = false,
    @ColumnInfo(name = "created_at") @SerialName("created_at") val createdAt: Instant = Clock.System.now(),
    @ColumnInfo(name = "updated_at") @SerialName("updated_at") val updatedAt: Instant = Clock.System.now(),
    @Transient @ColumnInfo(name = "is_sync_with_server") val isSyncWithServer: Boolean = false,
) : SupabaseTable {
    companion object {
        const val TABLE_NAME = "chat_messages"
    }

    override val tableName: String
        get() = TABLE_NAME
}