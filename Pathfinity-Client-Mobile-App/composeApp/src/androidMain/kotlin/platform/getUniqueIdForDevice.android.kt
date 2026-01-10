package platform

import android.provider.Settings
import krd.enos.pathfinity.AppContext

actual fun getUniqueIdForDevice(): String {
   return Settings.Secure.getString(AppContext.get().getContentResolver(), Settings.Secure.ANDROID_ID);
}