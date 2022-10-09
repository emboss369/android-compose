package com.android.example.mycamera

import android.Manifest
import android.content.pm.PackageManager
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
        onGranted(granted)
    }
    val context = LocalContext.current
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        // もう権限持ってる
        onGranted(true)
    } else {
        // https://stackoverflow.com/a/68331596/2520998

        // SideEffect: Compose の状態を非 Compose コードに公開する
        // 再コンポジションが成功するたびに呼び出される SideEffect コンポーザブルを使用します。
        SideEffect {
            // 権限持てないので、ActivityResultContracts
            // .RequestPermission()
            // で権限を要求、要求する権限は、Manifestに記載されているもの
            launcher.launch(Manifest.permission.CAMERA)
        }
    }
}