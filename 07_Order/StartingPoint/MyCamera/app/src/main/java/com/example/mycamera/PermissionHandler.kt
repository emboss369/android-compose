package com.example.mycamera

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun PermissionHandler(onGranted: (Boolean) -> Unit) {
  val launcher = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestPermission()
  ) { granted ->
    Log.d("RequestPermission", "granted: $granted")
    onGranted(granted)
  }

  val context = LocalContext.current
  if (ContextCompat.checkSelfPermission(
      context,
      Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
  ) {
    Log.d("RequestPermission", "already granted")
    onGranted(true)
  } else {
    SideEffect {
      Log.d("RequestPermission", "request permission")
      launcher.launch(Manifest.permission.CAMERA)
    }
  }
}