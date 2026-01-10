package platform

import kotlinx.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
actual fun getUniqueIdForDevice(): String {
   val osName = System.getProperty("os.name").lowercase()
   val folderPath: Path = Paths.get(System.getProperty("user.home") + "/.b352921f018855b")

   // Ensure the folder exists, and mark it as hidden if necessary.
   try {
      if (!Files.exists(folderPath)) {
         Files.createDirectory(folderPath)
         println("Folder created: ${folderPath.toAbsolutePath()}")
      } else {
         println("Folder already exists: ${folderPath.toAbsolutePath()}")
      }
      // On Windows, set the DOS "hidden" attribute.
      if (osName.contains("win")) {
         Files.setAttribute(folderPath, "dos:hidden", true)
         println("Folder marked as hidden.")
      } else {
         println("Folder is hidden by naming convention (name starts with a dot).")
      }
   } catch (e: IOException) {
      System.err.println("Error creating or modifying the folder: ${e.message}")
   }

   // Define the key file path.
   val keyFile = folderPath.resolve("key")

   // Read the key from the file if it exists, otherwise generate and store a new one.
   return try {
      if (!Files.exists(keyFile)) {
         // Generate a UUID, remove dashes to convert to hex string format.
         val uuid = Uuid.random().toHexDashString()
         Files.write(keyFile, uuid.toByteArray())
         println("New unique key generated and stored: $uuid")
         uuid
      } else {
         // Read the unique key from the file.
         val storedKey = Files.readAllLines(keyFile).firstOrNull() ?: ""
         println("Unique key read from file: $storedKey")
         storedKey
      }
   } catch (e: IOException) {
      System.err.println("Error reading or writing the key file: ${e.message}")
      ""
   }
}