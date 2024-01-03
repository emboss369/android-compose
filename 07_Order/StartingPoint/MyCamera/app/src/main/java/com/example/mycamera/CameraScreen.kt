package com.example.mycamera

import android.content.Context
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture


@Composable
fun CameraScreen() {
  var isGranted by remember { mutableStateOf(false) }

  val cameraState = rememberCameraState()

  PermissionHandler { granted ->
    isGranted = granted
  }

  if (isGranted) {
    Box() {
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



