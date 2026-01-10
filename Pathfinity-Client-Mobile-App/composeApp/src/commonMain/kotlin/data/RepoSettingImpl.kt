package data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import domain.Appearance
import domain.Lang
import domain.Setting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import supabase.SettingsSupabaseTable


// Newer DataStore Code
class RepoSettingImpl(
   private val dataStore: DataStore<Preferences>,
) : RepoSetting {
   private val appearanceKey = stringPreferencesKey("appearance")
   private val isFirstTimeKey = booleanPreferencesKey("is_first_time")
   private val langKey = stringPreferencesKey("lang")
   private val appstoreLinkKey = stringPreferencesKey("appstore_link")
   private val googlePlayLinkKey = stringPreferencesKey("google_play_link")
   private val desktopLinkKey = stringPreferencesKey("desktop_link")
   private val adminViewModeKey = stringPreferencesKey("admin_view_mode")

   private val onlineVersionKey = stringPreferencesKey("pathfinity_version")
   private val fibPhoneKey = stringPreferencesKey("fib_phone")
   private val whatsappPhoneKey = stringPreferencesKey("whatsapp_phone")
   private val instagramLink = stringPreferencesKey("instagram_link")
   private val facebookLink = stringPreferencesKey("facebook_link")
   private val pathfinityVersionMandatory = stringPreferencesKey("pathfinity_version_mandatory")
   private val hasEverSyncedKey = booleanPreferencesKey("has_ever_synced")
   private val hasEverLoadInitialDataKey = booleanPreferencesKey("has_ever_load_initial_data")
   private val profileIdKey = stringPreferencesKey("profile_id")

   override val userIdFlow = dataStore.data.map { it[profileIdKey] ?: "" }

   override val settingFlow = dataStore.data.map { preferences ->
      val appearance = preferences[appearanceKey] ?: Appearance.SystemDefault.name
      val isFirstTime = preferences[isFirstTimeKey] ?: true
      val lang = preferences[langKey] ?: Lang.Krd.name
      val appstoreLink = preferences[appstoreLinkKey] ?: "https://apps.apple.com/iq/app/recheck-app/id6455685408"
      val googlePlayLink = preferences[googlePlayLinkKey] ?: "https://play.google.com/store/apps/details?id=com.enos.recheck.android"
      val desktopLink = preferences[desktopLinkKey] ?: "https://sheekar.app/download"
      val onlineVersion = preferences[onlineVersionKey] ?: "1.0.0"
      val fibPhone = preferences[fibPhoneKey] ?: "7716777676"
      val whatsappPhone = preferences[whatsappPhoneKey] ?: "7716777676"
      val instagramLink = preferences[instagramLink] ?: "https://www.instagram.com/sheekar.app/"
      val facebookLink = preferences[facebookLink] ?: "https://www.facebook.com/profile.php?id=61569973341551"
      val sheekarVersionMandatory = preferences[pathfinityVersionMandatory] ?: "1.0.0"
      val hasEverSynced = preferences[hasEverSyncedKey] ?: false
      val hasEverLoadInitialData = preferences[hasEverLoadInitialDataKey] ?: false
      val profileId = preferences[profileIdKey] ?: ""

      Setting(
         appearance = Appearance.valueOf(appearance),
         isFirstTime = isFirstTime,
         lang = Lang.valueOf(lang),
         appstoreLink = appstoreLink,
         desktopLink = desktopLink,
         googlePlayLink = googlePlayLink,
         onlineVersion = onlineVersion,
         fibPhone = fibPhone,
         whatsappPhone = whatsappPhone,
         instagramLink = instagramLink,
         facebookLink = facebookLink,
         sheekarVersionMandatory = sheekarVersionMandatory,
         hasEverSyncedPrivateInfo = hasEverSynced,
         hasEverLoadInitialData = hasEverLoadInitialData,
         profileId = profileId,
      )
   }

   private suspend fun <T> setPreference(key: Preferences.Key<T>, value: T) {
      dataStore.edit { preferences ->
         preferences[key] = value
      }
   }

   // Convenience functions to set specific preferences
   override suspend fun setAppearance(appearance: Appearance) {
      setPreference(appearanceKey, appearance.name)
   }

   override suspend fun setIsFirstTime(isFirstTime: Boolean) {
      setPreference(isFirstTimeKey, isFirstTime)
   }

   override suspend fun setLang(lang: Lang) {
      setPreference(langKey, lang.name)
   }

   override suspend fun setProfileId(id: String) {
      setPreference(profileIdKey, id)
   }

   override val isEnabled: Flow<Boolean>
      get() = flowOf(true)

   private suspend fun setByKey(key: String, value: String) {
      setPreference(stringPreferencesKey(key), value)
   }

   override suspend fun saveSettings(settings: List<SettingsSupabaseTable>) {
      settings.forEach {
         setByKey(it.key, it.value)
      }
   }
}


