package supabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = CourseVideo.TABLE_NAME)
@Serializable
data class CourseVideo(
    @PrimaryKey @ColumnInfo(name = "id") @SerialName("id") val id: String,
    @ColumnInfo(name = "course_id") @SerialName("course_id") val courseId: String,
    @ColumnInfo(name = "title") @SerialName("title") val title: String,
    @ColumnInfo(name = "description") @SerialName("description") val description: String,
    @ColumnInfo(name = "video_url") @SerialName("video_url") val videoUrl: String,
    @ColumnInfo(name = "thumbnail_url") @SerialName("thumbnail_url") val thumbnailUrl: String? = null,
    @ColumnInfo(name = "sequence_number") @SerialName("sequence_number") val sequenceNumber: Int,
    @ColumnInfo(name = "is_free_preview") @SerialName("is_free_preview") val isFreePreview: Boolean = false,
    @ColumnInfo(name = "is_reviewed") @SerialName("is_reviewed") val isReviewed: Boolean? = null,
    @ColumnInfo(name = "is_approved") @SerialName("is_approved") val isApproved: Boolean? = null,
    @ColumnInfo(name = "rejection_reason") @SerialName("rejection_reason") val rejectionReason: String? = null,
    @ColumnInfo(name = "created_at") @SerialName("created_at") val createdAt: Instant = Clock.System.now(),
    @ColumnInfo(name = "updated_at") @SerialName("updated_at") val updatedAt: Instant = Clock.System.now()
) : SupabaseTable {
    companion object {
        const val TABLE_NAME = "course_videos"
    }

    override val tableName: String
        get() = TABLE_NAME
}