package supabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = VideoChange.TABLE_NAME)
@Serializable
data class VideoChange(
    @PrimaryKey @ColumnInfo(name = "id") @SerialName("id") val id: String,
    @ColumnInfo(name = "course_video_id") @SerialName("course_video_id") val courseVideoId: String? = null,
    @ColumnInfo(name = "title") @SerialName("title") val title: String? = null,
    @ColumnInfo(name = "description") @SerialName("description") val description: String? = null,
    @ColumnInfo(name = "video_url") @SerialName("video_url") val videoUrl: String? = null,
    @ColumnInfo(name = "is_reviewed") @SerialName("is_reviewed") val isReviewed: Boolean = false,
    @ColumnInfo(name = "is_approved") @SerialName("is_approved") val isApproved: Boolean? = null,
    @ColumnInfo(name = "rejection_reason") @SerialName("rejection_reason") val rejectionReason: String? = null,
    @ColumnInfo(name = "created_at") @SerialName("created_at") val createdAt: Instant = Clock.System.now(),
    @ColumnInfo(name = "updated_at") @SerialName("updated_at") val updatedAt: Instant = Clock.System.now()
) : SupabaseTable {
    companion object {
        const val TABLE_NAME = "video_changes"
    }

    override val tableName: String
        get() = TABLE_NAME
}