package supabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = UserAlumni.TABLE_NAME)
@Serializable
data class UserAlumni(
    @PrimaryKey @ColumnInfo(name = "id") @SerialName("id") val id: String,
    @ColumnInfo(name = "first_name") @SerialName("first_name") val firstName: String,
    @ColumnInfo(name = "last_name") @SerialName("last_name") val lastName: String,
    @ColumnInfo(name = "email") @SerialName("email") val email: String,
    @ColumnInfo(name = "is_approved") @SerialName("is_approved") val isApproved: Boolean = false,
    @ColumnInfo(name = "birthdate") @SerialName("birthdate") val birthdate: LocalDate? = null,
    @ColumnInfo(name = "graduation_year") @SerialName("graduation_year") val graduationYear: Int? = null,
    @ColumnInfo(name = "university") @SerialName("university") val university: String? = null,
    @ColumnInfo(name = "experience") @SerialName("experience") val experience: String? = null,
    @ColumnInfo(name = "profile_picture_url") @SerialName("profile_picture_url") val profilePictureUrl: String? = null,
    @ColumnInfo(name = "created_at") @SerialName("created_at") val createdAt: Instant = Clock.System.now(),
    @ColumnInfo(name = "updated_at") @SerialName("updated_at") val updatedAt: Instant = Clock.System.now()
) : SupabaseTable {
    companion object {
        const val TABLE_NAME = "user_alumni"
    }

    override val tableName: String
        get() = TABLE_NAME
}