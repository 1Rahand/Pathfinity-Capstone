package platform

import java.net.URL
import java.nio.file.Path
import java.nio.file.Paths

actual fun getBaseAppUri(): String {
   return FileLocation().getAbsoluteAppPath()!!
}

class FileLocation {
   private fun getExecutionFileLocation(): Path? {
      try {
         // Get the URL of the class file
         val location: URL = FileLocation::class.java.getProtectionDomain().getCodeSource().getLocation()


         // Convert the URL to a Path
         val path: Path = Paths.get(location.toURI())

         return path
      } catch (e: Exception) {
         e.printStackTrace()
         return null
      }
   }


   fun getAbsoluteAppPath() : String ? {
      return getExecutionFileLocation()?.parent?.toAbsolutePath()?.toString()
   }
}

