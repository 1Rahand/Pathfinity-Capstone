package supabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = ChatParticipant.TABLE_NAME)
@Serializable
data class ChatParticipant(
    @PrimaryKey @ColumnInfo(name = "id") @SerialName("id") val id: String,
    @ColumnInfo(name = "conversation_id") @SerialName("conversation_id") val conversationId: String,
    @ColumnInfo(name = "user_id") @SerialName("user_id") val userId: String,
    @ColumnInfo(name = "user_type") @SerialName("user_type") val userType: String,
    @ColumnInfo(name = "created_at") @SerialName("created_at") val createdAt: Instant = Clock.System.now(),
    @ColumnInfo(name = "updated_at") @SerialName("updated_at") val updatedAt: Instant = Clock.System.now()
) : SupabaseTable {
    companion object {
        const val TABLE_NAME = "chat_participants"
    }

    override val tableName: String
        get() = TABLE_NAME
}