package com.example.order

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.order.ui.theme.OrderTheme

@Composable
fun TopView(onTapButton: () -> Unit) {
  // https://developer.android.com/reference/kotlin/androidx/compose/foundation/package-summary?hl=ja#Image(androidx.compose.ui.graphics.painter.Painter,kotlin.String,androidx.compose.ui.Modifier,androidx.compose.ui.Alignment,androidx.compose.ui.layout.ContentScale,kotlin.Float,androidx.compose.ui.graphics.ColorFilter)

  Column(
    modifier = Modifier.background(Color.Yellow),
    // こっちはなぜかアライメント（整列）
    horizontalAlignment = Alignment.CenterHorizontally, // 横方向
    // こっちはなぜかアレンジメント（配置）紛らわしい！！！
    //verticalArrangement = Arrangement.Center // 縦方向
    verticalArrangement = Arrangement.SpaceEvenly // 空白を均等に
  ) {



    // https://developer.android.com/jetpack/compose/text?hl=ja
    Box(contentAlignment = Alignment.Center) {

      Image(
        painter = painterResource(id = R.drawable.hamburger),
        contentDescription = "hamburger"
      )
      Text(
        // text = "Colbar's burger",
        text = buildAnnotatedString {
          withStyle(style = SpanStyle(color = Color.Red)) {
            append("Colbar's")
          }
          append(" burger ")
        },
        // Modifierそれぞれのコンポーザブルを修飾する、もしくは拡張するときに利用する。
        modifier = Modifier
          .background(color = Color.Transparent)
          .width(300.dp),
        // テキストの色
        color = Color.Blue,
        // 文字サイズを変更する
        fontSize = 80.sp,
        // テキストを斜体にする
        fontStyle = FontStyle.Italic,
        // テキストを太字にする
        fontWeight = FontWeight.Bold,
        // テキストの配置
        textAlign = TextAlign.Center,
        // テキストのスタタイル
        style = TextStyle(
          shadow = Shadow(
            color = Color.White,
            offset = Offset(10.0f, 10.0f),
            blurRadius = 0.0f
          )
        ),
        // フォントの変更（Serif、Sans Serif、Monospace、Cursive ）
        fontFamily = FontFamily.SansSerif
      )

    }
    Button(onClick = {
      // 状態ホイスティング
      onTapButton()
    }) {
      Text("注文へ")
    }
  }
}
@Preview(widthDp = 360, heightDp = 640, showBackground = true)
@Composable
fun TopViewFirstPreview() {
  OrderTheme{
    Column(
      modifier = Modifier.background(Color.Yellow),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceEvenly
    ){
      Box{
        // Image()
        Text(
          text = "Colbar's burger",
          modifier = Modifier
            .background(color = Color.Red)
            .width(300.dp)
        )
      }
      Button(onClick = { /*TODO*/ }) {
        Text("注文へ")
      }
    }
  }

}
@Preview(widthDp = 360, heightDp = 640, showBackground = true)
@Composable
fun TopViewPreview() {
  OrderTheme{
    TopView(onTapButton = {})
  }
  
}

