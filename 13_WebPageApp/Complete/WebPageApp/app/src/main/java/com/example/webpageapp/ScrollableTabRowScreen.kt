package com.example.webpageapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.webpageapp.ui.theme.WebPageAppTheme

@Composable
fun TabRowScreen() {
  var tabIndex by remember { mutableIntStateOf(0) }

  Scaffold(
    bottomBar = {
      BottomAppBar{
        TabView(tabIndex = tabIndex, onTabChange = { tabIndex = it })
      }
    }
  ) { padding ->
    Column(modifier = Modifier.padding(padding)) {
      when (tabIndex) {
        0 -> Text("Home Tab")
        1 -> WebView(url = "https://developer.android.com//")
        2 -> WebView(url = "https://www.google.com/")
      }
    }
  }
}

@Preview
@Composable
fun TabRowScreenPreview() {
  WebPageAppTheme {
    TabRowScreen()
  }
}