package platform

import android.os.Build

actual fun getDeviceName(): String {
   val manufacturer = Build.MANUFACTURER.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
   val model = Build.MODEL
   return if (model.startsWith(manufacturer)) {
      model.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
   } else {
      "$manufacturer $model"
   }
}