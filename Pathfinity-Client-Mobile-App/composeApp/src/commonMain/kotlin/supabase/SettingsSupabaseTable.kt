package supabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = SettingsSupabaseTable.TABLE_NAME)
@Serializable
data class SettingsSupabaseTable(
    @PrimaryKey @ColumnInfo(name = "key") @SerialName("key") val key: String,
    @ColumnInfo(name = "value") @SerialName("value") val value: String,
    @ColumnInfo(name = "updated_at") @SerialName("updated_at") val updatedAt: Instant = Clock.System.now()
) : SupabaseTable {
    companion object {
        const val TABLE_NAME = "settings"
    }

    override val tableName: String
        get() = TABLE_NAME
}