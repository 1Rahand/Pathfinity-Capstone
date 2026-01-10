package platform

actual fun getOsName(): String {
   return try {
      System.getProperty("os.name")
   } catch (e: Exception) {
      "Unknown"
   }
}