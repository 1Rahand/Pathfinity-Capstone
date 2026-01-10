package database

import androidx.room.TypeConverter

class StringListConvertor {
   @TypeConverter
   fun fromList(value: List<String>): String {
      return value.joinToString(",")
   }

   @TypeConverter
   fun toList(value: String): List<String> {
      return value.split(",").map { it.trim() }
   }
}