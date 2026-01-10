package platform

import platform.Foundation.NSData
import platform.Foundation.base64EncodedStringWithOptions
import platform.Foundation.create
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


@OptIn(ExperimentalEncodingApi::class)
fun ByteArray.toNSData(): NSData {
   val base64Encoded = Base64.encode(this)
   val data = NSData.create(base64EncodedString = base64Encoded , options = 0UL)
   return data ?: NSData()
}

@OptIn(ExperimentalEncodingApi::class)
fun NSData.toByteArray(): ByteArray {
   val base64 = this.base64EncodedStringWithOptions(0UL)
   return Base64.decode(base64)
}