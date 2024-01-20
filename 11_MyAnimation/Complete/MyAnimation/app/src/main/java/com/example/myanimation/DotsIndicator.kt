package com.example.myanimation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myanimation.ui.theme.MyAnimationTheme

/**
 * インジケータのドットを表示します。
 *
 * @param totalDots ドットの総数
 * @param selectedIndex 選択されているドットのインデックス
 * @param selectedColor 選択されているドットの色
 * @param unSelectedColor 選択されていないドットの色
 * @param modifier Modifier
 */
@Composable
fun DotsIndicator(
  totalDots: Int,
  selectedIndex: Int,
  selectedColor: Color,
  unSelectedColor: Color,
  modifier: Modifier = Modifier
) {
  LazyRow(
    modifier = modifier
      .wrapContentWidth()
      .wrapContentHeight()
  ) {
    items(totalDots) { index ->
      val color by animateColorAsState(
        targetValue = if (index == selectedIndex) selectedColor
        else unSelectedColor,
        animationSpec = tween(1000),
        label = "dot color animation"
      )
      Box(
        modifier = Modifier
          .size(10.dp)
          .clip(CircleShape)
          .background(color)
      )
      if (index != totalDots - 1) {
        Spacer(modifier = Modifier.padding(horizontal = 2.dp))
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun DotIndicatorPreview() {
  MyAnimationTheme {
    DotsIndicator(
      totalDots = 10,
      selectedIndex = 1,
      selectedColor = Color.Black,
      unSelectedColor = Color.LightGray
    )
  }
}