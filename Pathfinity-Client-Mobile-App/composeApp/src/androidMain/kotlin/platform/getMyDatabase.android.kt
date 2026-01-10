package platform

import androidx.room.Room
import androidx.room.RoomDatabase
import database.MyDatabase
import krd.enos.pathfinity.AppContext

actual fun getMyDatabaseBuilder(): RoomDatabase.Builder<MyDatabase> {
   val context = AppContext.get()
   val dbFile = context.getDatabasePath("my_database.db")
   return Room.databaseBuilder(
      context = context,
      name = dbFile.absolutePath,
   )
}