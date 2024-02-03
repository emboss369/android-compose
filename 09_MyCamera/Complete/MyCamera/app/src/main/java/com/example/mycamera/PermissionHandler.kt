package com.example.mycamera

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun PermissionHandler(
  onGranted: (Boolean) -> Unit
) {
  val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { granted ->
    onGranted(granted)
  }
  val context = LocalContext.current
  if (androidx.core.content.ContextCompat.checkSelfPermission(
      context,
      android.Manifest.permission.CAMERA
    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
  ) {
    onGranted(true)
  } else {
    SideEffect {
      launcher.launch(android.Manifest.permission.CAMERA)
    }
  }
}