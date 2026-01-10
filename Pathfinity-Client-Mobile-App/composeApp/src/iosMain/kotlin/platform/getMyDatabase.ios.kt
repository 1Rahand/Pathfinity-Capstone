package platform

import androidx.room.Room
import androidx.room.RoomDatabase
import database.MyDatabase

actual fun getMyDatabaseBuilder(): RoomDatabase.Builder<MyDatabase> {
   val dbFilePath = getBaseAppUri() + "/my_database.db"
   return Room.databaseBuilder<MyDatabase>(
      name = dbFilePath,
   )
}