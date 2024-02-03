package com.example.cherryblossoms

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.TextView
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cherryblossoms.ui.theme.CherryBlossomsTheme
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader


class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      CherryBlossomsTheme {
        // A surface container using the 'background' color from the theme
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          // Greeting("Android")
          WikipediaView(onSelected = {})
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

var testapi = """
{"batchcomplete":true,"continue":{"sroffset":2,"continue":"-||images"},"query":{"searchinfo":{"totalhits":1495},"search":[{"ns":0,"title":"サクラ","pageid":7303,"size":123191,"wordcount":17278,"snippet":"桜祭りが行われる桜の木","timestamp":"2023-06-17T22:52:12Z"},{"ns":0,"title":"赤城南面千本桜","pageid":3100255,"size":3699,"wordcount":407,"snippet":"赤城南面千本桜は、群馬県前橋市にある<span class=\"searchmatch\">桜の名所</span>。","timestamp":"2019-08-08T23:22:12Z"}]}}
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



// RestAPIも試してみましょう。
// REST APIは、Webシステムを外部から利用するためのプログラムの呼び出し規約 (API)の種類の一つで、RESTと呼ばれる設計原則に従って策定されたものです。RESTは、Representational State Transferの略で、Webシステムを構築するためのアーキテクチャスタイルの一つです。REST APIは、HTTPプロトコルを使用して、Webシステムに対してリクエストを送信し、レスポンスを受け取ることができます。
// MediaWikiはWikipedia
// のような巨大なオンライン百科事典を運営するために開発されたソフトウェアです。
// MediaWikiは、GNU General Public Licenseで配布されるフリーソフトウェアであり、
// MySQLやPostgreSQL、SQLiteなどのデータベースを使用することができます。
// https://www.mediawiki.org/wiki/Manual:What_is_MediaWiki%3F/ja
// MediaWikiには、REST APIがあります。MediaWiki REST APIは、開発者に長期的な安定性を提供するように設計され、APIパスのグローバルバージョン番号を使ってバージョンを管理します（例：/v1/）1。MediaWiki REST APIは、ウィキメディアのコンテンツとメタデータにアクセスするためのAPIであり、機械可読形式で提供されます2。
// HTTPリクエストをrest.php URLに送信することでMediaWikiとやり取りすることができます。APIを使用して、Wikiページを検索して表示したり、メディアファイルを取得したり、ページ履歴を調べたりするアプリやスクリプトを作成することができます1。

/* まずは、一覧を取得しましょう。

MediaWiki REST APIとMediaWiki Action APIは異なるAPIです。

MediaWiki Action APIを使用して、「桜の名所」のデータをJSON形式で取得するには、以下の手順を実行します。

1. MediaWiki APIエンドポイントにアクセスします。 このエンドポイントは、すべてのMediaWiki APIリクエストで使用されます。通常、MediaWikiのURLに「/api.php」と付け足したものになります。たとえば、「https://ja.wikipedia.org/w/api.php」は、日本語版のWikipediaのAPIエンドポイントです。

2. 次に、APIエンドポイントに対して、以下のようなリクエストを送信します。

https://ja.wikipedia.org/w/api.php?action=query&format=json&srprop=isfilematch&list=search&formatversion=2&imlimit=1&srsearch=%E6%A1%9C%E3%81%AE%E5%90%8D%E6%89%80&srlimit=50

```
https://ja.wikipedia.org/w/api.php?action=query&format=json&list=search&srsearch=%E6%A1%9C%E3%81%AE%E5%90%8D%E6%89%80
```

このリクエストには、以下のパラメータが含まれています。

- action=query: APIアクションをqueryに設定します。queryアクションは、MediaWikiから情報を取得するための一般的な方法です。
- format=json: APIからの応答をJSON形式で取得します。
- list=search: 検索エンジンを使用して、指定されたキーワードに一致するページを検索します。
- srsearch=%E6%A1%9C%E3%81%AE%E5%90%8D%E6%89%80: srsearchパラメータに検索クエリを設定します。ここでは、「桜の名所」をUTF-8でエンコードした文字列を指定しています。

その他のパラメータについては https://www.mediawiki.org/wiki/API:Main_page/ja
に詳しく記載されていますので参考にしてください。

3. APIは、以下のような応答を返します。

```json
{
    "batchcomplete": true,
    "continue": {
        "sroffset": 20,
        "continue": "-||images"
    },
    "query": {
        "searchinfo": {
            "totalhits": 1490
        },
        "search": [
            {
                "ns": 0,
                "title": "サクラ",
                "pageid": 7303,
                "size": 122864,
                "wordcount": 17241,
                "snippet": "<span class=\"searchmatch\">の名所</span>である韓国国会と汝矣島周辺に植えられたサクラのうち9割以上が日本原産のソメイヨシノであり、韓国原産の王桜は1本も植えられていなかったことが判明した。また桜祭りが行われる<span class=\"searchmatch\">桜の名所</span>として有名な鎮海の女座川沿いの99.7%、慶和駅周辺の<span class=\"searchmatch\">桜の</span>木の91",
                "timestamp": "2023-04-29T11:26:39Z"
            },
            {
                "ns": 0,
                "title": "赤城南面千本桜",
                "pageid": 3100255,
                "size": 3699,
                "wordcount": 407,
                "snippet": "赤城南面千本桜（あかぎなんめんせんぼんざくら）は、群馬県前橋市にある<span class=\"searchmatch\">桜の名所</span>。日本さくら<span class=\"searchmatch\">名所</span>100選。 前橋市宮城地区苗ヶ島町、赤城山の南側斜面を通過する国道353号から忠治温泉へと至る道に整備された桜並木道である。約3.5キロメートルの区間にわたって約1,400本のソメイヨシノが植えられている。",
                "timestamp": "2019-08-08T23:22:12Z"
            },
            ...
        ]
    }
}
```
 */


// では、上記JSONを受け取るデータを作成しましょう
// 全部定義してみましたが、今回使用するのはtitileとsnippetだけです。
@Serializable
data class ApiSearch(
  val ns: Integer,
  val title: String,
  val pageid: Integer,
  val size: Integer,
  val wordcount: Integer,
  val snippet: String,
  val timestamp: String
)

// searchinfoとsearchがありますが、searchinfoは今回使わないので省略しましょう。
@Serializable
data class ApiQuery(
  val search: List<ApiSearch>
)
// batchcomplete,continue,queryがありますが、query以外使いません。
@Serializable
data class ApiResult(
  val query: ApiQuery
)

/*
MediaWiki REST APIとMediaWiki Action APIは異なるAPIです。
ウィキペディアにには、MediaWiki REST APIをベースとしたWikimedia REST APIが用意されていますので、この
Wikimedia REST APIを使ってアクセスします。
メディアウィキと読んだりウィキメディアと読んだりややこしいですが、要は同じものです。
ウィキペディアのために作られたオープンソースソフトウェアがMediaWikiです。いろんなサイトでMediaWikiが利用されています。


MediaWiki REST APIを使用して、「桜の名所」のデータをJSON形式で取得するには、以下の手順を実行します。

1. リクエストを送信するためのAPIエンドポイントにアクセスします。MediaWiki REST APIのAPIエンドポイントは、以下のようになります。

```
https://ja.wikipedia.org/api/rest_v1/

```

2. 次に、以下のようなリクエストを送信します。

```
https://ja.wikipedia.org/api/rest_v1/page/summary/%E3%82%B5%E3%82%AF%E3%83%A9

%E3%82%B5%E3%82%AF%E3%83%A9は、「サクラ」をURLデコードした文字列です。

```

このリクエストには、「summary」エンドポイントを使用して、「サクラ」の要約データを取得するように指示しています。

3. APIは、以下のような応答を返します。

```json
{
	"type": "standard",
	"title": "サクラ",
	"displaytitle": "<span class=\"mw-page-title-main\">サクラ</span>",
	"namespace": {
		"id": 0,
		"text": ""
	},
	"wikibase_item": "Q871991",
	"titles": {
		"canonical": "サクラ",
		"normalized": "サクラ",
		"display": "<span class=\"mw-page-title-main\">サクラ</span>"
	},
	"pageid": 7303,
	"thumbnail": {
		"source": "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e2/2020-04-07_Prunus_%C3%97_yedoensis_Tambasasayama%2CHyogo%28%E4%B8%B9%E6%B3%A2%E7%AF%A0%E5%B1%B1%E5%B8%82%E7%AF%A0%E5%B1%B1%E5%B7%9D%E3%81%AE%E3%82%BD%E3%83%A1%E3%82%A4%E3%83%A8%E3%82%B7%E3%83%8E%29DSCF2986%E2%98%86%E5%BD%A1.jpg/320px-2020-04-07_Prunus_%C3%97_yedoensis_Tambasasayama%2CHyogo%28%E4%B8%B9%E6%B3%A2%E7%AF%A0%E5%B1%B1%E5%B8%82%E7%AF%A0%E5%B1%B1%E5%B7%9D%E3%81%AE%E3%82%BD%E3%83%A1%E3%82%A4%E3%83%A8%E3%82%B7%E3%83%8E%29DSCF2986%E2%98%86%E5%BD%A1.jpg",
		"width": 320,
		"height": 213
	},
	"originalimage": {
		"source": "https://upload.wikimedia.org/wikipedia/commons/e/e2/2020-04-07_Prunus_%C3%97_yedoensis_Tambasasayama%2CHyogo%28%E4%B8%B9%E6%B3%A2%E7%AF%A0%E5%B1%B1%E5%B8%82%E7%AF%A0%E5%B1%B1%E5%B7%9D%E3%81%AE%E3%82%BD%E3%83%A1%E3%82%A4%E3%83%A8%E3%82%B7%E3%83%8E%29DSCF2986%E2%98%86%E5%BD%A1.jpg",
		"width": 2304,
		"height": 1536
	},
	"lang": "ja",
	"dir": "ltr",
	"revision": "94989758",
	"tid": "b77872c0-e680-11ed-b970-1768c5a3a24c",
	"timestamp": "2023-04-29T11:26:39Z",
	"description": "サクラ属の樹木の総称",
	"description_source": "central",
	"content_urls": {
		"desktop": {
			"page": "https://ja.wikipedia.org/wiki/%E3%82%B5%E3%82%AF%E3%83%A9",
			"revisions": "https://ja.wikipedia.org/wiki/%E3%82%B5%E3%82%AF%E3%83%A9?action=history",
			"edit": "https://ja.wikipedia.org/wiki/%E3%82%B5%E3%82%AF%E3%83%A9?action=edit",
			"talk": "https://ja.wikipedia.org/wiki/%E3%83%8E%E3%83%BC%E3%83%88:%E3%82%B5%E3%82%AF%E3%83%A9"
		},
		"mobile": {
			"page": "https://ja.m.wikipedia.org/wiki/%E3%82%B5%E3%82%AF%E3%83%A9",
			"revisions": "https://ja.m.wikipedia.org/wiki/Special:History/%E3%82%B5%E3%82%AF%E3%83%A9",
			"edit": "https://ja.m.wikipedia.org/wiki/%E3%82%B5%E3%82%AF%E3%83%A9?action=edit",
			"talk": "https://ja.m.wikipedia.org/wiki/%E3%83%8E%E3%83%BC%E3%83%88:%E3%82%B5%E3%82%AF%E3%83%A9"
		}
	},
	"extract": "サクラ は、バラ科サクラ亜科サクラ属 （スモモ属とすることもある。「野生種の分類」の項を参照）の落葉広葉樹の総称。またはその花である。一般的に春に桜色と表現される白色や淡紅色から濃紅色の花を咲かせる。",
	"extract_html": "<p><b>サクラ</b> は、バラ科サクラ亜科サクラ属 （スモモ属とすることもある。「野生種の分類」の項を参照）の落葉広葉樹の総称。またはその花である。一般的に春に桜色と表現される白色や淡紅色から濃紅色の花を咲かせる。</p>"
}
```

応答には、ページの情報が含まれています。応答には、タイトル、サムネイル画像、および要約などの情報が含まれています。

 */

// fuel HTTP ライブラリを使ってみる。
// REST APIにアクセスするには、HTTP/HTTPS通信を行う必要があります。通常、HttpURLConnection
// クラスを利用して通信を行うことが出来ます。今回はHttpURLConnectionよりも簡単に記述できるfuel
// というオープンソース・ライブラリを利用します。

// kotlin/Android 向けの最も簡単な HTTP ネットワーキング ライブラリであるFuel HTTP ライブラリについて見ていきます。
//非同期およびブロック要求の両方の基本的な HTTP 動詞 (GET、POST、DELETE など) のサポート
//ファイルをダウンロードおよびアップロードする機能 ( multipart/form-data )
//グローバル構成を管理する可能性
//ビルトイン オブジェクト シリアル化モジュール (Jackson、Gson、Mhosi、Forge)
//Kotlin のコルーチン モジュールと RxJava 2.x のサポート
//Router の設計パターンを簡単にセットアップ


// 参考 https://github.com/kittinunf/fuel/tree/2.x

// 状態を保持する
data class UIState(
  var CherryList: List<ApiSearch>? = null
)

// ビジネスロジックを実装するクラス。ViewModelを使う。
// import com.github.kittinunf.fuel.httpGet
// import com.github.kittinunf.result.Result
// が必要
class DataViewModel() : ViewModel() {
  var uiState by mutableStateOf(UIState())
    private set // setterをprivateにして外部から使用できなくしている

  fun query() {
      val httpAsync = ("https://ja.wikipedia.org/w/api.php?" +
      "action=query&format=json&prop=images&list=search&formatversion=2&" +
      "imlimit=1&srsearch=%E6%A1%9C%E3%81%AE%E5%90%8D%E6%89%80&srlimit=50")
      .httpGet()
      .responseString { request, response, result ->
        when (result) {
          // import com.github.kittinunf.result.Resultをインポート。
          is Result.Failure -> {
            val ex = result.getException()
            Log.e("DataViewModel", ex.toString())
          }

          is Result.Success -> {
            val data = result.get()
            Log.e("DataViewModel", data)
            val result = apiResult(data)
            Log.d("result", result.query.toString())
            uiState = UIState(result.query.search)
          }
        }
      }
  }
}

fun apiResult(data: String): ApiResult {
  val result =
    Json { ignoreUnknownKeys = true }.decodeFromString<ApiResult>(data)
  return result
}

/*
build.gradleに追加します。

    implementation "androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1"

 */
// 以下のインポート必須です。
// import androidx.lifecycle.viewmodel.compose.viewModel
@Composable
fun WikipediaView(viewModel: DataViewModel = viewModel(), onSelected: (ApiSearch) -> Unit) {
  val uiState = viewModel.uiState

  viewModel.query()

  SearchResultList(list = uiState.CherryList, onSelected = {})

}

@Composable
fun SearchResultRow(search: ApiSearch, onSelected: (ApiSearch) -> Unit) {
  Column(modifier = Modifier
    .fillMaxWidth()
    .background(Color.Yellow)
    .clickable {
      onSelected(search)
    }
  ) {

    Text(
      text = search.timestamp,
      color = Color.White,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.background(
        color = Color(0xFFFF0000)
      )
    )


    Text(
      text = search.title,
      fontSize = 30.sp,
      overflow = TextOverflow.Ellipsis
    )
    Row(horizontalArrangement = Arrangement.End, modifier = Modifier
      .fillMaxWidth()) {

      // https://medium.com/@theAndroidDeveloper/displaying-html-text-in-jetpack-compose-7b801bb028c6
      // snippetにはHTML形式の文字列が入っているので、HtmlCompatクラスを使います。
      // HtmlCompat.fromHtml()メソッドを使用するとHtml 文字列を Android の TextView
      // で使えるSpanned オブジェクトに変換できます。
      // 関数の最初の引数はソース文字列で、2 番目の引数を使用すると、関数の解析動作をカスタマイズするために使用できる特定のフラグを指定できます。
      // 各フラグの使用方法の説明は、この記事の範囲外です。
      // フラグを指定する必要がない場合は、単に 0 を渡すことができます。
      val snippetText = HtmlCompat.fromHtml(search.snippet, 0 )

      AndroidView(
        factory = { TextView(it) },
        update = { it.text = snippetText }
      )

    }


  }
}

@Composable
fun SearchResultList(list: List<ApiSearch>?, onSelected: (ApiSearch) -> Unit) {

  Column(
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier
      .padding(all = 16.dp)
      .verticalScroll(rememberScrollState())
  ) {
    list?.forEach { search ->
      SearchResultRow(search = search, onSelected = onSelected)
    }
  }
}


@Preview(showBackground = true)
@Composable
fun DataViewModelPreview() {

var list = apiResult(testapi)
  CherryBlossomsTheme {
    SearchResultList(list = list.query.search, onSelected = {})
  }
}


// 以上、ここまで説明すれば十分。次の章へ。

