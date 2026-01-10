package platform

import androidx.sqlite.SQLiteDriver
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

actual fun getSqliteDriver(): SQLiteDriver {
   return BundledSQLiteDriver()
}