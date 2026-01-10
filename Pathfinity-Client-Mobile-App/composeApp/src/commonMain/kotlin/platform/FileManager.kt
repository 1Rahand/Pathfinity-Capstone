package platform

expect object FileManager {
   fun writeFile(directory: String, fileName: String, data: ByteArray): Boolean
   fun readFile(directory: String, fileName: String): ByteArray?
}