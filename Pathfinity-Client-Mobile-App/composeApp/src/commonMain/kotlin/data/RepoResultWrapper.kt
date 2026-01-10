package data

import domain.result.ErrorSupabaseCancellation
import domain.result.Result
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.CancellationException

inline fun <T> wrapResultRepo(block: () -> T): Result<T, ErrorSupabaseCancellation> {
   return try {
      Result.Success(block())
   } catch (e: CancellationException) {
      e.printStackTrace()
      Result.Error(ErrorSupabaseCancellation.Cancelled)
   } catch (e: RestException) {
      e.printStackTrace()
      Result.Error(ErrorSupabaseCancellation.Rest)
   } catch (e: HttpRequestException) {
      e.printStackTrace()
      Result.Error(ErrorSupabaseCancellation.Network)
   } catch (e: HttpRequestTimeoutException) {
      e.printStackTrace()
      Result.Error(ErrorSupabaseCancellation.Network)
   } catch (e: Exception) {
      e.printStackTrace()
      Result.Error(ErrorSupabaseCancellation.Unknown)
   }
}

