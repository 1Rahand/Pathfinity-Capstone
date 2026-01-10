package platform

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

actual object KeyManager {


   @OptIn(ExperimentalEncodingApi::class)
   actual fun saveKey(alias: String, data: ByteArray) {
      val result = iOSKeyManager.save(alias, Base64.encode(data))
      println("Saved : $result")
   }

   @OptIn(ExperimentalEncodingApi::class)
   actual fun getKey(alias: String): ByteArray? {
        val base64 = iOSKeyManager.get(alias)
        return base64?.let { Base64.decode(it) }

   }
}

object iOSKeyManager{
   lateinit var save : (alias : String , base64 : String) -> Boolean
   lateinit var get : (alias : String) -> String?
}