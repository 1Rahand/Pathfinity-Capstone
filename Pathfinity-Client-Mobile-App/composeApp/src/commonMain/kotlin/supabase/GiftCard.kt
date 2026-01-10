package supabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = GiftCard.TABLE_NAME)
@Serializable
data class GiftCard(
    @PrimaryKey @ColumnInfo(name = "code") @SerialName("code") val code: String,
    @ColumnInfo(name = "created_at") @SerialName("created_at") val createdAt: Instant = Clock.System.now(),
    @ColumnInfo(name = "redeemed_at") @SerialName("redeemed_at") val redeemedAt: Instant? = null,
    @ColumnInfo(name = "redeemed_by") @SerialName("redeemed_by") val redeemedBy: String? = null,
    @ColumnInfo(name = "days") @SerialName("days") val days: Int = 365,
    @ColumnInfo(name = "serial") @SerialName("serial") val serial: Long? = null,
    @ColumnInfo(name = "updated_at") @SerialName("updated_at") val updatedAt: Instant = Clock.System.now(),
    @ColumnInfo(name = "metadata") @SerialName("metadata") val metadata: String = "",
    @ColumnInfo(name = "generated_by") @SerialName("generated_by") val generatedBy: String? = null
) : SupabaseTable {
    companion object {
        const val TABLE_NAME = "gift_cards"

        val empty =
            GiftCard(
                code = "",
                createdAt = Clock.System.now(),
                redeemedAt = null,
                redeemedBy = null,
                days = 0,
                serial = null,
                updatedAt = Clock.System.now(),
                metadata = "",
                generatedBy = null
            )
    }

    override val tableName: String
        get() = TABLE_NAME

    val isRedeemed: Boolean
        get() = redeemedAt != null
}