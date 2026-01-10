package data

import domain.Appearance
import domain.Lang
import domain.Setting
import kotlinx.coroutines.flow.Flow
import supabase.SettingsSupabaseTable

interface RepoSetting {
   val isEnabled: Flow<Boolean>
   val userIdFlow: Flow<String>
   val settingFlow: Flow<Setting>

   suspend fun setAppearance(appearance: Appearance)
   suspend fun setIsFirstTime(isFirstTime: Boolean)
   suspend fun setLang(lang: Lang)
   suspend fun saveSettings(settings: List<SettingsSupabaseTable>)
   suspend fun setProfileId(id: String)
}