package platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.dataWithContentsOfFile
import platform.Foundation.writeToFile

actual object FileManager {

   /**
    * Writes [data] to a file named [fileName] inside [directory]
    * (relative to the iOS Documents folder).
    *
    * E.g., directory = "" (top-level), "/images", "/images/subject1", etc.
    * Automatically creates the subdirectories if they don't exist.
    */
   @OptIn(ExperimentalForeignApi::class)
   actual fun writeFile(directory: String, fileName: String, data: ByteArray): Boolean {
      val fileManager = NSFileManager.defaultManager
      val documentsPath = getBaseAppUri()

      // Clean up directory path (remove any leading slash).
      // If directory is empty, we'll just write directly in Documents.
      val subDir = directory.removePrefix("/")
      val fullDirPath = if (subDir.isEmpty()) {
         documentsPath
      } else {
         "$documentsPath/$subDir"
      }

      // Create the subdirectory (with intermediate dirs) if needed.
      val directoryExists : Boolean = fileManager.fileExistsAtPath(fullDirPath)

      if (!directoryExists) {
         memScoped {
            fileManager.createDirectoryAtPath(
               path = fullDirPath,
               withIntermediateDirectories = true,
               attributes = null,
               error = null
            )
         }
      }

      // Build final file path and write data
      val finalPath = "$fullDirPath/$fileName"
      val nsData = data.toNSData()
      return nsData.writeToFile(finalPath, atomically = true)
   }

   /**
    * Reads a file named [fileName] from [directory] (relative to the
    * iOS Documents folder) and returns its contents as [ByteArray],
    * or null if not found.
    */
   actual fun readFile(directory: String, fileName: String): ByteArray? {
      val documentsPath = getBaseAppUri()
      val subDir = directory.removePrefix("/")
      val fullDirPath = if (subDir.isEmpty()) {
         documentsPath
      } else {
         "$documentsPath/$subDir"
      }

      val finalPath = "$fullDirPath/$fileName"
      val fileManager = NSFileManager.defaultManager
      if (!fileManager.fileExistsAtPath(finalPath)) {
         return null
      }

      val nsData = NSData.dataWithContentsOfFile(finalPath) ?: return null
      return nsData.toByteArray()
   }


//   /**
//    * Converts a Kotlin [ByteArray] to iOS [NSData].
//    */
//   @OptIn(ExperimentalForeignApi::class)
//   private fun ByteArray.toNSData(): NSData {
//      val mutableData = NSMutableData.dataWithCapacity(size.toULong()) as NSMutableData
//      usePinned { pinned ->
//         mutableData.appendBytes(pinned.addressOf(0), size.toULong())
//      }
//      return mutableData
//   }
//
//   /**
//    * Converts iOS [NSData] to a Kotlin [ByteArray].
//    */
//   @OptIn(ExperimentalForeignApi::class)
//   private fun NSData.toByteArray(): ByteArray {
//      val byteArray = ByteArray(length.toInt())
//      byteArray.usePinned {
//         memcpy(it.addressOf(0), bytes, length)
//      }
//      return byteArray
//   }
}