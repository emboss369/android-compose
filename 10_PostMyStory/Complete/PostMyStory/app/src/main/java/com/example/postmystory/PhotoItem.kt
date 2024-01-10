package com.example.postmystory

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest



/**
 * 画像をタップしたときに、画像のURLを返す。
 *
 * @param photo 画像のURL文字列
 * @param onClick タップ時の処理
 *
 * AsyncImageを使い、画像を表示します。
 * AsyncImageのmodelパラメータにはImageRequest.Builderを使い、画像のURLを指定し、crossfadeを有効にし、ディスクキャッシュ機能を無効にします。
 * placeholderには、画像が読み込まれるまで表示する画像としてnow_loading.pngを指定します。
 * contentScaleには、画像の表示方法を指定します。FillWidthを指定すると、画像の幅を領域いっぱいに表示します。
 */
@Composable
fun PhotoItem(photo: String, onClick: (String) -> Unit) {
  AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
      .data(photo)
      .crossfade(true)
      .diskCachePolicy(CachePolicy.DISABLED)
      .build(),
    contentDescription = null,
    modifier = Modifier.clickable { onClick(photo) },
    contentScale = ContentScale.FillWidth,
    placeholder = painterResource(id = R.drawable.now_loading)
  )
}

@Preview
@Composable
fun PhotoItemPreview() {
  PhotoItem(
    photo = "https://picsum.photos/200/200",
    onClick = {}
  )
}



