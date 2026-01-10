package platform

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import krd.enos.pathfinity.AppContext
import okio.Path.Companion.toPath

actual fun getDatastore(): DataStore<Preferences> {
   return PreferenceDataStoreFactory.createWithPath {
      AppContext.get().filesDir.resolve(dataStoreFileName).absolutePath.toPath()
   }
}