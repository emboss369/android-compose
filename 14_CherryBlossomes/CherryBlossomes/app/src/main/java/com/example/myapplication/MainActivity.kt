package com.example.myapplication

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MyApplicationTheme {
        // A surface container using the 'background' color from the theme
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          Greeting("Android")
        }
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(
    text = "Hello $name!",
    modifier = modifier
  )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  MyApplicationTheme {
    Greeting("Android")
  }
}

/*
JSONを扱うにはGoogleのライブラリであるgsonなどが有名ですが、
今回は https://github.com/Kotlin/kotlinx.serialization
を使います。
これはKotlin言語の開発元であるJetBrainが手掛けるライブラリなので、Kotlinとの相性が良いのでおすすめです。
詳しくは
https://github.com/Kotlin/kotlinx.serialization#gradle
に記載のある通りだとだめで、
プロジェクトの方のGradleを
plugins {
    id 'com.android.application' version '8.0.0' apply false
    id 'com.android.library' version '8.0.0' apply false
    id 'org.jetbrains.kotlin.android' version '1.7.20' apply false

    // for kotlinx.serialization
    // ポイント、現在使用中のKotlinバージョンと合わせる。上の行にある、org.jetbrains.kotlin.android
    // が、'1.7.20' なので、それに合わせて下のバージョンも同じにすること！
    id 'org.jetbrains.kotlin.plugin.serialization' version '1.7.20' apply false
}
それかれ、モジュールのGradleで、
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'

    // for kotlinx.serialization
    id 'org.jetbrains.kotlin.plugin.serialization'
}


dependencies {
    // for kotlinx.serialization
    // このバージョンも重要。
    // id 'org.jetbrains.kotlin.plugin.serialization' version '1.7.20' apply false
    // に対応するのは、1.4.1。のようだ。
    implementation "org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1"




としたら、ようやくちゃんと動いた。


 */

/*
100Cherry_List.jsonは、LinkData（http://linkdata
.org/）で公開されている、日本さくら名所100選、というデータを利用しました。
このデータをXMLに変換して利用しています。
assetsに入れます。
*/

@Serializable
data class Cherry(
  val wiki: String, val name: String, val pref: String,
  val longitude: String, val latitude: String
)

fun getJson(resources: Resources): String {
  val assetManager = resources.assets
  val inputStream = assetManager.open("100Cherry_List.json")
  val bufferedReader = BufferedReader(InputStreamReader(inputStream))
  return bufferedReader.readText()
}
fun getCherryList(str: String): List<Cherry> {
  // Point!! ここでは、インポート宣言である、
  // import kotlinx.serialization.decodeFromString
  // が必須です。そうしないとエラーになります。
  // 詳しくはこちらにある、が読んでない。
  // https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/serialization-guide.md
  val obj = Json.decodeFromString<List<Cherry>>(str)
  return obj
}


@Composable
fun CherryList(list: List<Cherry>, onSelected: (Cherry) -> Unit) {

  // verticalArrangement = Arrangement.spacedBy
  // これで、カラムの各セルの間に余白を開けることが出ます。
  // さらに
  // .padding(horizontal = 16.dp)
  // で、左右のみ余白を追加しました。

  // .padding(all = 16.dp)
  // で、上下左右に余白を追加できます。

  // このままではスクロールしてくれません
  // .verticalScroll(rememberScrollState())
  // も必要です。追加します。
  // なんででしょうね？最初からスクロールすればいいのにね・


  Column(
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier
      .padding(all = 16.dp)
      .verticalScroll(rememberScrollState())
  ) {
    list.forEach { cherry ->
      CherryRow(cherry, onSelected)
    }
  }
}

@Composable
fun CherryRow(cherry: Cherry, onSelected: (Cherry) -> Unit) {

  // もしまだ説明していななら、
  // fillMaxWidth()について説明すること。



  Column(modifier = Modifier
    .fillMaxWidth()
    .background(Color.Yellow)
  ) {

    Text(
      text = cherry.pref,
      color = Color.White,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.background(
        color = Color(0xFFFF0000)
      )
    )


    Text(
      text = cherry.name,
      fontSize = 30.sp,
      overflow = TextOverflow.Ellipsis
    )
    Row(horizontalArrangement = Arrangement.End, modifier = Modifier
      .fillMaxWidth()) {

      Icon(
        imageVector = Icons.Default.LocationOn,
        contentDescription = null,
        modifier = Modifier.clickable{
          onSelected(cherry)
        }
      )
      Text(text = "${cherry.longitude}, ${cherry.latitude}")
    }

  }
}

// テスト用に２件のデータを使う。
var teststr = """
[
 {
  "wiki": "http:\/\/ja.wikipedia.org\/w\/index.php?title=%E6%9D%BE%E5%89%8D%E5%85%AC%E5%9C%92&action=edit&redlink=1",
  "name": "松前公園",
  "pref": "北海道",
  "longitude": "41.430848",
  "latitude": "140.10868"
 },
 {
  "wiki": "http:\/\/ja.wikipedia.org\/w\/index.php?title=%E4%BA%8C%E5%8D%81%E9%96%93%E9%81%93%E8%B7%AF%E6%A1%9C%E4%B8%A6%E6%9C%A8&action=edit&redlink=1",
  "name": "二十間道路桜並木",
  "pref": "北海道",
  "longitude": "42.386001",
  "latitude": "142.422621"
 }
]
"""

@Preview(showBackground = true)
@Composable
fun CherryListPreview() {

  var list = getCherryList(teststr)
  var filtered = list.filter { it.name.contains("道路") }
  CherryBlossomsTheme {
    CherryList(list = list, onSelected = {})
  }
}




/*

https://github.com/kittinunf/Fuel



<!-- このパーミッションが必須 -->
<uses-permission android:name="android.permission.INTERNET" />

モジュールのbuild.gradleに下記追加
    /// Fuel library
    def fuel_version = "2.3.1"
    implementation "com.github.kittinunf.fuel:fuel:$fuel_version"
    implementation "com.github.kittinunf.fuel:fuel-android:$fuel_version"


* */