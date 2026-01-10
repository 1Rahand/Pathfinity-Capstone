package platform

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

actual fun getDatastore(): DataStore<Preferences> {
   return PreferenceDataStoreFactory.createWithPath {
      (getBaseAppUri() + "/$dataStoreFileName").toPath()
   }
}