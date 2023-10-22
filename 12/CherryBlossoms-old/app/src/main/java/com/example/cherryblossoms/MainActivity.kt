package com.example.cherryblossoms

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cherryblossoms.ui.theme.CherryBlossomsTheme
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var list = getCherryList(getJson(resources))

        setContent {
            CherryBlossomsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Greeting("Android")
                    CherryList(list = list)
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
    CherryBlossomsTheme {
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
に記載のある通りですが、

plugins {
    id 'org.jetbrains.kotlin.multiplatform' version '1.8.10'
    id 'org.jetbrains.kotlin.plugin.serialization' version '1.8.10'
}
と、
dependencies {
    implementation "org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0"
}
 */

/*
100Cherry_List.jsonは、LinkData（http://linkdata
.org/）で公開されている、日本さくら名所100選、というデータを利用しました。
このデータをXMLに変換して利用しています。
assetsに入れます。
*/

@Serializable
data class Cherry(val wiki: String, val name: String, val pref: String,
                  val longtude: String, val latitude: String)

fun getJson(resources: Resources) : String {
    val assetManager = resources.assets
    val inputStream = assetManager.open("100Cherry_List.json")
    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
    return bufferedReader.readText()
}

fun getCherryList(str: String) : List<Cherry> {
    val obj = Json.decodeFromString<List<Cherry>>(str)
    return obj
}

@Composable
fun CherryList(list: List<Cherry>) {
    Column {
        list.forEach { cherry ->
            CherryRow(cherry)
        }
    }
}

@Composable
fun CherryRow(cherry: Cherry) {
    Text(cherry.name)
}

// テスト用に２件のデータを使うのがよい。どくしゃにもわかりやすかろう
var teststr = """"
[{
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
}]
""""

@Preview(showBackground = true)
@Composable
fun CherryListPreview() {
    var list = getCherryList(teststr)
    CherryBlossomsTheme {
        CherryList(list = list)
    }
}
