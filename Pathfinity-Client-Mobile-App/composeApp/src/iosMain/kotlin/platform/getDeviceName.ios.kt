package platform

actual fun getDeviceName(): String {
   return DeviceNameManager.getDeviceName()
}

object DeviceNameManager{
   lateinit var getDeviceName : () -> String
}