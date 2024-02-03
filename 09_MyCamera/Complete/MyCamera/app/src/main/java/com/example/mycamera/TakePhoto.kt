package com.example.mycamera

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TakePhoto(takePhoto: () -> Unit) {
  Button(onClick = takePhoto) {
    Text(text = "撮影")
  }
}