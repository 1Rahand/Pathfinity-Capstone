package krd.enos.pathfinity

import App
import agoraId
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
   private val PERMISSION_REQUEST_CODE = 1
   private val permissions = arrayOf(
      Manifest.permission.CAMERA,
      Manifest.permission.RECORD_AUDIO
   )

   private val requestPermissionLauncher = registerForActivityResult(
      ActivityResultContracts.RequestMultiplePermissions()
   ) { permissionsMap ->
      val allGranted = permissionsMap.all { it.value }
      if (allGranted) {
         showToast("All permissions granted")
      } else {
         showToast("Some permissions were denied. App may not work properly.")
      }
   }

   private val mRtcEventHandler = object : IRtcEngineEventHandler() {
      override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
         super.onJoinChannelSuccess(channel, uid, elapsed)
         runOnUiThread {
            showToast("Joined channel $channel")
         }
      }
      override fun onUserJoined(uid: Int, elapsed: Int) {
         runOnUiThread {
            showToast("User joined: $uid")
         }
      }
      override fun onUserOffline(uid: Int, reason: Int) {
         super.onUserOffline(uid, reason)
         runOnUiThread {
            showToast("User offline: $uid")
         }
      }
   }

   fun showToast(message: String) {
      // Implement your toast logic here
      Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
   }

   private fun checkAndRequestPermissions() {
      val permissionsToRequest = permissions.filter {
         ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
      }.toTypedArray()

      if (permissionsToRequest.isNotEmpty()) {
         requestPermissionLauncher.launch(permissionsToRequest)
      }
   }

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()

      // Request permissions at startup
      checkAndRequestPermissions()

      setContent {
         App(
            platformModule = module {
               single<RtcEngine> {
                  try {
                     val config = RtcEngineConfig().apply {
                        mContext = applicationContext
                        mAppId = agoraId
                        mEventHandler = mRtcEventHandler
                     }
                     RtcEngine.create(config).apply {
                        startPreview()
                        enableVideo()
                        enableAudio()
                     }
                  } catch (e: Exception) {
                     showToast("Error initializing RTC engine: ${e.message}")
                     throw RuntimeException("Error initializing RTC engine: ${e.message}")
                  }
               }
            }
         )
      }
   }
}

@Preview
@Composable
fun AppAndroidPreview() {
   App()
}

