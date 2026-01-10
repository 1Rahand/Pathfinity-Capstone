package supabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = CourseReview.TABLE_NAME)
@Serializable
data class CourseReview(
   @PrimaryKey @ColumnInfo(name = "id") @SerialName("id") val id: String,
   @ColumnInfo(name = "course_id") @SerialName("course_id") val courseId: String,
   @ColumnInfo(name = "student_id") @SerialName("student_id") val studentId: String,
   @ColumnInfo(name = "rating") @SerialName("rating") val rating: Int,
   @ColumnInfo(name = "review_text") @SerialName("review_text") val reviewText: String? = null,
   @ColumnInfo(name = "created_at") @SerialName("created_at") val createdAt: Instant = Clock.System.now(),
   @ColumnInfo(name = "updated_at") @SerialName("updated_at") val updatedAt: Instant = Clock.System.now()
) : SupabaseTable {
   companion object {
      const val TABLE_NAME = "course_reviews"
      val empty = CourseReview(
         id = "",
         courseId = "",
         studentId = "",
         rating = 0,
         reviewText = null,
         createdAt = Clock.System.now(),
         updatedAt = Clock.System.now()
      )
   }

   override val tableName: String
      get() = TABLE_NAME
}