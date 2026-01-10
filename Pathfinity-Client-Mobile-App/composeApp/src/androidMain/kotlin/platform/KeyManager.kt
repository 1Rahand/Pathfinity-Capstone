package platform

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import krd.enos.pathfinity.AppContext
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

actual object KeyManager {
   private const val androidKeyStore = "AndroidKeyStore"

   // Save ByteArray securely in Keystore
   actual fun saveKey(alias: String, data: ByteArray) {
      try {
         // Generate a key if not already present
         val secretKey = getOrCreateSecretKey(alias)

         // Encrypt the ByteArray using the key
         val cipher = Cipher.getInstance("AES/GCM/NoPadding")
         cipher.init(Cipher.ENCRYPT_MODE, secretKey)

         val iv = cipher.iv // Store this securely alongside the ciphertext
         val encryptedData = cipher.doFinal(data)

         // Save IV and encryptedData to SharedPreferences or local storage
         saveToStorage(alias, iv, encryptedData)

      } catch (e: Exception) {
         e.printStackTrace()
      }
   }

   // Retrieve and decrypt the key
   @OptIn(ExperimentalEncodingApi::class)
   actual fun getKey(alias: String): ByteArray? {
      try {
         // Load Keystore
         val keyStore = KeyStore.getInstance(androidKeyStore).apply { load(null) }

         // Retrieve the secret key
         val secretKey = keyStore.getKey(alias, null) as? SecretKey
            ?: throw Exception("Key not found in Keystore")

         // Retrieve IV and encrypted data from storage
         val preferences = AppContext.get().getSharedPreferences("SecurePrefs", Context.MODE_PRIVATE)
         val iv = Base64.decode(preferences.getString("$alias-IV", null) ?: "")
         val encryptedData = Base64.decode(preferences.getString("$alias-EncryptedData", null) ?: "")

         // Decrypt the data using the secret key
         val cipher = Cipher.getInstance("AES/GCM/NoPadding")
         val spec = GCMParameterSpec(128, iv)
         cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

         return cipher.doFinal(encryptedData)
      } catch (e: Exception) {
         e.printStackTrace()
         return null
      }
   }

   // Generate or retrieve the secret key
   private fun getOrCreateSecretKey(alias: String): SecretKey {
      val keyStore = KeyStore.getInstance(androidKeyStore).apply { load(null) }

      // Check if key already exists
      keyStore.getKey(alias, null)?.let {
         return it as SecretKey
      }

      // Generate a new key
      val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, androidKeyStore)
      val keyGenParameterSpec = KeyGenParameterSpec.Builder(
         alias,
         KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
      ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
         .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
         .setKeySize(256)
         .build()

      keyGenerator.init(keyGenParameterSpec)
      return keyGenerator.generateKey()
   }

   // Save IV and encrypted data to persistent storage
   @OptIn(ExperimentalEncodingApi::class)
   private fun saveToStorage(alias: String, iv: ByteArray, encryptedData: ByteArray) {
      val preferences = AppContext.get().getSharedPreferences("SecurePrefs", Context.MODE_PRIVATE)
      preferences.edit().apply {
         putString("$alias-IV", Base64.encode(iv))
         putString("$alias-EncryptedData", Base64.encode(encryptedData))
         apply()
      }
   }


}