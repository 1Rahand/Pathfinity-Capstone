package supabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = Course.TABLE_NAME)
@Serializable
data class Course(
    @PrimaryKey @ColumnInfo(name = "id") @SerialName("id") val id: String,
    @ColumnInfo(name = "title") @SerialName("title") val title: String = "",
    @ColumnInfo(name = "creator_id") @SerialName("creator_id") val creatorId: String,
    @ColumnInfo(name = "description") @SerialName("description") val description: String? = null,
    @ColumnInfo(name = "thumbnail_url") @SerialName("thumbnail_url") val thumbnailUrl: String? = null,
    @ColumnInfo(name = "category_id") @SerialName("category_id") val categoryId: String? = null,
    @ColumnInfo(name = "membership_type") @SerialName("membership_type") val membershipTypeString: String = "PRO",
    @ColumnInfo(name = "is_approved") @SerialName("is_approved") val isApproved: Boolean? = null,
    @ColumnInfo(name = "is_active") @SerialName("is_active") val isActive: Boolean = false,
    @ColumnInfo(name = "rejection_reason") @SerialName("rejection_reason") val rejectionReason: String? = null,
    @ColumnInfo(name = "difficulty") @SerialName("difficulty") val difficulty: String? = null,
    @ColumnInfo(name = "created_at") @SerialName("created_at") val createdAt: Instant = Clock.System.now(),
    @ColumnInfo(name = "updated_at") @SerialName("updated_at") val updatedAt: Instant = Clock.System.now()
) : SupabaseTable {
    companion object {
        const val TABLE_NAME = "courses"
    }

    override val tableName: String
        get() = TABLE_NAME

    enum class MembershipType {
        FREE,PRO
    }

    val membershipType: MembershipType
        get() = when (membershipTypeString) {
            "FREE" -> MembershipType.FREE
            "PRO" -> MembershipType.PRO
            else -> MembershipType.FREE // Default to PRO if unknown
        }


}