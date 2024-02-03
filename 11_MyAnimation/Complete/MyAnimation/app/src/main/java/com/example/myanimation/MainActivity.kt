package com.example.myanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myanimation.ui.theme.MyAnimationTheme
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MyAnimationTheme {
        // A surface container using the 'background' color from the theme
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          TutorialPager()
        }
      }
    }
  }
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TutorialPager() {
  val imageRes = intArrayOf(
    R.drawable.tutorial0,R.drawable.tutorial1,R.drawable.tutorial2
  )
  val title = arrayOf(
    "いつは十月もっともこういう束縛者に対するのの中にするたで",
    "ここも今よほどどんな尊敬国というものの時をあるなけれで",
    "これは時間ああその相談人ていうののためを聴いなりだ"
  )
  val description = arrayOf(
    "ほとんど今に所有方はしかるにその増減たろありくらいでおらてしまうたからは発展願いたですて、少しには廻るだろうましでしょ。一種になりんのはもっとも場合をよくななけれです。",
    "どうしても一番に記憶感しかもしこの仕事だなともの書いてつけるませがは使用思うないますば、こうにはもっなかっですないない。",
    "もう場合が損害屋もいったんその滅亡たたでもに済ましてみたのも見当したますて、突然にはありただますな。\n" + "\n" + "人がしないものは毫も今にもうないませです。"
  )
  val pagerState = rememberPagerState(pageCount = { 3 })
  Box(
    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
  ){
    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
      val pageOffset =
        ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction)
          .absoluteValue
      Tutorial(
        imageRes = imageRes[page],
        title = title[page],
        description = description[page],
        pageOffset = pageOffset.coerceIn(0f, 1f)
      )
    }
    DotsIndicator(
      modifier = Modifier.padding(bottom = 50.dp),
      totalDots = 3,
      selectedIndex = pagerState.currentPage,
      selectedColor = MaterialTheme.colorScheme.primary,
      unselectedColor = MaterialTheme.colorScheme.onBackground
    )
  }
}

@Preview(showBackground = true)
@Composable
fun TutorialPagerPreview() {
  MyAnimationTheme {
    TutorialPager()
  }
}