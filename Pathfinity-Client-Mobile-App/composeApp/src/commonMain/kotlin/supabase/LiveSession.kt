package supabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = LiveSession.TABLE_NAME)
@Serializable
data class LiveSession(
    @PrimaryKey @ColumnInfo(name = "id") @SerialName("id") val id: String,
    @ColumnInfo(name = "creator_id") @SerialName("creator_id") val creatorId: String,
    @ColumnInfo(name = "course_id") @SerialName("course_id") val courseId: String,
    @ColumnInfo(name = "title") @SerialName("title") val title: String,
    @ColumnInfo(name = "status") @SerialName("status") val status: String, // "scheduled", "active", "ended", or "cancelled"
    @ColumnInfo(name = "channel_name") @SerialName("channel_name") val channelName: String,
    @ColumnInfo(name = "scheduled_at") @SerialName("scheduled_at") val scheduledAt: Instant? = null,
    @ColumnInfo(name = "started_at") @SerialName("started_at") val startedAt: Instant? = Clock.System.now(),
    @ColumnInfo(name = "ended_at") @SerialName("ended_at") val endedAt: Instant? = null,
    @ColumnInfo(name = "viewer_count") @SerialName("viewer_count") val viewerCount: Int = 0,
    @ColumnInfo(name = "created_at") @SerialName("created_at") val createdAt: Instant = Clock.System.now(),
    @ColumnInfo(name = "updated_at") @SerialName("updated_at") val updatedAt: Instant = Clock.System.now()
) : SupabaseTable {
    companion object {
        const val TABLE_NAME = "live_sessions"
    }

    override val tableName: String
        get() = TABLE_NAME
}

