package com.example.webpageapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebView(url: String) {
  AndroidView(
    factory = { context ->
      android.webkit.WebView(context).apply {
        webChromeClient = android.webkit.WebChromeClient()
        webViewClient = android.webkit.WebViewClient()
        settings.javaScriptEnabled = true
        layoutParams = android.view.ViewGroup.LayoutParams(
          android.view.ViewGroup.LayoutParams.MATCH_PARENT,
          android.view.ViewGroup.LayoutParams.MATCH_PARENT
        )
        loadUrl(url)
      }
    }
  )
}

@Preview
@Composable
fun WebViewPreview() {
  WebView(url = "https://www.yahoo.co.jp/")
}