package ui.platform

import android.util.Log
import android.view.SurfaceView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import data.AgoraTokenRequester
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.video.VideoCanvas
import io.ktor.client.HttpClient
import org.koin.compose.koinInject
import ui.components.environment.MyLocalUserStudent
import kotlin.uuid.Uuid

@OptIn(ExperimentalUnsignedTypes::class)
@Composable
actual fun LiveView(
   modifier: Modifier,
   channelName: String,
   uid : String
) {
   val TAG = "LiveView"
   val rtcEngine: RtcEngine = koinInject()
   val httpClient = koinInject<HttpClient>()

   // Track remote user IDs
   var remoteUid by remember { mutableStateOf<Int?>(null) }

   LaunchedEffect(Unit) {
      val options = ChannelMediaOptions().apply {
         clientRoleType = Constants.CLIENT_ROLE_AUDIENCE
         channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING
         publishMicrophoneTrack = false
         publishCameraTrack = false
      }

      AgoraTokenRequester.getAgoraToken(channelName, httpClient)
         .onSuccess {
            rtcEngine.joinChannel(it, channelName, 0, options)
         }
   }

   DisposableEffect(rtcEngine) {
      // Enable video module
      rtcEngine.enableVideo()

      // Set up event handler for remote user events
      val eventHandler = object : IRtcEngineEventHandler() {
         override fun onUserJoined(uid: Int, elapsed: Int) {
            Log.d(TAG, "Remote user joined: $uid")
            remoteUid = uid
         }

         override fun onUserOffline(uid: Int, reason: Int) {
            Log.d(TAG, "Remote user offline: $uid, reason: $reason")
            if (remoteUid == uid) {
               remoteUid = null
            }
         }

         override fun onFirstRemoteVideoFrame(uid: Int, width: Int, height: Int, elapsed: Int) {
            Log.d(TAG, "First remote video frame: $uid, size: $width x $height")
         }

         override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
            Log.d(TAG, "Successfully joined channel: $channel with uid: $uid")
         }

         override fun onError(err: Int) {
            Log.e(TAG, "RTC Error: $err")
         }
      }

      rtcEngine.addHandler(eventHandler)

      onDispose {
         rtcEngine.removeHandler(eventHandler)
         rtcEngine.leaveChannel()
      }
   }

   // Only show remote view when we have a remote UID
   remoteUid?.let { rUid ->
      RemoteVideoView(
         uid = rUid,
         rtcEngine = rtcEngine,
         modifier = modifier.fillMaxSize()
      )
   }
}

@Composable
fun RemoteVideoView(
   uid: Int,
   rtcEngine: RtcEngine,
   modifier: Modifier = Modifier
) {
   AndroidView(
      factory = { context ->
         SurfaceView(context).apply {
            setZOrderMediaOverlay(true)
         }
      },
      modifier = modifier,
      update = { surfaceView ->
         rtcEngine.setupRemoteVideo(
            VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid)
         )
      }
   )
}

