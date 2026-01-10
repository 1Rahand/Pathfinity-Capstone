package supabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ui.screens.internship.InternshipCard

@Entity(tableName = Internship.TABLE_NAME)
@Serializable
data class Internship(
    @PrimaryKey @ColumnInfo(name = "id") @SerialName("id") val id: String,
    @ColumnInfo(name = "company_id") @SerialName("company_id") val companyId: String,
    @ColumnInfo(name = "title") @SerialName("title") val title: String,
    @ColumnInfo(name = "description") @SerialName("description") val description: String,
    @ColumnInfo(name = "duration") @SerialName("duration") val duration: String,
    @ColumnInfo(name = "skills") @SerialName("skills") val skills: List<String>,
    @ColumnInfo(name = "is_approved") @SerialName("is_approved") val isApproved: Boolean? = null,
    @ColumnInfo(name = "is_active") @SerialName("is_active") val isActive: Boolean = false,
    @ColumnInfo(name = "rejection_reason") @SerialName("rejection_reason") val rejectionReason: String? = null,
    @ColumnInfo(name = "is_paid") @SerialName("is_paid") val isPaid : Boolean,
    @ColumnInfo(name = "city") @SerialName("city") val city : String?,
    @ColumnInfo(name = "created_at") @SerialName("created_at") val createdAt: Instant = Clock.System.now(),
    @ColumnInfo(name = "updated_at") @SerialName("updated_at") val updatedAt: Instant = Clock.System.now()
) : SupabaseTable {
    companion object {
        const val TABLE_NAME = "internships"
        val empty = Internship(
            id = "",
            companyId = "",
            title = "",
            description = "",
            duration = "",
            skills = emptyList(),
            isApproved = null,
            isActive = false,
            rejectionReason = null,
            isPaid = false,
            city = "",
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now()
        )
    }

    override val tableName: String
        get() = TABLE_NAME


}