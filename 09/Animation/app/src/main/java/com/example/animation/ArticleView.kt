package com.example.animation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.example.animation.ui.theme.AnimationTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun ArticleView(article: Article,
                modifier: Modifier = Modifier
) {

    //Column(
        // Columnがスクロールするように設定するには次のように
        // モディファイアでverticalScrollを設定します。
        // https://developer.android.com/jetpack/compose/gestures?hl=ja#scroll-modifiers
//        modifier = Modifier
//            .verticalScroll(rememberScrollState())
    //) {
        // https://developer.android.com/jetpack/compose/libraries#image-loading
        // Instacart の Coil ライブラリには、ネットワーク経由でリモート画像を読み込むなど、外部ソースから画像を読み込むためのコンポーズ可能な関数が用意されています。
        // CoilはKotlin コルーチンに支えられた Android 用の画像読み込みライブラリ。
        // 高速・軽量・使いやすいライブラリです。
        // implementation("io.coil-kt:coil-compose:2.2.2")
        // で導入。
        // <uses-permission android:name="android.permission.INTERNET" />
        // 追加
        // https://coil-kt.github.io/coil/compose/#observing-asyncimagepainterstate
        // ここでは、picsum.photosというサービスを使っています。「Lorem Picsum（https://picsum.photos/）」はフォトサービス「Unsplash」の画像を利用したWEBサービスで、無料のダミー画像を使うことができます。
        // お好きな画像URLに変更して頂いても良いでしょう。



        Box(
            modifier = modifier.clip(shape = RoundedCornerShape(20.dp))
                .background(LightGray),
            contentAlignment = Alignment.BottomCenter
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth().height(height = 140.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(article.image)
                    .crossfade(true)
                    // coliのAsyncImageは高機能、メモリキャッシュ機能・ディスクキャッシュ機能も標準装備。無効にしたいばあいはdiskCachePolicyでDISABLEDにすることもできます。
                    //.memoryCachePolicy(CachePolicy.DISABLED)
                    .diskCachePolicy(CachePolicy.DISABLED)
                    .transformations(RoundedCornersTransformation(40f))
                    .build(),
                contentDescription = article.caption,
                contentScale = ContentScale.FillWidth
            )
            Box(modifier = Modifier.fillMaxWidth().background(color = Color.White.copy(alpha = 0.5f)).padding(8.dp)) {
                Text(text = article.title, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            }
        }
    //}
}

@Preview(showBackground = true)
@Composable
fun ArticleViewPreview() {
    AnimationTheme {
        val article = Article(
            title = "タイトル",
            caption = "キャプション",
            image = "https://picsum.photos/seed/5/200"
        )
        ArticleView(article = article)
    }
}