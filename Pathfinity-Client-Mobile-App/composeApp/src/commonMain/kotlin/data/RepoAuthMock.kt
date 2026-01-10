package data

import domain.result.ErrorSupabaseCancellation
import domain.result.Result
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import supabase.UserStudent

object RepoAuthMock : RepoAuth {
   private val _userStudent = MutableStateFlow<UserStudent?>(null)
   override val userStudent: Flow<UserStudent?> = _userStudent

   private val _authStateRaw = MutableStateFlow<SessionStatus>(SessionStatus.Initializing)
   override val authStateRaw: Flow<SessionStatus> = _authStateRaw

   private val _authState = MutableStateFlow<AuthState>(AuthState.NotAuthenticated)
   override val authState: Flow<AuthState> = _authState

   private var userId: String = ""

   // Controls whether auth operations should succeed or fail
   var shouldSucceed: Boolean = true

   // Helper functions to control mock state
   fun updateUserStudent(student: UserStudent?) {
      _userStudent.value = student
   }

   fun updateAuthState(authenticated: Boolean) {
      if (authenticated) {
         _authState.value = AuthState.Authenticated("mock-user-id")
         userId = "mock-user-id"
      } else {
         _authState.value = AuthState.NotAuthenticated
         userId = ""
      }
   }

   override suspend fun getUserId(): String = userId

   override suspend fun requestOtp(email: String): Result<Unit?, ErrorSupabaseCancellation> {
      delay(1000)
      return if (shouldSucceed) {
         Result.Success(Unit)
      } else {
         Result.Error(ErrorSupabaseCancellation.Rest)
      }
   }

   override suspend fun signOut(): Result<Unit, ErrorSupabaseCancellation> {
      delay(1000)
      if (shouldSucceed) {
         userId = ""
         _authState.value = AuthState.NotAuthenticated
         return Result.Success(Unit)
      }
      return Result.Error(ErrorSupabaseCancellation.Rest)
   }

   override suspend fun verifyOtp(email: String, otp: String): Result<Unit, ErrorSupabaseCancellation> {
      delay(1000)
      return if (shouldSucceed) {
         userId = "mock-user-id"
         _authState.value = AuthState.Authenticated(userId)
         Result.Success(Unit)
      } else {
         Result.Error(ErrorSupabaseCancellation.Rest)
      }
   }

   override suspend fun redeemCode(giftCard: String): Result<Boolean, ErrorSupabaseCancellation> {
        delay(1000)
        return if (shouldSucceed) {
             Result.Success(true)
        } else {
             Result.Error(ErrorSupabaseCancellation.Rest)
        }
   }

   override suspend fun saveUserStudent(userStudent: UserStudent): Result<Unit, ErrorSupabaseCancellation> {
      delay(1000)
      return if (shouldSucceed) {
         _userStudent.value = userStudent
         Result.Success(Unit)
      } else {
         Result.Error(ErrorSupabaseCancellation.Rest)
      }
   }

   override suspend fun deleteAccount(): Result<Unit, ErrorSupabaseCancellation> {
      delay(1000)
      return if (shouldSucceed) {
         userId = ""
         _authState.value = AuthState.NotAuthenticated
         Result.Success(Unit)
      } else {
         Result.Error(ErrorSupabaseCancellation.Rest)
      }
   }

   override suspend fun anonymousSignUp(): Result<Unit, ErrorSupabaseCancellation> {
      delay(1000)
      return if (shouldSucceed) {
         userId = "anonymous-user-id"
         _authState.value = AuthState.Authenticated(userId)
         Result.Success(Unit)
      } else {
         Result.Error(ErrorSupabaseCancellation.Rest)
      }
   }

   override suspend fun requestUpdateEmail(email: String): Result<Unit, ErrorSupabaseCancellation> {
      delay(1000)
      return if (shouldSucceed) Result.Success(Unit) else Result.Error(ErrorSupabaseCancellation.Rest)
   }

   override suspend fun verifyUpdateEmailOtp(email: String, otp: String): Result<Unit, ErrorSupabaseCancellation> {
      delay(1000)
      return if (shouldSucceed) Result.Success(Unit) else Result.Error(ErrorSupabaseCancellation.Rest)
   }
}