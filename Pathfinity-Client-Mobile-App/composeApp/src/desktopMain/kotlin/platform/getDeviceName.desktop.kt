package platform

import java.net.InetAddress

actual fun getDeviceName(): String {
   return try {
      InetAddress.getLocalHost().hostName
   } catch (e: Exception) {
      "Unknown Device"
   }
}