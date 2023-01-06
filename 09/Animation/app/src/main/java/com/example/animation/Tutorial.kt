package com.example.animation

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animation.ui.theme.AnimationTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

// チュートリアル画面
@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tutorial(
    modifier: Modifier = Modifier,
    @DrawableRes imageResource: Int,
    title: String,
    description: String,
    pageNo: Int,
    pagerState: PagerState,
    pageOffset: Float = 0f,
) {

    val alpha by animateFloatAsState(
//        max( 0.0f, // 最小値を0に制限
//            min(
//                1.0f, // 最大値を1に制限
//                if (pagerState.currentPage == pageNo)
//                    1.0f - abs(pagerState.currentPageOffset * 2.0f)
//                else 0.0f
//            )
//        )
        //max( 0.0f,min(1.0f,1.0f - pageOffset))
        1f - pageOffset
    )
    val offset by animateDpAsState(
        (pageOffset * 100).dp
    )

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = imageResource),
            contentScale = ContentScale.Crop,
            contentDescription = description
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(60.dp))

            Text(
                modifier = Modifier.offset(y = offset),
                text = title,
                fontFamily = FontFamily.Monospace,
                fontSize = 30.sp,
                color = Color.White.copy(alpha = alpha)
            )

            Spacer(modifier = Modifier.size(30.dp))

            Text(
                modifier = Modifier.offset(y = -offset),
                text = description,
                fontFamily = FontFamily.SansSerif,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = alpha)
            )

        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
fun TutorialPreview() {
    AnimationTheme {
        Tutorial(
            imageResource = R.drawable.tutorial0,
            title = "Lorem ipsum dolor sit amet",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. \n" +
                    "Dui id ornare arcu odio ut sem nulla pharetra diam. \n" +
                    "Risus viverra adipiscing",
            pageNo = 0,
            pagerState = PagerState(),
            pageOffset = 0.0f
        )
    }
}
