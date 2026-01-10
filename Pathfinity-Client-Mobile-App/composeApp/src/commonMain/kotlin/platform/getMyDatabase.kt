package platform

import androidx.room.RoomDatabase
import database.MyDatabase

expect fun getMyDatabaseBuilder(): RoomDatabase.Builder<MyDatabase>