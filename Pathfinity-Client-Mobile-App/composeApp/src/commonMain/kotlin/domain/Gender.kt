package domain

enum class Gender {
   Male, Female, Unknown;

   companion object {
      fun fromString(value: String): Gender {
         return when (value.lowercase()) {
            "male" -> Male
            "female" -> Female
            else -> Unknown
         }
      }
   }
}


