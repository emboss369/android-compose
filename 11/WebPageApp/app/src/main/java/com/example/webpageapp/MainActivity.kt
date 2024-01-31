package com.example.webpageapp

import android.os.Bundle

import android.widget.ProgressBar
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
                        //WebPageView2(url = "https://www.yahoo.co.jp/")
//                        TextField(value = text, onValueChange = {
//                                newText ->  text = newText
//                        })
                    //}
                    TabViewWeb1()
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

// 参考サイト
// https://proandroiddev.com/tabs-in-jetpack-compose-81b1496c97dc
@Composable
fun TabViewWeb1() {
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Home", "About", "Settings", "User", "Nice","Email", "Star", "Menu")

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    icon = {
                        when (index) {
                            0 -> Icon(imageVector = Icons.Default.Home, contentDescription = null)
                            1 -> Icon(imageVector = Icons.Default.Info, contentDescription = null)
                            2 -> Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                            3 -> Icon(imageVector = Icons.Default.Person, contentDescription = null)
                            4 -> Icon(imageVector = Icons.Default.ThumbUp, contentDescription = null)
                            5 -> Icon(imageVector = Icons.Default.Email, contentDescription = null)
                            6 -> Icon(imageVector = Icons.Default.Star, contentDescription = null)
                            7 -> Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                        }
                    }
                )
            }
        }
//        when (tabIndex) {
//            0 -> Text("Tab $tabIndex")
//            1 -> WebPageView(url = "https://www.yahoo.co.jp/")
//            2 -> WebPageView(url = "https://bing.com/")
//            3 -> WebPageView(url = "https://bing.com/")
//            4 -> Text("Tab $tabIndex")
//        }
        when (tabIndex) {
            0 -> Text("Home Tab")
            1 -> WebPageView(url = "https://www.yahoo.co.jp/")
            2 -> Text("Settings Tab")
            3 -> Text("User Tab")
            4 -> Text("Nice Tab")
            5 -> Text("Email Tab")
            6 -> Text("Star Tab")
            7 -> Text("Menu Tab")
            // 他のインデックスに対応するコンテンツをここに追加します
        }
    }
}

@Composable
fun TabViewWeb() {
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Home", "About", "Settings", "User", "Nice","Email", "Star", "Menu")

    Column(modifier = Modifier.fillMaxWidth()) {


        // ScrollableTabRow を使うと、スクロール可能なタブを作成します。タブが多くデザインが崩れてしまう場合はこちらを使いましょう。

        ScrollableTabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    icon = {
                        when (index) {
                            0 -> Icon(imageVector = Icons.Default.Home, contentDescription = null)
                            1 -> Icon(imageVector = Icons.Default.Info, contentDescription = null)
                            2 -> Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                            3 -> Icon(imageVector = Icons.Default.Person, contentDescription = null)
                            4 -> Icon(imageVector = Icons.Default.ThumbUp, contentDescription = null)
                            5 -> Icon(imageVector = Icons.Default.Email, contentDescription = null)
                            6 -> Icon(imageVector = Icons.Default.Star, contentDescription = null)
                            7 -> Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                        }
                    }
                )
            }
        }
        when (tabIndex) {
            0 -> WebPageView(url = "https://www.yahoo.co.jp/")
            1 -> WebPageView(url = "https://www.yahoo.co.jp/")
            2 -> WebPageView(url = "https://www.google.com/")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TabViewWebPreview(){
    TabViewWeb()
}

