@file:OptIn(ExperimentalAnimationApi::class)

package com.example.animation

import android.content.res.Resources
import android.os.Bundle
import kotlin.math.min
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.animation.ui.theme.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.lerp
import androidx.compose.ui.text.style.lerp
import androidx.compose.ui.unit.*
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.absoluteValue
import androidx.compose.ui.util.lerp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        topBar = { TopAppBar(title = { Text("Animation", color = Color.White) }, backgroundColor = Color.Black)},
                        backgroundColor = MaterialTheme.colors.background,
                        content = { innerPadding ->

                            TutorialPager()

                        }
                    )
                }
            }
        }
    }
}



@OptIn(ExperimentalPagerApi::class)
@Composable
fun TutorialPager() {
    // https://google.github.io/accompanist/pager/
    // pager機能は執筆時点ではComoseにありません。
    // そのため「Accompanist」を使います。これはまだComposeに採用されていない実験的な機能を集めたものです。

    val state = rememberPagerState()
    //val slideImage = remember { mutableStateOf(R.drawable.tutorial0) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        HorizontalPager(count = 3, state = state) { page ->
            val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                when (page) {

                0 -> {
                    Tutorial(
                        // ビュー全体を拡大縮小する。
//                        modifier = Modifier.graphicsLayer {
//
//                            // leapをインポート
//                            // implementation "androidx.compose.ui:ui-util:$compose_ui_version"
//                            lerp(
//                                start = 0.15f,
//                                stop = 1f,
//                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
//                            ).also { scale ->
//                                scaleX = scale
//                                scaleY = scale
//                            }
//
//                            alpha = lerp(
//                                start = 0.5f,
//                                stop = 1f,
//                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
//                            )
//                        },
                        imageResource = R.drawable.tutorial0,
                        title = "いつは十月もっともこういう束縛者に対するのの中にするたで",
                        description = "ほとんど今に所有方はしかるにその増減たろありくらいでおらてしまうたからは発展願いたですて、少しには廻るだろうましでしょ。一種になりんのはもっとも場合をよくななけれです。",
                        pageNo = page,
                        pagerState = state,
                        pageOffset = pageOffset.coerceIn(0f, 1f)

                        )
                }

                1 -> {
                    Tutorial(
//                        modifier = Modifier.graphicsLayer {
//
//                            // leapをインポート
//                            // implementation "androidx.compose.ui:ui-util:$compose_ui_version"
//                            lerp(
//                                start = 0.15f,
//                                stop = 1f,
//                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
//                            ).also { scale ->
//                                scaleX = scale
//                                scaleY = scale
//                            }
//
//                            alpha = lerp(
//                                start = 0.5f,
//                                stop = 1f,
//                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
//                            )
//                        },
                        imageResource = R.drawable.tutorial1,
                        title = "ここも今よほどどんな尊敬国というものの時をあるなけれで",
                        description = "どうしても一番に記憶感しかもしこの仕事だなともの書いてつけるませがは使用思うないますば、こうにはもっなかっですないない。",
                        pageNo = page,
                        pagerState = state,
                        pageOffset = pageOffset.coerceIn(0f, 1f)
                    )
                }

                2 -> {
                    Tutorial(
                        imageResource = R.drawable.tutorial2,
                        title = "これは時間ああその相談人ていうののためを聴いなりだ",
                        description = "もう場合が損害屋もいったんその滅亡たたでもに済ましてみたのも見当したますて、突然にはありただますな。\n" +
                                "\n" +
                                "人がしないものは毫も今にもうないませです。",
                        pageNo = page,
                        pagerState = state,
                        pageOffset = pageOffset.coerceIn(0f, 1f)
                    )
                }
            }

//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            Image(
//                painterResource(slideImage.value),
//                contentDescription = ""
//            )
//        }

            // Debug
            Row() {
                Spacer(modifier = Modifier.size(300.dp))
                Text(text = "${state.currentPage}  ${state.currentPageOffset} ${pageOffset.coerceIn(0f, 1f)}", color = Color.Yellow)
            }


        }

        DotsIndicator(
            modifier = Modifier.padding(16.dp),
            totalDots = 3,
            selectedIndex = state.currentPage,
            selectedColor = Color.White,
            unSelectedColor = Color.Gray
        )


    }


}

@Preview(showBackground = true)
@Composable
fun TutorialPagerPreview() {
    AnimationTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Animation", color = Color.White) }, backgroundColor = Color.Black)},
            backgroundColor = MaterialTheme.colors.background,
            content = { innerPadding ->

                TutorialPager()

            }
        )
    }
}

