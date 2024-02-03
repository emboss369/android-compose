package com.example.mycamera

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun CameraScreen() {
  var isGranted by remember { mutableStateOf(false) }
  val cameraState = rememberCameraState()
  PermissionHandler { granted ->
    isGranted = granted
  }
  if (isGranted) {
    Box {
      PreviewCamera { ctx ->
        cameraState.startCamera(ctx)
      }
      TakePhoto {
        cameraState.takePhoto()
      }
    }
  } else {
    Text(text = "カメラの権限がありません")
  }
}