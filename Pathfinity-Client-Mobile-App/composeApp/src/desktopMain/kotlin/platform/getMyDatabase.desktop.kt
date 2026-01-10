package platform

import androidx.room.Room
import androidx.room.RoomDatabase
import database.MyDatabase
import java.io.File

actual fun getMyDatabaseBuilder(): RoomDatabase.Builder<MyDatabase> {
   val dbFile = File(
      getBaseAppUri(),
      "my_database.db"
   )

   return Room.databaseBuilder<MyDatabase>(
      name = dbFile.absolutePath,
   )
}