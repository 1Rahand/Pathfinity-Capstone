package supabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(
   tableName = "course_video_progress",
   primaryKeys = ["video_id", "student_id"],
   foreignKeys = [
      ForeignKey(
         entity = CourseVideo::class,
         parentColumns = ["id"],
         childColumns = ["video_id"],
         onDelete = ForeignKey.CASCADE
      ),
      ForeignKey(
         entity = UserStudent::class,
         parentColumns = ["id"],
         childColumns = ["student_id"],
         onDelete = ForeignKey.CASCADE
      )
   ],
   indices = [Index(value = ["video_id", "student_id"])]
)
data class CourseVideoProgress(
   @ColumnInfo(name = "video_id") @SerialName("video_id") val videoId: String,
   @ColumnInfo(name = "student_id") @SerialName("student_id") val studentId: String,
   @ColumnInfo(name = "completed") @SerialName("completed") val completed: Boolean = false,
   @ColumnInfo(name = "created_at") @SerialName("created_at") val createdAt: Instant = Clock.System.now(),
   @ColumnInfo(name = "updated_at") @SerialName("updated_at") val updatedAt: Instant = Clock.System.now()
) : SupabaseTable {
   companion object {
      const val TABLE_NAME = "course_video_progress"
   }

   override val tableName: String
      get() = TABLE_NAME
}