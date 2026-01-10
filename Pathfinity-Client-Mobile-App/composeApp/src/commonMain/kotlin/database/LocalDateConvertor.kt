package database

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

class LocalDateConvertor {
   @TypeConverter
   fun fromLocalDate(value: LocalDate): String {
      return value.toString() // LocalDate already formats as ISO-8601 (YYYY-MM-DD)
   }

   @TypeConverter
   fun toLocalDate(value: String): LocalDate {
      return LocalDate.parse(value)
   }
}