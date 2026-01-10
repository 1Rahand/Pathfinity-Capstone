package data

import domain.result.ErrorSupabaseCancellation
import domain.result.Result
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import supabase.UserStudent

interface RepoAuth {
   val userStudent: Flow<UserStudent?>
   val authStateRaw: Flow<SessionStatus>
   val authState: Flow<AuthState>

   suspend fun getUserId(): String
   suspend fun requestOtp(email: String): Result<Unit?, ErrorSupabaseCancellation>
   suspend fun signOut(): Result<Unit, ErrorSupabaseCancellation>
   suspend fun verifyOtp(email: String, otp: String): Result<Unit, ErrorSupabaseCancellation>
   suspend fun redeemCode(giftCard: String): Result<Boolean, ErrorSupabaseCancellation>
   suspend fun saveUserStudent(userStudent: UserStudent): Result<Unit, ErrorSupabaseCancellation>
   suspend fun deleteAccount(): Result<Unit, ErrorSupabaseCancellation>
   suspend fun anonymousSignUp(): Result<Unit, ErrorSupabaseCancellation>
   suspend fun requestUpdateEmail(email: String): Result<Unit, ErrorSupabaseCancellation>
   suspend fun verifyUpdateEmailOtp(email: String, otp: String): Result<Unit, ErrorSupabaseCancellation>
}