package platform

import android.os.Build
import androidx.sqlite.SQLiteDriver
import androidx.sqlite.driver.AndroidSQLiteDriver
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

actual fun getSqliteDriver(): SQLiteDriver {
   // Use BundledSQLiteDriver for Android 30 and below
   // and AndroidSQLiteDriver for Android 31 and above
   // This is a workaround for the issue with AndroidSqliteDriver where it caused crashes
   if (Build.VERSION.SDK_INT <= 30) {
      return BundledSQLiteDriver()
   }
   return AndroidSQLiteDriver()
}