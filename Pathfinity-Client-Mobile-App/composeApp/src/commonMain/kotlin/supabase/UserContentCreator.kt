package supabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = UserContentCreator.TABLE_NAME)
@Serializable
data class UserContentCreator(
    @PrimaryKey @ColumnInfo(name = "id") @SerialName("id") val id: String,
    @ColumnInfo(name = "first_name") @SerialName("first_name") val firstName: String,
    @ColumnInfo(name = "last_name") @SerialName("last_name") val lastName: String,
    @ColumnInfo(name = "email") @SerialName("email") val email: String,
    @ColumnInfo(name = "birthdate") @SerialName("birthdate") val birthdate: LocalDate? = null,
    @ColumnInfo(name = "is_approved") @SerialName("is_approved") val isApproved: Boolean? = null,
    @ColumnInfo(name = "bio") @SerialName("bio") val bio: String? = null,
    @ColumnInfo(name = "phone") @SerialName("phone") val phone: String? = null,
    @ColumnInfo(name = "profile_picture_url") @SerialName("profile_picture_url") val profilePictureUrl: String? = null,
    @ColumnInfo(name = "created_at") @SerialName("created_at") val createdAt: Instant = Clock.System.now(),
    @ColumnInfo(name = "updated_at") @SerialName("updated_at") val updatedAt: Instant = Clock.System.now()
) : SupabaseTable {
    companion object {
        const val TABLE_NAME = "user_content_creators"
        val mock = UserContentCreator(
            id = "1",
            firstName = "John",
            lastName = "Doe",
            birthdate = LocalDate(1990, 1, 1),
            email = "enos.salar.10@gmail.com",
            isApproved = true,
            bio = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            phone = "+1234567890",
            createdAt = Instant.fromEpochMilliseconds(0),
            updatedAt = Instant.fromEpochMilliseconds(0)
        )
    }

    override val tableName: String
        get() = TABLE_NAME
}