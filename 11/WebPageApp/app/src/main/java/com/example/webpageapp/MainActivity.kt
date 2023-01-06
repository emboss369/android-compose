package com.example.webpageapp

import android.os.Bundle

import android.widget.ProgressBar
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.webpageapp.ui.theme.WebPageAppTheme
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WebPageAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    //Column() {
                        //var text by remember { mutableStateOf("https://www.yahoo.co.jp/") }
                        WebPageView2(url = "https://www.yahoo.co.jp/")
//                        TextField(value = text, onValueChange = {
//                                newText ->  text = newText
//                        })
                    //}
                }
            }
        }
    }
}

// https://developer.android.com/jetpack/compose/interop/interop-apis?hl=ja#views-in-compose
// Compose UI に Android View 階層を含めることができます。
// Compose でまだ利用できない UI 要素（AdView など）を使用する場合に便利です。

// 先に、　 <uses-permission android:name="android.permission.INTERNET"/>
// するのをお忘れなく。


@Composable
fun WebPageView(url: String) {


    AndroidView(
        // コロン2つは、関数（呼び出し可能）参照を取得するために使用されます。ここではFactoryには「::WebView」とすることで、従来のビューのコンストラクタを呼び出しています。
        factory = { context ->
            android.webkit.WebView(context).apply {
                webViewClient = android.webkit.WebViewClient()
                settings.javaScriptEnabled = true
                loadUrl(url)
            }
        }
    )
    //Text(text = "${url} ${backPressed} ${counter}")

    // https://developer.android.com/jetpack/compose/libraries?hl=ja#handling_the_system_back_button
    // システムの [戻る] ボタンの処理
    // コンポーザブル内からカスタムの「戻る」ナビゲーションを提供し、
    // システムの [戻る] ボタンのデフォルトの動作をオーバーライドするには、
    // コンポーザブルで BackHandler を使用してイベントをインターセプトします。
    // 最初の引数は、BackHandler が現在有効かどうかを制御します。
    // この引数を使用すると、コンポーネントの状態に基づいてハンドラを一時的に無効にできます。
    // ユーザーがシステムの「戻る」イベントをトリガーし、BackHandler が有効になっている場合、末尾のラムダが呼び出されます。
//    BackHandler() {
//
//    }
}

@Preview(showBackground = true)
@Composable
fun WebPageViewPreview() {

    WebPageAppTheme {
        //Column() {
            //var text by remember { mutableStateOf("https://www.yahoo.co.jp") }
            //TextField(value = text, onValueChange = { newText -> text = newText})
            WebPageView(url = "https://www.yahoo.co.jp")
        //}

    }
}

// コラム
// アコンパニスト（伴奏者）、https://github.com/google/accompanist/tree/main/web
// Accompanistに、WebViewがある。こちらを使う。
// ちなみに、
// Accompanistアコンパニスト（伴奏者）とは、Composeの補助ライブラリである。Composeには作曲という意味があり、Composer（作曲家）を補助する意味でAccompanist（伴奏者）と名付けられているようです。
// Jetpack Compose での基本的な WebView サポートのための WebView のラッパー。
// 基本的な使い方
//このラッパーを実装するには、2 つの重要な API が必要です。レイアウトを提供する WebView と、表示する URL を含む記憶された状態を提供する rememberWebViewState(url) です。
//
//基本的な使い方は以下の通りです。
// implementation "com.google.accompanist:accompanist-webview:0.2.8"

@Composable
fun WebPageView2(url: String) {
    val state = rememberWebViewState(url)

    WebView(state = state,
        onCreated = { webView ->
            webView.settings.javaScriptEnabled = true
        }
    )
}

@Preview(showBackground = true)
@Composable
fun WebPageView2Preview() {

    WebPageAppTheme {
        WebPageView2(url = "https://www.yahoo.co.jp/")
    }
}