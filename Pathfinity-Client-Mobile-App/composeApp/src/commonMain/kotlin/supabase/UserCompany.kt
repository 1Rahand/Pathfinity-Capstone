package supabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = UserCompany.TABLE_NAME)
@Serializable
data class UserCompany(
   @PrimaryKey @ColumnInfo(name = "id") @SerialName("id") val id: String,
   @ColumnInfo(name = "company_name") @SerialName("company_name") val companyName: String,
   @ColumnInfo(name = "email") @SerialName("email") val email: String,
   @ColumnInfo(name = "is_approved") @SerialName("is_approved") val isApproved: Boolean = false,
   @ColumnInfo(name = "profile_picture_url") @SerialName("profile_picture_url") val profilePictureUrl: String? = null,
   @ColumnInfo(name = "created_at") @SerialName("created_at") val createdAt: Instant = Clock.System.now(),
   @ColumnInfo(name = "updated_at") @SerialName("updated_at") val updatedAt: Instant = Clock.System.now()
) : SupabaseTable {
   companion object {
      const val TABLE_NAME = "user_companies"
   }

   override val tableName: String
      get() = TABLE_NAME
}