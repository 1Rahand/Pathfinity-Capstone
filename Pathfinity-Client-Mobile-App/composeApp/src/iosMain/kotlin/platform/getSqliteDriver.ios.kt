package platform

import androidx.sqlite.SQLiteDriver
import androidx.sqlite.driver.NativeSQLiteDriver

actual fun getSqliteDriver(): SQLiteDriver {
   return NativeSQLiteDriver()
}