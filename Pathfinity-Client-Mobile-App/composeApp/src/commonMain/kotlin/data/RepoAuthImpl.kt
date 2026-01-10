package data

import database.MyDao
import domain.result.ErrorSupabaseCancellation
import domain.result.Result
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.OTP
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import supabase.UserStudent

sealed interface AuthState {
   data class Authenticated(val profileId: String) : AuthState
   data object NotAuthenticated : AuthState
}

@OptIn(ExperimentalCoroutinesApi::class)
class RepoAuthImpl(
   private val supabase: SupabaseClient,
   private val repoSettingImpl: RepoSettingImpl,
   private val dao: MyDao,
   private val syncer : Syncer
) : RepoAuth {

   var userIdCache: String? = null

   private suspend fun getUserIdFromSupabase(): String? {
      return withTimeoutOrNull(5000) { // 5 second timeout
         supabase.auth.sessionStatus
            .first { it !is SessionStatus.Initializing }
            .let { status ->
               when (status) {
                  is SessionStatus.Authenticated -> status.session.user?.id
                  else -> null
               }
            }
      }
   }

   override suspend fun getUserId(): String {
      userIdCache?.takeIf { it.isNotEmpty() }?.let { return it }

      val idFromSettings = repoSettingImpl.settingFlow.first().profileId
      if (idFromSettings.isNotEmpty()) {
         userIdCache = idFromSettings
         return idFromSettings
      }

      val idFromSupabase = getUserIdFromSupabase()
      userIdCache = idFromSupabase
      return idFromSupabase ?: ""
   }

   override val userStudent = repoSettingImpl.userIdFlow.flatMapLatest { id ->
      if (id.isEmpty()) {
         MutableStateFlow(null)
      } else {
         dao.getUserStudentByIdFlow(id)
      }
   }


   override val authStateRaw = supabase.auth.sessionStatus

   override val authState = repoSettingImpl.settingFlow.map { it.profileId }.map {
      if (it.isEmpty()) AuthState.NotAuthenticated else AuthState.Authenticated(it)
   }


   override suspend fun requestOtp(email: String) = wrapResultRepo {
      withContext(Dispatchers.IO) {
         supabase.auth.signUpWith(OTP) {
            this.email = email.lowercase().trim()
            this.data = buildJsonObject {
                put("p_user_type", "user_student")
            }
         }
      }
   }

   override suspend fun signOut() = wrapResultRepo {
      withContext(Dispatchers.IO + NonCancellable) {
         val userId = getUserId()
         dao.clearAllData()
         supabase.auth.signOut()
         repoSettingImpl.setProfileId("")
      }
   }


   override suspend fun verifyOtp(email: String, otp: String) = wrapResultRepo {
      withContext(Dispatchers.IO + NonCancellable) {
         supabase.auth.verifyEmailOtp(OtpType.Email.EMAIL, email = email.lowercase().trim(), token = otp)
         println("AuthRepo : ${getUserIdFromSupabase()}")
      }
   }

   override suspend fun saveUserStudent(userStudent: UserStudent) = wrapResultRepo {
      withContext(Dispatchers.IO) {
         val student = supabase.from(UserStudent.TABLE_NAME).upsert(userStudent){
            select()
            limit(1)
            single()
         }.decodeAs<UserStudent>()
         dao.upsertUserStudent(student)
      }
   }

      override suspend fun redeemCode(giftCard: String) = wrapResultRepo {
         withContext(Dispatchers.IO){
            val result = supabase.rpcRedeemGiftCard(giftCard)
            if (result) syncer.reloadProfile()
            result
         }
      }


   override suspend fun anonymousSignUp() = wrapResultRepo {
      withContext(Dispatchers.IO) {
         val jsonObject = buildJsonObject {
            put("p_user_type" , "user_student")
         }
         supabase.auth.signInAnonymously(data = jsonObject)
         println("AuthRepo : ${getUserIdFromSupabase()}")
//         supabase.rpcSetProfileDeviceInfo(getOsName(), getDeviceName(), getUniqueIdForDevice())
      }
   }

   override suspend fun deleteAccount() = wrapResultRepo {
      withContext(Dispatchers.IO) {
         val userId = getUserId()
         supabase.rpcDeleteAccount()
         repoSettingImpl.setProfileId("")
         dao.deleteProfile(userId)
         signOut()
         Unit
      }
   }

   override suspend fun requestUpdateEmail(email: String) = wrapResultRepo {
      withContext(Dispatchers.IO) {
         supabase.auth.updateUser {
            this.email = email
         }
         Unit
      }
   }

   override suspend fun verifyUpdateEmailOtp(email: String, otp: String) = wrapResultRepo {
      withContext(Dispatchers.IO) {
         supabase.auth.verifyEmailOtp(OtpType.Email.EMAIL_CHANGE, email = email, token = otp)
         val profiles = supabase.from(UserStudent.TABLE_NAME).select().decodeList<UserStudent>()
         dao.upsertUserStudents(profiles)
         Unit
      }
   }


}