package platform

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
actual fun getUniqueIdForDevice(): String {
   KeyManager.getKey("sheekar_unique_id")?.let {
      return it.decodeToString()
   }

   val uniqueId = Uuid.random().toHexDashString()

   KeyManager.saveKey("sheekar_unique_id", uniqueId.encodeToByteArray())
   return uniqueId
}