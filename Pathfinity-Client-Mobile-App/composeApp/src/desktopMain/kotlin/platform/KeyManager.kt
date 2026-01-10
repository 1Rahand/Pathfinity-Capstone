package platform

import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.security.KeyStore
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

actual object KeyManager {

   // Constants (NOT secure to hard-code in production)
   private const val KEYSTORE_TYPE = "JCEKS"
   private const val KEYSTORE_FILENAME = "myKeys.jks"
   private const val KEYSTORE_PASSWORD = "changeit"

   /**
    * Returns the path to the keystore file, e.g. <appPath>/myKeys.jks
    */
   private fun getKeyStorePath(): Path? {
      val appPath = getBaseAppUri()
      return Paths.get(appPath, KEYSTORE_FILENAME)
   }

   /**
    * Loads the existing keystore from disk, or creates a new one if it doesn't exist.
    */
   private fun loadKeyStore(): KeyStore? {
      return try {
         val keyStore = KeyStore.getInstance(KEYSTORE_TYPE)
         val keyStorePath = getKeyStorePath() ?: return null

         if (Files.exists(keyStorePath)) {
            // Load existing keystore
            FileInputStream(keyStorePath.toFile()).use { fis ->
               keyStore.load(fis, KEYSTORE_PASSWORD.toCharArray())
            }
         } else {
            // Create a new empty keystore
            keyStore.load(null, KEYSTORE_PASSWORD.toCharArray())
            saveKeyStore(keyStore)
         }
         keyStore
      } catch (e: Exception) {
         e.printStackTrace()
         null
      }
   }

   /**
    * Writes the in-memory keystore back to disk.
    */
   private fun saveKeyStore(keyStore: KeyStore) {
      val keyStorePath = getKeyStorePath() ?: return
      // Ensure directory exists
      Files.createDirectories(keyStorePath.parent)
      FileOutputStream(keyStorePath.toFile()).use { fos ->
         keyStore.store(fos, KEYSTORE_PASSWORD.toCharArray())
      }
   }

   /**
    * Saves arbitrary bytes under the given alias.
    * Internally, we store them as a "RAW" SecretKeySpec in the keystore.
    */
   actual fun saveKey(alias: String, data: ByteArray) {
      try {
         val keyStore = loadKeyStore() ?: return

         // Convert your arbitrary bytes into a SecretKey
         val secretKey: SecretKey = SecretKeySpec(data, "RAW")

         // Wrap it in a KeyStore Entry
         val entry = KeyStore.SecretKeyEntry(secretKey)
         val protection = KeyStore.PasswordProtection(KEYSTORE_PASSWORD.toCharArray())

         // Store the entry under the alias
         keyStore.setEntry(alias, entry, protection)

         // Save changes to disk
         saveKeyStore(keyStore)
      } catch (e: Exception) {
         e.printStackTrace()
      }
   }

   /**
    * Retrieves previously stored bytes from the keystore under the given alias.
    * Returns null if not found or there's an error.
    */
   actual fun getKey(alias: String): ByteArray? {
      return try {
         val keyStore = loadKeyStore() ?: return null
         val protection = KeyStore.PasswordProtection(KEYSTORE_PASSWORD.toCharArray())

         val entry = keyStore.getEntry(alias, protection)
         if (entry is KeyStore.SecretKeyEntry) {
            // Convert the stored secret key back to the original bytes
            entry.secretKey.encoded
         } else {
            null
         }
      } catch (e: Exception) {
         e.printStackTrace()
         null
      }
   }
}