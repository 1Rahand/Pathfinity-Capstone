import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.AuthState
import data.RepoAuthImpl
import data.RepoSettingImpl
import data.Syncer
import domain.Setting
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RootViewModel(
   private val repoSettingImpl: RepoSettingImpl,
   private val repoAuth: RepoAuthImpl,
   private val syncer: Syncer
) : ViewModel() {


   val student = repoAuth.userStudent.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

   val rootViewState =
      combine(repoAuth.authState, repoSettingImpl.settingFlow, student) { authStatus, setting, profile ->
         println("RootViewModel : authStatus = ${authStatus::class.simpleName} , setting = ${setting::class.simpleName}")
         val value = when {
            setting.isFirstTime -> RootViewState.OnBoarding
            authStatus is AuthState.NotAuthenticated -> RootViewState.Login
//            (profile != null && (profile.firstName == "" || profile.firstName == null)) -> RootViewState.ProfileRegistration
            authStatus is AuthState.Authenticated -> RootViewState.AppContent
            else -> RootViewState.Loading
         }
         println("RootViewModel : rootViewState = ${value::class.simpleName}")
         value
      }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), RootViewState.Loading)


   init {
      viewModelScope.launch(Dispatchers.IO) {
         repoAuth.authStateRaw.collectLatest {
            println("RootViewModel : supabaseRawState = ${it::class.simpleName}")
            when (it) {
               is SessionStatus.Authenticated -> {
                  repoSettingImpl.setProfileId(it.session.user?.id ?: "")
                  repoAuth.userIdCache = it.session.user?.id
                  syncer.sync()
               }

               is SessionStatus.Initializing -> {}
               is SessionStatus.NotAuthenticated -> {
                  syncer.sync()
               }

               is SessionStatus.RefreshFailure -> {}
            }
         }
      }


   }

   val settingFlow = repoSettingImpl.settingFlow
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Setting())

}