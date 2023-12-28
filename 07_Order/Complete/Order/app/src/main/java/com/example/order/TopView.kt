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
  Column(
    modifier = Modifier.background(Color.Yellow),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceEvenly
  ) {
    Box(contentAlignment = Alignment.Center) {
      Image(
        painter = painterResource(id = R.drawable.hamburger),
        contentDescription = "hamburger"
      )
      Text(
        text = buildAnnotatedString {
          withStyle(style = SpanStyle(color = Color.Red)) {
            append("Colbar's")
          }
          append(" burger ")
        },
        modifier = Modifier
          .background(color = Color.Transparent)
          .width(300.dp),
        color = Color.Blue,
        fontSize = 80.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
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
//@Preview(widthDp = 360, heightDp = 640, showBackground = true)
//@Composable
//fun TopViewFirstPreview() {
//  OrderTheme{
//    Column(
//      modifier = Modifier.background(Color.Yellow),
//      horizontalAlignment = Alignment.CenterHorizontally,
//      verticalArrangement = Arrangement.SpaceEvenly
//    ){
//      Box{
//        // Image()
//        Text(
//          text = "Colbar's burger",
//          modifier = Modifier
//            .background(color = Color.Red)
//            .width(300.dp)
//        )
//      }
//      Button(onClick = { /*TODO*/ }) {
//        Text("注文へ")
//      }
//    }
//  }
//}
@Preview(widthDp = 360, heightDp = 640, showBackground = true)
@Composable
fun TopViewPreview() {
  OrderTheme{
    TopView(onTapButton = {})
  }
  
}

