package domain

enum class UserType {
   Free, Pro, Admin, Seller, LimitedAdmin, Unknown;

   companion object {
      fun fromString(value: String): UserType {
         return when (value.lowercase()) {
            "free" -> Free
            "pro" -> Pro
            "admin" -> Admin
            "seller" -> Seller
            "limited_admin" -> LimitedAdmin
            else -> Unknown
         }
      }
   }
}