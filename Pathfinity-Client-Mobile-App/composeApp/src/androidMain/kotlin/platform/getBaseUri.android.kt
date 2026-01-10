package platform

import krd.enos.pathfinity.AppContext


actual fun getBaseAppUri(): String {
   return AppContext.get().filesDir.absolutePath
}