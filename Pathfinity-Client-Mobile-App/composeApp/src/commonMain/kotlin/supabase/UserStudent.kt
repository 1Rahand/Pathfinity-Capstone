package supabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import domain.Gender
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = UserStudent.TABLE_NAME)
@Serializable
data class UserStudent(
    @PrimaryKey @ColumnInfo(name = "id") @SerialName("id") val id: String,
    @ColumnInfo(name = "first_name") @SerialName("first_name") val firstName: String? = null,
    @ColumnInfo(name = "last_name") @SerialName("last_name") val lastName: String? = null,
    @ColumnInfo(name = "email") @SerialName("email") val email: String? = null,
    @ColumnInfo(name = "birthdate") @SerialName("birthdate") val birthdate: LocalDate? = null,
    @ColumnInfo(name = "gender") @SerialName("gender") val genderString: String? = null,
    @ColumnInfo(name = "premium") @SerialName("premium") val premium: Boolean = false,
    @ColumnInfo(name = "premium_expires_at") @SerialName("premium_expires_at") val premiumExpiresAt: Instant? = null,
    @ColumnInfo(name = "active") @SerialName("active") val active: Boolean = true,
    @ColumnInfo(name = "skills") @SerialName("skills") val skills: List<String>? = null,
    @ColumnInfo(name = "created_at") @SerialName("created_at") val createdAt: Instant = Clock.System.now(),
    @ColumnInfo(name = "updated_at") @SerialName("updated_at") val updatedAt: Instant = Clock.System.now()
) : SupabaseTable {

    override val tableName: String
        get() = TABLE_NAME

    val fullName: String
        get() {
            if (firstName == null && lastName == null) {
                return "Guest"
            }
            return "${firstName.orEmpty()} ${lastName.orEmpty()}".trim()
        }

    companion object {
        const val TABLE_NAME = "user_students"
        val empty = UserStudent(
            id = "",
            firstName = null,
            lastName = null,
            birthdate = null,
            premium = false,
            premiumExpiresAt = null,
            createdAt = Clock.System.now(),
            active = true,
            genderString = null,
            updatedAt = Clock.System.now()
        )
    }


    val gender: Gender get() = Gender.fromString(genderString ?: "")
}