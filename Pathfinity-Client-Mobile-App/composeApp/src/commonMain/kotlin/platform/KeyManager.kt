package platform

expect object KeyManager {
   fun saveKey(alias: String, data: ByteArray)
   fun getKey(alias: String): ByteArray?
}