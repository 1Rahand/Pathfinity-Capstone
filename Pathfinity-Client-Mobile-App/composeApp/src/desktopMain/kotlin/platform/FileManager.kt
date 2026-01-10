package platform

import java.io.File

actual object FileManager {
   actual fun writeFile(directory: String, fileName: String, data: ByteArray): Boolean {
      try {
         val directoryFile = File(getBaseAppUri() + "/" + directory)
         if (!directoryFile.exists()) {
            directoryFile.mkdirs()
         }

         val file = File(directoryFile, fileName)
         if (!file.exists()){
            file.createNewFile()
         }
         file.writeBytes(data)
         return true
      }catch (e : Exception){
         e.printStackTrace()
         return false
      }
   }

   actual fun readFile(directory: String, fileName: String): ByteArray? {
      try {
         val file = File(getBaseAppUri() + "/" + directory, fileName)
         if (file.exists()){
            return file.readBytes()
         }
      }catch (e : Exception){
         e.printStackTrace()
      }
      return null
   }
}