package com.example.myslideshow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FirstPage
import androidx.compose.material.icons.filled.LastPage
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.myslideshow.ui.theme.MySlideshowTheme
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MySlideshowTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Sample()
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun SamplePreview() {
    MySlideshowTheme {
        Sample()
    }
}

// https://google.github.io/accompanist/pager/
// pager機能は執筆時点ではComoseにありません。
// そのため「Accompanist」を使います。これはまだComposeに採用されていない実験的な機能を集めたものです。

// 設定方法
// https://github.com/google/accompanist/tree/main/pager
// dependencies {
//     implementation "com.google.accompanist:accompanist-pager:<version>"
// }

// build.gradleを見ると、
// 現在のバージョンが、
//         compose_ui_version = '1.2.0'
// となっていた、
// https://github.com/google/accompanist#compose-versions
// を見ると、1.2の最新は
// v0.25.1
// なので、0.25.1
// implementation "com.google.accompanist:accompanist-pager:0.25.1"

val resources = listOf(
    R.drawable.slide00, R.drawable.slide01,
    R.drawable.slide02, R.drawable.slide03,
    R.drawable.slide04, R.drawable.slide05,
    R.drawable.slide06, R.drawable.slide07,
    R.drawable.slide08, R.drawable.slide09
)

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun Sample() {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "スライドショー") },
                backgroundColor = Color.White
            )
        },
        modifier = Modifier.fillMaxSize(),
        backgroundColor = Color.White
    ) { padding ->
//        Image(
//            painter = painterResource(id = resources[0]),
//            alignment = Alignment.Center,
//            contentScale = ContentScale.Crop,
//            contentDescription = "null",
//            modifier = Modifier.fillMaxSize().padding(padding).border(3.dp,Color.Red)
//        )
        Column(Modifier
            //.fillMaxSize()
            .padding(padding)) {
            val pagerState = rememberPagerState()

            // Display 10 items
            VerticalPager(
                count = 10,
                state = pagerState,
                // Add 32.dp horizontal padding to 'center' the pages
                contentPadding = PaddingValues(vertical = 128.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) { page ->
                PagerSampleItem(
                    imageResource = resources[page],
                    modifier = Modifier
                        .fillMaxSize()  // 画面いっぱいに広げるために必要
                )
            }
            ActionsRow(
                pagerState = pagerState,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

/**
 * Simple pager item which displays an image
 */
@OptIn(ExperimentalCoilApi::class)
@Composable
internal fun PagerSampleItem(
    imageResource: Int,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(id = imageResource),
        alignment = Alignment.Center,
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = modifier   // 画面いっぱいに広げるために必要
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun ActionsRow(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        val scope = rememberCoroutineScope()

        IconButton(
            enabled = pagerState.currentPage > 0,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(0)
                }
            }
        ) {
            Icon(imageVector = Icons.Default.FirstPage, contentDescription = null)
        }

        IconButton(
            enabled = pagerState.currentPage > 0,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            }
        ) {
            Icon(Icons.Default.NavigateBefore, null)
        }

        IconButton(
            enabled = pagerState.currentPage < pagerState.pageCount - 1,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }
        ) {
            Icon(Icons.Default.NavigateNext, null)
        }

        IconButton(
            enabled = pagerState.currentPage < pagerState.pageCount - 1,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.pageCount - 1)
                }
            }
        ) {
            Icon(Icons.Default.LastPage, null)
        }
    }
}