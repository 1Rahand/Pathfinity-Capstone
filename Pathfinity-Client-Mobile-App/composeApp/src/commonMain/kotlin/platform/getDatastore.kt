package platform

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

expect fun getDatastore(): DataStore<Preferences>
internal const val dataStoreFileName = "pathfinity.preferences_pb"