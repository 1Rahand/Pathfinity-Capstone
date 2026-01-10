package supabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = UserAdmin.TABLE_NAME)
@Serializable
data class UserAdmin(
    @PrimaryKey @ColumnInfo(name = "id") @SerialName("id") val id: String,
    @ColumnInfo(name = "email") @SerialName("email") val email: String,
    @ColumnInfo(name = "username") @SerialName("username") val username: String,
    @ColumnInfo(name = "first_name") @SerialName("first_name") val firstName: String,
    @ColumnInfo(name = "last_name") @SerialName("last_name") val lastName: String? = null,
    @ColumnInfo(name = "is_super_admin") @SerialName("is_super_admin") val isSuperAdmin: Boolean = false,
    @ColumnInfo(name = "profile_picture_url") @SerialName("profile_picture_url") val profilePictureUrl: String? = null,
    @ColumnInfo(name = "created_at") @SerialName("created_at") val createdAt: Instant = Clock.System.now(),
    @ColumnInfo(name = "updated_at") @SerialName("updated_at") val updatedAt: Instant = Clock.System.now()
) : SupabaseTable {
    companion object {
        const val TABLE_NAME = "user_admins"
    }

    override val tableName: String
        get() = TABLE_NAME
}