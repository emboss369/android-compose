package com.example.cherryblossoms

import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.example.cherryblossoms.data.ApiSearch
import kotlinx.coroutines.launch

@Composable
fun WikipediaRow(search: ApiSearch, onSelected: (ApiSearch) -> Unit) {
  val scope = rememberCoroutineScope()
  Column(modifier = Modifier
    .fillMaxWidth()
    .background(MaterialTheme.colorScheme.secondaryContainer)
    .clickable {
      scope.launch {
        onSelected(search)
      }
    }) {

    Text(
      text =  convertToJST(search.timestamp),
      color = MaterialTheme.colorScheme.onSecondary,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.background(
        color = MaterialTheme.colorScheme.secondary
      )
    )
    Text(
      text = search.title, fontSize = 30.sp, overflow = TextOverflow.Ellipsis,
      color = MaterialTheme.colorScheme.secondary
    )
    Row(
      horizontalArrangement = Arrangement.End,
      modifier = Modifier.fillMaxWidth()
    ) {

      // https://medium.com/@theAndroidDeveloper/displaying-html-text-in-jetpack-compose-7b801bb028c6
      // snippetにはHTML形式の文字列が入っているので、HtmlCompatクラスを使います。
      // HtmlCompat.fromHtml()メソッドを使用するとHtml 文字列を Android の TextView
      // で使えるSpanned オブジェクトに変換できます。
      // 関数の最初の引数はソース文字列で、2 番目の引数を使用すると、関数の解析動作をカスタマイズするために使用できる特定のフラグを指定できます。
      // 各フラグの使用方法の説明は、この記事の範囲外です。
      // フラグを指定する必要がない場合は、単に 0 を渡すことができます。
      val snippetText = HtmlCompat.fromHtml(search.snippet, 0)

      AndroidView(factory = { TextView(it) },
        update = { it.text = snippetText })
    }
  }
}

@Preview(showBackground = true)
@Composable
fun WikipediaRowPreview() {
  WikipediaRow(
    search = ApiSearch(
      ns = 0,
      title = "桜",
      pageid = 1,
      size = 1,
      wordcount = 1,
      snippet = "桜（さくら）は、バラ科サクラ属の落葉高木である。",
      timestamp = "2021-10-01T00:00:00Z"
    )
  ) {}
}