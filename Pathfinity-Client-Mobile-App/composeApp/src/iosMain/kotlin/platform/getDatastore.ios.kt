package platform

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath

@OptIn(ExperimentalForeignApi::class)
actual fun getDatastore(): DataStore<Preferences> {
   return PreferenceDataStoreFactory.createWithPath(produceFile = { (getBaseAppUri() + "/$dataStoreFileName").toPath() })
}