package supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.datetime.Instant

suspend inline fun <reified T : SupabaseTable> SupabaseClient.getUpdatedList(tableInstance: T, maxUpdatedAt: Instant): List<T> {
   return this.from(tableInstance.tableName).select {
      filter {
         gt("updated_at", maxUpdatedAt)
      }
   }.decodeAsOrNull<List<T>>() ?: emptyList()
}
